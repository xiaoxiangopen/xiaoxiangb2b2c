package net.xiaoxiangshop.security;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.exception.MobileSmsCodeException;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.event.UserLoggedInEvent;
import net.xiaoxiangshop.util.WebUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.repository.GenericDeclRepository;

/**
 * Security - 认证过滤器
 */
public class AuthenticationFilter extends FormAuthenticationFilter {


    /**
     * "重定向URL"参数名称
     */
    private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";

    /**
     * 用户类型
     */
    private Class<? extends User> userClass;

    @Inject
    private ApplicationEventPublisher applicationEventPublisher;
    @Inject
    private UserService userService;

    @Inject
    private MemberService memberService;
    @Inject
    private SmsService smsService;
    @Inject
    private AdminService adminService;
    @Inject
    private BusinessService businessService;
    /**
     * 创建令牌
     *
     * @param servletRequest  ServletRequest
     * @param servletResponse ServletResponse
     * @return 令牌
     */
    @Override
    protected org.apache.shiro.authc.AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String username = getUsername(servletRequest);
        String password = getPassword(servletRequest);
        boolean rememberMe = isRememberMe(servletRequest);
        String host = getHost(servletRequest);
        //-----------2020-10-23--- 新增手机短信登录逻辑
        String loginType=servletRequest.getParameter("loginType");
        //用户注册数据来源
        String dataSource=servletRequest.getParameter("dataSource");
        if(loginType!=null&&!"".equals(loginType)){//手机验证码登录
            String mobile=servletRequest.getParameter("mobile");
            String smsCode=servletRequest.getParameter("smsCode");
            /**判断是否为Member 初始化用户*/
            if(getUserClass() != null && Member.class.isAssignableFrom(getUserClass())){
                List<Member> memberList=memberService.memberList(mobile);
                //新号新增用户
                //查询到一条记录更新
                //查询到多条，返回到用户名密码登录界面
                if(memberList==null||memberList.size()==0){
                    Member member=new Member();
                    MemberRank memberRank = new MemberRank();
                    memberRank.setId(1L);
                    member.setMemberRank(memberRank);
                    member.setPoints(BigDecimal.ZERO);
                    member.setMobile(mobile);
                    //调用中台接口
                    memberService.synchroMember(member,mobile);
                    if("2".equals(dataSource)){
                        member.setDataSource(XmlUtils.DATA_SOURCE_TWO);
                    }else{
                        member.setDataSource(XmlUtils.DATA_SOURCE_ONE);
                    }
                    String ip=request.getRemoteAddr();
                    addUser(member,ip);
                }
//                if(memberList!=null&&memberList.size()==1){
//                    Member member=memberList.get(0);
//                    memberService.synchroMember(member,mobile);
//                    if(member.getMemberRank()==null){
//                        MemberRank memberRank = new MemberRank();
//                        memberRank.setId(1L);
//                        member.setMemberRank(memberRank);
//                    }
//                    if(member.getPoints()==null){
//                        member.setPoints(BigDecimal.ZERO);
//                    }
//                    memberService.update(member);
//                }
            }
             return new UserMobileAuthenticationToken(getUserClass(), mobile, smsCode,rememberMe, host);
        }else{
            return new UserAuthenticationToken(getUserClass(), username, password, rememberMe, host);
        }
    }


    private  void addUser(Member member,String ip){
        //新增会员信息
        member.setUsername(member.getMobile()+"_"+ XmlUtils.getRandom(2));
        //新增用户密码
        member.setPassword(XmlUtils.getRandom(8));
        member.setName(member.getMobile());
        member.setEmail(null);
        member.setPoint(0L);
        member.setBalance(BigDecimal.ZERO);
        member.setFrozenAmount(BigDecimal.ZERO);
        member.setAmount(BigDecimal.ZERO);
        member.setIsEnabled(true);
        member.setIsLocked(false);
        member.setLockDate(null);
        member.setLastLoginIp(ip);
        member.setLastLoginDate(new Date());
        member.setSafeKey(null);
        member.setDistributor(null);
        member.setCart(null);
        member.setOrders(null);
        member.setPaymentTransactions(null);
        member.setMemberDepositLogs(null);
        member.setReceivers(null);
        member.setReviews(null);
        member.setConsultations(null);
        member.setProductFavorites(null);
        member.setProductNotifies(null);
        member.setSocialUsers(null);
        member.setPointLogs(null);
        userService.register(member);
    }

    /**
     * 是否允许访问
     *
     * @param servletRequest  ServletRequest
     * @param servletResponse ServletResponse
     * @param mappedValue     映射值
     * @return 是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        Subject subject = getSubject(servletRequest, servletResponse);
        Object principal = subject != null ? subject.getPrincipal() : null;
        if (principal != null && !getUserClass().isAssignableFrom(principal.getClass())) {
            return false;
        }
        return super.isAccessAllowed(servletRequest, servletResponse, mappedValue);
    }

    /**
     * 拒绝访问处理
     *
     * @param servletRequest  ServletRequest
     * @param servletResponse ServletResponse
     * @return 是否继续处理
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            if (WebUtils.isAjaxRequest(request)) {
                Results.unauthorized(response, "common.message.unauthorized");
                return false;
            }
            saveRequest(request);
            String loginUrl = getLoginUrl();
            if (StringUtils.equalsIgnoreCase(request.getMethod(), "GET")) {
                String redirectUrl = request.getRequestURI() + (StringUtils.isNotEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "");
                loginUrl += StringUtils.contains(loginUrl, "?") ? "&" : "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, "UTF-8");
            }
            WebUtils.sendRedirect(request, response, loginUrl);
            return false;
        }
    }

    /**
     * 登录成功处理
     *
     * @param authenticationToken 令牌
     * @param subject             Subject
     * @param servletRequest      ServletRequest
     * @param servletResponse     ServletResponse
     * @return 是否继续处理
     */
    @Override
    protected boolean onLoginSuccess(org.apache.shiro.authc.AuthenticationToken authenticationToken, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
       String smsCode=request.getParameter("smsCode");
        User user = userService.getCurrent();
        Long id = user.getId();
        Member member = memberService.find(id);
        if (null != member) {
            String vipcard = member.getAttributeValue0();
            /**同步整合会员信息*/
            member=memberService.synchroMember(member,vipcard);
            if(member!=null){
                if(member.getMemberRank()==null){
                    MemberRank memberRank = new MemberRank();
                    memberRank.setId(1L);
                    member.setMemberRank(memberRank);
                }
                if(member.getPoints()==null){
                    member.setPoints(BigDecimal.ZERO);
                }
                member.setLastLoginIp(XmlUtils.getIp(request));
                member.setLastLoginDate(new Date());
                memberService.update(member);
            }
        }

        applicationEventPublisher.publishEvent(new UserLoggedInEvent(this, user));

        //        applicationEventPublisher.publishEvent(new UserLoggedInEvent(this, userService.getCurrent()));
        //&&smsCode!=null
        if (WebUtils.isAjaxRequest(request)) {
            Results.ok(response, Results.DEFAULT_OK_MESSAGE);
            return false;
        }
        return super.onLoginSuccess(authenticationToken, subject, servletRequest, servletResponse);
    }

    /**
     * 登录失败处理
     *
     * @param authenticationToken     令牌
     * @param authenticationException 认证异常
     * @param servletRequest          ServletRequest
     * @param servletResponse         ServletResponse
     * @return 是否继续处理
     */
    @Override
    protected boolean onLoginFailure(org.apache.shiro.authc.AuthenticationToken authenticationToken, AuthenticationException authenticationException, ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (WebUtils.isAjaxRequest(request)) {
            String errorMessage = null;
            if (authenticationException instanceof UnknownAccountException) {
                errorMessage = "common.login.unknownAccount";
            } else if (authenticationException instanceof LockedAccountException) {
                errorMessage = "common.login.lockedAccount";
            } else if (authenticationException instanceof DisabledAccountException) {
                errorMessage = "common.login.disabledAccount";
            } else if (authenticationException instanceof IncorrectCredentialsException) {
                errorMessage = "common.login.incorrectCredentials";
            } else if (authenticationException instanceof MobileSmsCodeException){//2020-10-24新增短信验证码错误消息异常
                errorMessage = "common.login.smsCodeCredentials";
            } else if (authenticationException instanceof AuthenticationException) {
                errorMessage = "common.login.incorrectCredentials";
            }
            if (errorMessage != null) {
                Results.unprocessableEntity(response, errorMessage);
            }
            return false;
        }
        return super.onLoginFailure(authenticationToken, authenticationException, servletRequest, servletResponse);
    }

    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public Class<? extends User> getUserClass() {
        return userClass;
    }

    /**
     * 设置用户类型
     *
     * @param userClass 用户类型
     */
    public void setUserClass(Class<? extends User> userClass) {
        this.userClass = userClass;
    }

}
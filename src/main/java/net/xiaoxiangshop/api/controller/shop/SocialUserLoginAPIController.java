package net.xiaoxiangshop.api.controller.shop;
import java.math.BigDecimal;
import java.util.*;

import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.wf.jwtp.exception.ExpiredTokenException;
import org.wf.jwtp.provider.Token;
import org.wf.jwtp.provider.TokenStore;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import me.chanjar.weixin.common.error.WxErrorException;
import net.xiaoxiangshop.CommonAttributes;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.plugin.LoginPlugin;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller - 社会化用户登录
 * 
 */
@RestController
@RequestMapping("/api/social_user")
public class SocialUserLoginAPIController {

	/**
	 * 小程序登录id
	 */
	private static final String loginPluginId = "weixinMiniLoginPlugin";
	
	private static final Logger _logger = LoggerFactory.getLogger(SocialUserLoginAPIController.class);

	@Inject
	private UserService userService;
	@Inject
	private SocialUserService socialUserService;
	@Inject
	private PluginService pluginService;
	@Inject
	private TokenStore tokenStore;
	private final WxMaServiceImpl wxMaService = new WxMaServiceImpl();
	private LoginPlugin loginPlugin;
	@Inject
	private MemberService memberService;

	@ModelAttribute
	public void populateModel() {
		loginPlugin = pluginService.getLoginPlugin(loginPluginId);
		// 获取小程序服务实例
		WxMaInMemoryConfig wxMaInMemoryConfig = new WxMaInMemoryConfig();
		wxMaInMemoryConfig.setAppid(loginPlugin.getAttribute("appId"));
		wxMaInMemoryConfig.setSecret(loginPlugin.getAttribute("appSecret"));
		wxMaService.setWxMaConfig(wxMaInMemoryConfig);
	}
	
	/**
	 * 登录接口
	 */
	@PostMapping("/login")
	public ApiResult signIn(String code, String signature, String rawData, String encryptedData, String ivStr, HttpServletRequest request) {
		_logger.info("用户请求登录获取Token");
		if (StringUtils.isEmpty(code)) {
			throw new ExpiredTokenException();
		}
		if (loginPlugin == null || BooleanUtils.isNotTrue(loginPlugin.getIsEnabled())) {
			return ResultUtils.badRequest("现在禁止登录!");
		}
		WxMaJscode2SessionResult codeResult = null;
		try {
			codeResult = wxMaService.jsCode2SessionInfo(code);
		} catch (WxErrorException e) {
			e.printStackTrace();
			_logger.info("error : " + e.getMessage());
		}
		_logger.info("codeResult : " + codeResult);

		Map<String, Object> data = new HashMap<>();
		WxMaUserService wxMaUserService = wxMaService.getUserService();
		String sessionKey = codeResult.getSessionKey();
		
		_logger.info("请求获取用户信息 : sessionKey = " + sessionKey + ", rawData = " + rawData + ", signature = " + signature);
        // 用户信息校验
        if (!wxMaUserService.checkUserInfo(sessionKey, rawData, signature)) {
            return ResultUtils.badRequest("验证用户信息完整性失败!");
        }
        // 解密用户信息
        WxMaUserInfo wxMaUserInfo = wxMaUserService.getUserInfo(sessionKey, encryptedData, ivStr);
		_logger.info("获取解密用户信息："+wxMaUserInfo);
		Member m1 = new Member();
//		wxMaUserInfo.setUnionId("ou-5K6EGRZewo7M3PS3CYNxVRxXk");
		//调用接口查询会员信息
		m1=memberService.synchroMember(m1,wxMaUserInfo.getUnionId());
		//根据手机号查询是否在平台注册过会员
		List<Member> memberList=new ArrayList<>();
		/*if(m1.getAttributeValue0()!=null&&m1.getMobile()!=null){
			 memberList=memberService.findHistoryUser(m1);
		}*/

		if(m1.getMobile()!=null){
			memberList=memberService.memberList(m1.getMobile());
		}
		//查询用户小程序登录
		SocialUser socialUser = socialUserService.find(loginPluginId, wxMaUserInfo.getUnionId());
		if(socialUser!=null&&socialUser.getUser()!=null){
			m1=memberService.find(socialUser.getUser().getId());
		}

		if(socialUser!=null&&socialUser.getUser()==null){
			if(memberList!=null&&memberList.size()>0){
				if(memberList.size()==1){
					//更新绑定关系
					m1=memberList.get(0);
				}else{
					//多条记录情况下
					Optional<Member> memberOptional=memberList.stream().max(Comparator.comparing(Member::getCreatedDate));
					m1=memberOptional.get();
				}
				socialUserService.bindUser(m1, socialUser, wxMaUserInfo.getUnionId());
			}else{
				//新增会员信息
				addUser(m1,wxMaUserInfo,request);
				socialUserService.bindUser(m1, socialUser, wxMaUserInfo.getUnionId());
			}
		}
		if(socialUser==null){
			if(memberList!=null&&memberList.size()>0){
				//新增三方登录
				SocialUser soc=new SocialUser();
				if(memberList.size()==1){
					m1=memberList.get(0);
				}else{
					//多条记录情况下
					Optional<Member> memberOptional=memberList.stream().max(Comparator.comparing(Member::getCreatedDate));
					m1=memberOptional.get();
				}
				addSocialUser(m1,wxMaUserInfo,soc);
				//更新绑定关系
				socialUserService.bindUser(m1,soc, wxMaUserInfo.getUnionId());
			}else{
				addUser(m1,wxMaUserInfo,request);
				SocialUser soc=new SocialUser();
				addSocialUser(m1,wxMaUserInfo,soc);
				//更新绑定关系
				socialUserService.bindUser(m1, soc, wxMaUserInfo.getUnionId());
			}

		}

		m1.setAttributeValue1(wxMaUserInfo.getOpenId());
		m1=memberService.synchroMember(m1,wxMaUserInfo.getUnionId());

		if(m1.getMemberRank()==null){
			MemberRank memberRank = new MemberRank();
			memberRank.setId(1L);
			m1.setMemberRank(memberRank);
		}
        if(m1.getPoints()==null){
			m1.setPoints(BigDecimal.ZERO);
		}

        //用户名==UNID并且手机号不等于空，从新给username赋值
        if(wxMaUserInfo.getUnionId().equals(m1.getUsername())&&m1.getMobile()!=null){
			m1.setUsername(m1.getMobile()+"_"+ XmlUtils.getRandom(2));
		}

		m1.setLastLoginIp(XmlUtils.getIp(request));
		m1.setLastLoginDate(new Date());

		//账户涉及到合并
		memberService.updateMembers(m1);

        Map<String, Object> item = new HashMap<>();
        item.put("id", String.valueOf(m1.getId()));
        item.put("nickName", wxMaUserInfo.getOpenId());
        item.put("avatarUrl", m1.getAvatarUrl());
        item.put("point", m1.getPoints());
		item.put("phone", m1.getMobile());
        item.put("open_id", wxMaUserInfo.getOpenId());

        // 签发token
		Token token = tokenStore.createNewToken(String.valueOf(m1.getId()), Member.MEMBER_PERMISSIONS, Member.ROLES, CommonAttributes.EXPIRE_TIME);
		data.put("token", "Bearer " + token.getAccessToken());
		data.put("member", item);
		data.put("session_key", sessionKey);
        return ResultUtils.ok(data);
	}

	private  void addSocialUser(Member m1,WxMaUserInfo wxMaUserInfo,SocialUser soc){
		//新增三方登录
		soc = new SocialUser();
		soc.setLoginPluginId(loginPluginId);
		soc.setUniqueId(wxMaUserInfo.getUnionId());
		soc.setUser(m1);
		socialUserService.save(soc);
	}

	private  void addUser(Member m1,WxMaUserInfo wxMaUserInfo,HttpServletRequest request){

		//新增会员信息
		m1.setUsername(wxMaUserInfo.getUnionId());
		//默认8位数密码
		m1.setPassword(XmlUtils.getRandom(8));
		m1.setName(repNickname(wxMaUserInfo.getNickName()));
		m1.setAttributeValue1(wxMaUserInfo.getOpenId());
		m1.setAddress(wxMaUserInfo.getCountry() + ", " + wxMaUserInfo.getProvince() + ", " + wxMaUserInfo.getCity());
		m1.setAvatarUrl(wxMaUserInfo.getAvatarUrl());
		m1.setEmail(null);
		if ("1".equals(wxMaUserInfo.getGender())){
			m1.setGender(Member.Gender.MALE);
		}
		else{
			m1.setGender(Member.Gender.FEMALE);
		}
		m1.setPoint(0L);
		m1.setBalance(BigDecimal.ZERO);
		m1.setFrozenAmount(BigDecimal.ZERO);
		m1.setAmount(BigDecimal.ZERO);
		m1.setIsEnabled(true);
		m1.setIsLocked(false);
		m1.setLockDate(null);
		m1.setLastLoginIp(XmlUtils.getIp(request));
		m1.setLastLoginDate(new Date());
		m1.setSafeKey(null);
		m1.setDistributor(null);
		m1.setCart(null);
		m1.setOrders(null);
		m1.setPaymentTransactions(null);
		m1.setMemberDepositLogs(null);
		m1.setReceivers(null);
		m1.setReviews(null);
		m1.setConsultations(null);
		m1.setProductFavorites(null);
		m1.setProductNotifies(null);
		m1.setSocialUsers(null);
		m1.setPointLogs(null);
		m1.setDataSource(XmlUtils.DATA_SOURCE_THREE);
		try {
			userService.register(m1);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//处理微信名特殊字符问题
	public String  repNickname(String nickname){
		if(XmlUtils.hasEmoji(nickname)){
			nickname=XmlUtils.replaceEmoji(nickname);
		}
		return nickname;
	}


}
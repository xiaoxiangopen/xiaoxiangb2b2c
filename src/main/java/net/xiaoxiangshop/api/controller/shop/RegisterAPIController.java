package net.xiaoxiangshop.api.controller.shop;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.service.DistributorService;
import net.xiaoxiangshop.service.MemberRankService;
import net.xiaoxiangshop.service.MemberService;
import net.xiaoxiangshop.service.UserService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * 会员 - 注册接口类
 */
@RestController
@RequestMapping("/api/register")
public class RegisterAPIController {

	@Inject
	private MemberService memberService;
	@Inject
	private UserService userService;
	@Inject
	private MemberRankService memberRankService;
	@Inject
	private DistributorService distributorService;

	/**
	 * 检查手机是否存在
	 */
	@GetMapping("/check_mobile")
	public ApiResult checkMobile(String mobile) {
		return ResultUtils.ok(StringUtils.isNotEmpty(mobile) && !memberService.mobileExists(mobile));
	}

	/**
	 * 注册提交
	 */
	@PostMapping("/submit")
	public ApiResult submit(String mobile, String password, String spreadMemberUsername) {
		Setting setting = SystemUtils.getSetting();
		if (!ArrayUtils.contains(setting.getAllowedRegisterTypes(), Setting.RegisterType.MEMBER)) {
			return ResultUtils.unprocessableEntity("member.register.disabled");
		}
		if (memberService.mobileExists(mobile)) {
			return ResultUtils.unprocessableEntity("member.register.mobileExist");
		}

		if (StringUtils.isEmpty(password)) {
			return ResultUtils.unprocessableEntity("member.login.passwordRequired");
		}

		Member member = new Member();

		member.setUsername(mobile);
		member.setPassword(password);
		member.setMobile(mobile);
		member.setPoint(0L);
		member.setBalance(BigDecimal.ZERO);
		member.setFrozenAmount(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		member.setLockDate(null);
		member.setLastLoginDate(new Date());
		member.setSafeKey(null);
		member.setMemberRank(memberRankService.findDefault());
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
		Member spreadMember = memberService.findByUsername(spreadMemberUsername);
		if (spreadMember != null) {
			distributorService.create(member, spreadMember);
		}
		return ResultUtils.ok();
	}

}

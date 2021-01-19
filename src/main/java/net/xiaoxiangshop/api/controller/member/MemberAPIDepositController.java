package net.xiaoxiangshop.api.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberDepositLog;
import net.xiaoxiangshop.plugin.PaymentPlugin;
import net.xiaoxiangshop.service.MemberDepositLogService;
import net.xiaoxiangshop.service.PluginService;
import net.xiaoxiangshop.service.UserService;
import net.xiaoxiangshop.util.WebUtils;

/**
 * 预存款 - 接口类
 */
@RestController("memberApiDepositController")
@RequestMapping("/api/member/member_deposit")
public class MemberAPIDepositController extends BaseAPIController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private PluginService pluginService;
	@Inject
	private UserService userService;

	/**
	 * 检查余额
	 */
	@PostMapping("/check_balance")
	public ApiResult checkBalance() {
		Member currentUser = userService.getCurrent(Member.class);
		Map<String, Object> data = new HashMap<>();
		data.put("balance", currentUser.getBalance());
		return ResultUtils.ok(data);
	}

	/**
	 * 充值
	 */
	@GetMapping("/recharge")
	public ApiResult recharge() {
		List<PaymentPlugin> paymentPlugins = pluginService.getActivePaymentPlugins(WebUtils.getRequest());
		Map<String, Object> data = new HashMap<>();
		if (!paymentPlugins.isEmpty()) {
			Map<String, Object> defaultPlugin = new HashMap<>();
			defaultPlugin.put("id", String.valueOf(paymentPlugins.get(0).getId()));
			defaultPlugin.put("displayName", paymentPlugins.get(0).getDisplayName());
			defaultPlugin.put("logo", paymentPlugins.get(0).getLogo());
			data.put("defaultPaymentPlugin", defaultPlugin);
			
			List<Map<String, Object>> paymentPluginsMap = new ArrayList<>();
			for (PaymentPlugin paymentPlugin : paymentPlugins) {
				Map<String, Object> item = new HashMap<>();
				item.put("id", String.valueOf(paymentPlugin.getId()));
				item.put("displayName", paymentPlugin.getDisplayName());
				item.put("logo", paymentPlugin.getLogo());
				paymentPluginsMap.add(item);
			}
			data.put("paymentPlugins", paymentPluginsMap);
		}
		return ResultUtils.ok(data);
	}


	/**
	 * 记录
	 */
	@GetMapping("/log")
	public ApiResult log(@RequestParam(name = "pageNumber", defaultValue = "1")Integer pageNumber) {
		Member currentUser = userService.getCurrent(Member.class);

		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<MemberDepositLog> memberDepositLogs = memberDepositLogService.findPage(currentUser, pageable);
		for (MemberDepositLog memberDepositLog : memberDepositLogs.getContent()) {
			Map<String, Object> item = new HashMap<>();
			String  sMg="";
			if(MemberDepositLog.Type.RECHARGE.equals(memberDepositLog.getType())){
				sMg="预存款充值";
			}else if(MemberDepositLog.Type.ADJUSTMENT.equals(memberDepositLog.getType())){
				sMg="预存款调整";
			}else if(MemberDepositLog.Type.ORDER_PAYMENT.equals(memberDepositLog.getType())){
				sMg="订单支付";
			}else if(MemberDepositLog.Type.ORDER_REFUNDS.equals(memberDepositLog.getType())){
				sMg="订单退款";
			}else if(MemberDepositLog.Type.DISTRIBUTION_COMMISSION.equals(memberDepositLog.getType())){
				sMg="分销提成";
			}else if(MemberDepositLog.Type.DISTRIBUTION_CASH.equals(memberDepositLog.getType())){
				sMg="分销提现";
			}else if(MemberDepositLog.Type.CHARGE_CARD.equals(memberDepositLog.getType())){
				sMg="充值卡充值";
			}else{
				sMg="";
			}
			item.put("type", sMg);
			item.put("credit", memberDepositLog.getCredit());
			item.put("debit", memberDepositLog.getDebit());
			item.put("balance", memberDepositLog.getBalance());
			item.put("memo", memberDepositLog.getMemo());
			item.put("createdDate",memberDepositLog.getCreatedDate());
			list.add(item);

		}
		data.put("list", list);
		return ResultUtils.ok(data);
	}

}
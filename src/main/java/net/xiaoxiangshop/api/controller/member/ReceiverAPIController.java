package net.xiaoxiangshop.api.controller.member;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.inject.Inject;
import com.alibaba.fastjson.JSONObject;
import net.xiaoxiangshop.ErpInterfaceMethod;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.WebUtils;
import net.xiaoxiangshop.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.security.CurrentUser;

/**
 * 收货地址 - 接口类
 */
@RestController("memberApiReceiverController")
@RequestMapping("/api/member/receiver")
public class ReceiverAPIController extends BaseAPIController {
	private static final Logger _logger = LoggerFactory.getLogger(ReceiverAPIController.class);
	@Inject
	private AreaService areaService;
	@Inject
	private ReceiverService receiverService;
	@Inject
	private MemberService memberService;
	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private ErpResultService erpResultService;

	/**
	 * 门店编码
	 */
	@Value("${default_store_no}")
	private String default_store_no;

	/**
	 * ERP中台接口URL
	 */
	@Value("${erp_basic_url}")
	private String erp_basic_url;
	/**
	 * 列表
	 */
	@GetMapping("/list")
	public ApiResult list(@CurrentUser Member currentUser) {
		Map<String, Object> data = new HashMap<>();
		Receiver defaultReceiver = receiverService.findDefault(currentUser);
		if(defaultReceiver!=null){
			data.put("defaultId", String.valueOf(defaultReceiver.getId()));
		}
		List<Map<String, Object>> list = new ArrayList<>();
		Page<Receiver> receivers = receiverService.findPage(currentUser, new Pageable());
		for (Receiver receiver : receivers.getContent()) {
			Map<String, Object> item = new HashMap<>();
			item.put("receiverId", String.valueOf(receiver.getId()));
			item.put("consignee", receiver.getConsignee());
			item.put("areaName", receiver.getAreaName());
			item.put("address", receiver.getAddress());
			item.put("phone", receiver.getPhone());
			list.add(item);
		}
		data.put("list", list);
		return ResultUtils.ok(data);
	}


	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public ApiResult edit(@RequestParam("receiverId") Long receiverId) {
		if (receiverId == null) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		Receiver pReceiver = receiverService.find(receiverId);
		Map<String, Object> data = new HashMap<>();
		
		Map<String, Object> receiver = new HashMap<>();
		receiver.put("receiverId", String.valueOf(pReceiver.getId()));
		receiver.put("consignee", pReceiver.getConsignee());
		//receiver.put("areaName", Arrays.asList(pReceiver.getAreaName()));
		receiver.put("address", pReceiver.getAddress());
		receiver.put("phone", pReceiver.getPhone());
		
		data.put("areaName", Arrays.asList(pReceiver.getAreaName()));
		data.put("receiver", receiver);
		return ResultUtils.ok(data);
	}
	
	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ApiResult save(@CurrentUser Member currentUser, Receiver receiver) {

		String  ps=receiver.getAreaName().split(",")[1]+receiver.getAreaName().split(",")[2];
		receiver.setArea(areaService.findByLikeName(ps));
		receiver.setIsDefault(true);
		if (!isValid(receiver)) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		if (Receiver.MAX_RECEIVER_COUNT != null && currentUser.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return ResultUtils.unprocessableEntity("member.receiver.addCountNotAllowed", Receiver.MAX_RECEIVER_COUNT);
		}
		receiver.setAreaName(null);
		receiver.setMember(currentUser);
		receiverService.save(receiver);
		return ResultUtils.ok();
	}


	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ApiResult update(@CurrentUser Member currentUser, Receiver receiverForm) {
		boolean status = receiverForm.getAreaName().contains(",");
		if(status){
			String  ps=receiverForm.getAreaName().split(",")[1]+receiverForm.getAreaName().split(",")[2];
			receiverForm.setArea(areaService.findByLikeName(ps));
		}else{
			receiverForm.setArea(areaService.findByLikeName(receiverForm.getAreaName()));
		}
		receiverForm.setMember(currentUser);
		Receiver   receiver=receiverService.find(receiverForm.getId());
		receiverForm.setIsDefault(receiver.getIsDefault());
		if (!isValid(receiverForm)) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		receiverService.update(receiverForm);
		return ResultUtils.ok();
	}

	/**
	 * 设置默认
	 */
	@PostMapping("/update_default")
	public ApiResult updateDefault(@RequestParam("receiverId") Long receiverId) {
		Receiver receiver = receiverService.find(receiverId);
		if (receiver == null) {
			return ResultUtils.NOT_FOUND;
		}
		receiver.setIsDefault(true);
		receiverService.update(receiver);
		return ResultUtils.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ApiResult delete(@RequestParam("receiverId") Long receiverId) {
		Receiver receiver = receiverService.find(receiverId);
		if (receiver == null) {
			return ResultUtils.NOT_FOUND;
		}

		receiverService.delete(receiver);
		return ResultUtils.ok();
	}



	/**
	 * 会员充值
	 */
	@PostMapping("/recharge")
	public ApiResult recharge(Recharge recharge,@CurrentUser Member member) {

		RechargeRequestBean rechargeRequestBean = new RechargeRequestBean();
		rechargeRequestBean.setMethod(ErpInterfaceMethod.CARD_INVEST);
		rechargeRequestBean.setOrgid(default_store_no);
		rechargeRequestBean.setJe("0");

		rechargeRequestBean.setCardid(recharge.getCardid());
		rechargeRequestBean.setPwd(recharge.getPwd());
		String msg="充值失败,请您稍后再试.";
		ApiResult  apiResult=new ApiResult();
		try {
			_logger.info("发送请求信息："+JSONObject.toJSONString(rechargeRequestBean));
			String retV = WebUtils.sendPost(erp_basic_url, JSONObject.toJSONString(rechargeRequestBean));
			JSONObject obj = JSONObject.parseObject(retV);
			_logger.info("获取充值信息："+obj);
			Integer code = obj.getInteger("sub_code");
			if (code == 0) {
				JSONObject obj_data = obj.getJSONObject("data");
				String money = obj_data.getString("czje");
				BigDecimal czje =new BigDecimal(money);

				BigDecimal amount=member.getBalance().add(czje);
				member.setBalance(amount);
				DecimalFormat df1 = new DecimalFormat("0.00");
				memberService.update(member);
				//保存充值记录
				BigDecimal zero = new BigDecimal("0");
				MemberDepositLog log=new MemberDepositLog();
				log.setBalance(amount);
				log.setCredit(czje);
				log.setDebit(zero);
				log.setCardNo(recharge.getCardid());
				log.setMember(member);
				log.setMemo("充值卡充值"+recharge.getCardid());
				log.setType(MemberDepositLog.Type.CHARGE_CARD);
				memberDepositLogService.save(log);
				msg="充值成功,请您留意账户变化.";
				apiResult.setStatus(HttpStatus.CONTINUE);
				_logger.info("充值成功");
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HashMap hashMap = new HashMap();
			hashMap.put("erpType", "MemberDepositController.deposit");
			hashMap.put("sendTime", simpleDateFormat.format(new Date()));
			hashMap.put("sendText", JSONObject.toJSONString(rechargeRequestBean));
			hashMap.put("type", "4");
			hashMap.put("resultTime", simpleDateFormat.format(new Date()));
			hashMap.put("resultText", retV);
			hashMap.put("resultCode", code);
			hashMap.put("sn",recharge.getCardid());
			erpResultService.add(hashMap);
		} catch (Exception e) {
			_logger.info("充值抛出失败异常");
			e.printStackTrace();
		}
		apiResult.setMessage(msg);
		return apiResult;
	}

}
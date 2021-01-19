package net.xiaoxiangshop.api.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.PointLog;
import net.xiaoxiangshop.service.PointLogService;
import net.xiaoxiangshop.service.UserService;

/**
 * 我的积分 - 接口类
 */
@RestController("memberApiPointLogController")
@RequestMapping("/api/member/point_log")
public class PointLogAPIController extends BaseAPIController {

	/**
	 * 每页记录数
	 */
	private static final int PAGE_SIZE = 10;

	@Inject
	private PointLogService pointLogService;
	@Inject
	private UserService userService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public ApiResult list(@RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber) {
		Member currentUser = userService.getCurrent(Member.class);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		
		List<Map<String, Object>> data = new ArrayList<>();
		Page<PointLog> pointLogs = pointLogService.findPage(currentUser, pageable);
		for (PointLog pointLog : pointLogs.getContent()) {
			Map<String, Object> item = new HashMap<>();
			item.put("type", pointLog.getType());
			item.put("credit", pointLog.getCredit());
			item.put("debit", pointLog.getDebit());
			item.put("balance", pointLog.getBalance());
			item.put("memo", pointLog.getMemo());
			data.add(item);
		}
		return ResultUtils.ok(data);
	}

}
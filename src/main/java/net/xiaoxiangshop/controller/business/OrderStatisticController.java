package net.xiaoxiangshop.controller.business;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.xiaoxiangshop.service.OrderStatisticService;
import net.xiaoxiangshop.util.DatesUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Statistic;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.StatisticService;

/**
 * Controller - 订单统计
 * 
 */
@Controller("businessOrderStatisticController")
@RequestMapping("/business/order_statistic")
public class OrderStatisticController extends BaseController {

	/**
	 * 默认类型
	 */
	private static final Statistic.Type DEFAULT_TYPE = Statistic.Type.CREATE_ORDER_COUNT;

	/**
	 * 默认周期
	 */
	private static final Statistic.Period DEFAULT_PERIOD = Statistic.Period.DAY;

	@Inject
	private StatisticService statisticService;
	@Inject
	private OrderStatisticService orderStatisticService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Model model) {
//		model.addAttribute("types", Statistic.Type.getTypes(Statistic.Group.BUSINESS_ORDER));
//		model.addAttribute("type", DEFAULT_TYPE);
//		model.addAttribute("periods", Statistic.Period.values());
//		model.addAttribute("period", DEFAULT_PERIOD);
//		model.addAttribute("beginDate", DateUtils.addMonths(new Date(), -1));
//		model.addAttribute("endDate", new Date());
		return "business/order_statistic/list";
	}

	/**
	 * 数据
	 */
	@GetMapping("/data")
	public ResponseEntity<?> data(Statistic.Type type, Statistic.Period period, Date beginDate, Date endDate, @CurrentStore Store currentStore) {
		if (type == null) {
			type = DEFAULT_TYPE;
		}
		if (period == null) {
			period = DEFAULT_PERIOD;
		}

		List<Statistic.Type> types = Statistic.Type.getTypes(Statistic.Group.BUSINESS_ORDER);
		if (!types.contains(type)) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		Date now = new Date();
		if (beginDate == null) {
			switch (period) {
			case YEAR:
				beginDate = DateUtils.addYears(now, -10);
				break;
			case MONTH:
				beginDate = DateUtils.addYears(now, -1);
				break;
			case DAY:
				beginDate = DateUtils.addMonths(now, -1);
			}
		}
		if (endDate == null) {
			endDate = now;
		}
		switch (period) {
		case YEAR:
			beginDate = DateUtils.truncate(beginDate, Calendar.YEAR);
			Date nextYearMinumumDate = DateUtils.ceiling(endDate, Calendar.YEAR);
			endDate = DateUtils.addMilliseconds(nextYearMinumumDate, -1);
			break;
		case MONTH:
			beginDate = DateUtils.truncate(beginDate, Calendar.MONTH);
			Date nextMonthMinumumDate = DateUtils.ceiling(endDate, Calendar.MONTH);
			endDate = DateUtils.addMilliseconds(nextMonthMinumumDate, -1);
			break;
		case DAY:
			beginDate = DateUtils.truncate(beginDate, Calendar.DAY_OF_MONTH);
			Date tomorrowMinumumDate = DateUtils.ceiling(endDate, Calendar.DAY_OF_MONTH);
			endDate = DateUtils.addMilliseconds(tomorrowMinumumDate, -1);
		}
		return ResponseEntity.ok(statisticService.analyze(type, currentStore, period, beginDate, endDate));
	}



	/**
	 * 数据
	 */
	@GetMapping("/orderData")
	public ResponseEntity<?> orderData(Date beginDate, Date endDate,@CurrentStore Store currentStore) {
		if (beginDate == null) {
			if (endDate == null||DatesUtil.compareToDate(endDate, new Date()) != -1) {//结束时间为空 开始时间为昨天及之前的30天 或大于当前时间 按照当前时间计算
				endDate= DatesUtil.addDate(new Date(), -1);
				beginDate = DatesUtil.addDate(new Date(), -30);
			} else {
				if (DatesUtil.compareToDate(endDate, new Date()) == -1) {//小于当前时间
					beginDate = DatesUtil.addDate(endDate, -29);
				}
			}
		}
		Map<String,Object> reslut= orderStatisticService.analyze(currentStore,beginDate,endDate);
		return ResponseEntity.ok(reslut);
	}

	/**
	 *按照订单来源统计
	 * @param source  订单来源
	 * @param beginDate
	 * @param endDate
	 * @param currentStore
	 * @return
	 */
	@GetMapping("/orderSourceData")
	public ResponseEntity<?> orderSourceData(Integer source,Date beginDate, Date endDate,@CurrentStore Store currentStore) {
		if (beginDate == null) {
			if (endDate == null||DatesUtil.compareToDate(endDate, new Date()) != -1) {//结束时间为空 开始时间为昨天及之前的30天 或大于当前时间 按照当前时间计算
				endDate= DatesUtil.addDate(new Date(), -1);
				beginDate = DatesUtil.addDate(new Date(), -30);
			} else {
				if (DatesUtil.compareToDate(endDate, new Date()) == -1) {//小于当前时间
					beginDate = DatesUtil.addDate(endDate, -29);
				}
			}
		}
		Map<String,Object> reslut= orderStatisticService.orderSourceDataAnalyze(currentStore,source,beginDate,endDate);
		return ResponseEntity.ok(reslut);
	}
}
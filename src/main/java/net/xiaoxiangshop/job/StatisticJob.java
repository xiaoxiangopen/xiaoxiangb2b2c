package net.xiaoxiangshop.job;

import java.util.Calendar;

import javax.inject.Inject;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.xiaoxiangshop.service.StatisticService;

/**
 * Job - 统计
 * 
 */
@Lazy(false)
@Component
@EnableScheduling
public class StatisticJob {

	@Inject
	private StatisticService statisticService;

	/**
	 * 收集
	 */
	@Scheduled(cron = "${job.statistic_collect.cron}")
	public void collect() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		statisticService.collect(year, month, day);
	}
}
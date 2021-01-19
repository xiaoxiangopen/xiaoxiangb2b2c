package net.xiaoxiangshop.job;

import javax.inject.Inject;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.xiaoxiangshop.service.StoreService;

/**
 * Job - 店铺
 * 
 */
@Lazy(false)
@Component
@EnableScheduling
public class StoreJob {

	@Inject
	private StoreService storeService;

	/**
	 * 过期店铺处理
	 */
	@Scheduled(cron = "${job.store_expired_processing.cron}")
	public void evictExpired() {
//		storeService.expiredStoreProcessing();
	}

}
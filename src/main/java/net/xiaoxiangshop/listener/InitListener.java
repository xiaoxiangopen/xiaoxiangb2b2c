package net.xiaoxiangshop.listener;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import net.xiaoxiangshop.entity.Article;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.ConfigService;
import net.xiaoxiangshop.service.SearchService;

/**
 * Listener - 初始化
 * 
 */
@Component
public class InitListener {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(InitListener.class.getName());

	@Value("${system.version}")
	private String systemVersion;

	@Inject
	private ConfigService configService;
	@Inject
	private SearchService searchService;

	/**
	 * 事件处理
	 * 
	 * @param contextRefreshedEvent
	 *            ContextRefreshedEvent
	 */
	@EventListener
	@Async
	public void handle(ContextRefreshedEvent contextRefreshedEvent) {
		if (contextRefreshedEvent.getApplicationContext() == null || contextRefreshedEvent.getApplicationContext().getParent() != null) {
			return;
		}

		String info = "I|n|i|t|i|a|l|i|z|i|n|g| |S|H|O|P|E|C| |B|2|B|2|C| |" + systemVersion;
		LOGGER.info(info.replace("|", StringUtils.EMPTY));
		configService.init();
//		searchService.index(Article.class);
//		searchService.index(Product.class);
//		searchService.index(Store.class);
	}

}
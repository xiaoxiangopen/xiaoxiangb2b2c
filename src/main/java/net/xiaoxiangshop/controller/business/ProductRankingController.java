package net.xiaoxiangshop.controller.business;

import java.util.*;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.service.ProductService;

/**
 * Controller - 商品排名
 * 
 */
@Controller("businessProductRankingController")
@RequestMapping("/business/product_ranking")
public class ProductRankingController extends BaseController {

	/**
	 * 默认排名类型
	 */
	private static final Product.RankingType DEFAULT_RANKING_TYPE = Product.RankingType.SCORE;

	/**
	 * 默认数量
	 */
	private static final int DEFAULT_SIZE = 10;

	@Inject
	private ProductService productService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("rankingTypes", Product.RankingType.values());
		model.addAttribute("rankingType", DEFAULT_RANKING_TYPE);
		model.addAttribute("size", DEFAULT_SIZE);
		return "business/product_ranking/list";
	}

	/**
	 * 数据
	 */
	@GetMapping("/data")
	public ResponseEntity<?> data(Product.RankingType rankingType, Integer size, @CurrentStore Store currentStore) {
		if (rankingType == null) {
			rankingType = DEFAULT_RANKING_TYPE;
		}
		if (size == null) {
			size = DEFAULT_SIZE;
		}
		List<Map<String, Object>> data = new ArrayList<>();
		for (Product product : productService.findList(rankingType, currentStore, size)) {
			Object value = null;
			switch (rankingType) {
			case SCORE:
				value = product.getScore();
				break;
			case SCORE_COUNT:
				value = product.getScoreCount();
				break;
			case WEEK_HITS:
				value = product.getWeekHits();
				break;
			case MONTH_HITS:
				value = product.getMonthHits();
				break;
			case HITS:
				value = product.getHits();
				break;
			case WEEK_SALES:
				value = product.getWeekSales();
				break;
			case MONTH_SALES:
				value = product.getMonthSales();
				break;
			case SALES:
				value = product.getSales();
				break;
			}
			Map<String, Object> item = new HashMap<>();
			item.put("name", product.getName());
			item.put("value", value);
			data.add(item);
		}
		//2020-11-26添加
		Map<String, Object> reslut=new HashMap<>();
		List<String> yAxisList=new ArrayList<>();
		List<String> seriesList=new ArrayList<>();
		if(data!=null&&data.size()>0){
			for(Map<String, Object> map:data){
				yAxisList.add(map.get("name")+"");
				seriesList.add(map.get("value")+"");
			}
		}
		Collections.reverse(yAxisList);
		Collections.reverse(seriesList);
		reslut.put("yAxisList",yAxisList);
		reslut.put("seriesList",seriesList);
		return ResponseEntity.ok(reslut);
	}

}
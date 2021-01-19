package net.xiaoxiangshop.api.controller.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;

/**
 * Controller - 帮助
 * 
 */
@RestController("helpApiCouponCodeController")
@RequestMapping("/api/help")
public class HelpAPIController {

	private String systemName = "小象电商";
	
	private String systemDescription = "小象电商是国有大型综合性商业公司“小象电商集团有限公司”旗下的唯一官方购物网站。是小象电商特别为尊贵的顾客打造的一个一实体店为依托的在线购物消费平台。小象电商——数百个品牌专柜、逾万款商品，囊括化妆品、家电、数码、服饰、钟表、珠宝、超市百货等18种百货商品类别，设有7种支付方式，并提供市内货到付款、省内满额免邮费、网上预售、积分卡充值线上消费等贴心服务。务求让您，超越时空，乐在其中。　　我们将竭力为顾客您提供无微不至、独一无二的网络购物体验，让您足不出户也能够体验尊贵，共享小象！小象电商，超越时空，乐在其中！小象电商定期推出惊喜单品、心水专题。更多精彩体验请关注“小象电商”公众号。";
	
	/**
	 * 首页
	 */
	@GetMapping
	public ApiResult index() {
		List<Map<String, Object>> helpItems = new ArrayList<>();
		
		Map<String, Object> item1 = new HashMap<>();
		item1.put("title", systemName);
		item1.put("content", systemDescription);
		helpItems.add(item1);
		
		Map<String, Object> item2 = new HashMap<>();
		item2.put("title", "客服电话");
		item2.put("content", "020－00000000");
		helpItems.add(item2);
		
		Map<String, Object> data = new HashMap<>();
		data.put("list", helpItems);
		
		return ResultUtils.ok(data);
	}
}

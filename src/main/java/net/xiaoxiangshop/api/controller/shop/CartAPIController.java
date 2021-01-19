package net.xiaoxiangshop.api.controller.shop;

import java.util.*;

import javax.inject.Inject;

import net.xiaoxiangshop.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.Cart;
import net.xiaoxiangshop.entity.CartItem;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.security.CurrentCart;
import net.xiaoxiangshop.service.CartService;
import net.xiaoxiangshop.service.SkuService;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * 购物车 - 接口类
 */
@RestController
@RequestMapping("/api/cart")
public class CartAPIController {

	@Inject
	private SkuService skuService;
	@Inject
	private CartService cartService;
	@Inject
	private CartItemService cartItemService;



	/**
	 * 获取当前购物车
	 */
	@GetMapping("/get_current")
	public ApiResult getCurrent(@CurrentCart Cart currentCart) {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> cartItems = new ArrayList<>();
		Setting setting = SystemUtils.getSetting();
		if (currentCart != null && !currentCart.isEmpty()) {

			Set<CartItem>  cartItemSet=currentCart.getCartItems();
			Set<CartItem>  set=new HashSet<CartItem>();
			for (CartItem c:cartItemSet){
				if (c.getIsBuy()==1){
					set.add(c);
				}
			}
			//计算被选中的价格
			Cart currentCart1=new Cart();
			currentCart1.setCartItems(set);

			data.put("quantity", currentCart.getQuantity(false));
			data.put("rewardPoint", currentCart.getRewardPoint());
			data.put("effectivePrice", currentCart1.getEffectivePrice());
			data.put("discount", currentCart.getDiscount());
			data.put("id", String.valueOf(currentCart.getId()));
			for (CartItem cartItem : currentCart) {
				Map<String, Object> item = new HashMap<>();
				Sku sku = cartItem.getSku();
				Product product = sku.getProduct();
				item.put("id", String.valueOf(cartItem.getId()));
				item.put("productId", String.valueOf(product.getId()));
				item.put("cartId", String.valueOf(cartItem.getCart().getId()));
				item.put("skuId", String.valueOf(sku.getId()));
				item.put("skuName", sku.getName());
				item.put("skuThumbnail", sku.getThumbnail() != null ? setting.getSiteUrl() + sku.getThumbnail() : setting.getSiteUrl() + setting.getDefaultThumbnailProductImage());
				item.put("price", cartItem.getPrice());
				item.put("specifications", sku.getSpecifications());
				item.put("quantity", cartItem.getQuantity());
				item.put("subtotal", cartItem.getSubtotal());
				item.put("isBuy", cartItem.getIsBuy()==1?true:false);
				cartItems.add(item);
			}
		}
		data.put("cartItems", cartItems);
		return ResultUtils.ok(data);
	}

	/**
	 * 添加
	 */
	@PostMapping("/add")
	public ApiResult add(Long skuId, Integer quantity, @CurrentCart Cart currentCart) {
		Map<String, Object> data = new HashMap<>();
		if (quantity == null || quantity < 1) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		Sku sku = skuService.find(skuId);
		if (sku == null) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotExist");
		}
		if (!Product.Type.GENERAL.equals(sku.getType())) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotForSale");
		}
		if (!sku.getIsActive()) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotActive");
		}
		if (!sku.getIsMarketable()) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotMarketable");
		}
		if (sku.getProduct().getStore().hasExpired()) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotBuyExpired");
		}

		int cartItemSize = 1;
		int skuQuantity = quantity;
		if (currentCart != null) {
			if (currentCart.contains(sku)) {
				CartItem cartItem = currentCart.getCartItem(sku);
				cartItemSize = currentCart.size();
				skuQuantity = cartItem.getQuantity() + quantity;
			} else {
				cartItemSize = currentCart.size() + 1;
				skuQuantity = quantity;
			}
		}
		if (Cart.MAX_CART_ITEM_SIZE != null && cartItemSize > Cart.MAX_CART_ITEM_SIZE) {
			return ResultUtils.unprocessableEntity("shop.cart.addCartItemSizeNotAllowed", Cart.MAX_CART_ITEM_SIZE);
		}
		if (CartItem.MAX_QUANTITY != null && skuQuantity > CartItem.MAX_QUANTITY) {
			return ResultUtils.unprocessableEntity("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY);
		}
		if (skuQuantity > sku.getAvailableStock()) {
			return ResultUtils.unprocessableEntity("shop.cart.skuLowStock");
		}
		if (currentCart == null) {
			currentCart = cartService.create();
		}
		cartService.add(currentCart, sku, quantity);
		//总选
		data.put("zongshu", currentCart.getQuantity(false));
		data.put("quantity", 1);
		data.put("rewardPoint", currentCart.getRewardPoint());
		data.put("effectivePrice", currentCart.getEffectivePrice());
		data.put("discount", currentCart.getDiscount());
		data.put("cartKey", currentCart.getKey());
		return ResultUtils.ok(data);
	}

	/**
	 * 修改
	 */
	@PostMapping("/modify")
	public ApiResult modify(Long skuId, Integer quantity, @CurrentCart Cart currentCart) {
		Map<String, Object> data = new HashMap<>();
		if (quantity == null || quantity < 1) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		Sku sku = skuService.find(skuId);
		if (sku == null) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotExist");
		}
		if (currentCart == null || currentCart.isEmpty()) {
			return ResultUtils.unprocessableEntity("shop.cart.notEmpty");
		}
		if (!currentCart.contains(sku)) {
			return ResultUtils.unprocessableEntity("shop.cart.cartItemNotExist");
		}
		if (!sku.getIsActive()) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotActive");
		}
		if (!sku.getIsMarketable()) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotMarketable");
		}
		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			return ResultUtils.unprocessableEntity("shop.cart.addQuantityNotAllowed", CartItem.MAX_QUANTITY);
		}
		if (quantity > sku.getAvailableStock()) {
			return ResultUtils.unprocessableEntity("shop.cart.skuLowStock");
		}

		Set<CartItem>  cartItemSet=currentCart.getCartItems();
		Set<CartItem>  set=new HashSet<CartItem>();
		for (CartItem c:cartItemSet){
			if (c.getIsBuy()==1){
				set.add(c);
			}
		}
		//计算被选中的价格
		Cart currentCart1=new Cart();
		currentCart1.setCartItems(set);

		cartService.modify(currentCart, sku, quantity);

		data.put("quantity", currentCart.getQuantity(false));
		data.put("rewardPoint", currentCart.getRewardPoint());
		data.put("effectivePrice", currentCart1.getEffectivePrice());
		data.put("discount", currentCart.getDiscount());

		Map<String, Object> cartItemMap = new HashMap<>();
		CartItem cartItem = currentCart.getCartItem(sku);
		cartItemMap.put("subtotal", cartItem.getSubtotal());
		cartItemMap.put("isLowStock", cartItem.getIsLowStock());
		data.put("cartItem", cartItemMap);

		Store store = sku.getStore();
		Map<String, Object> storeMap = new HashMap<>();
		storeMap.put("giftNames", currentCart.getGiftNames(store));
		storeMap.put("promotionNames", currentCart.getPromotionNames(store));
		data.put("store", storeMap);

		return ResultUtils.ok(data);
	}
	
	/**
	 * 移除
	 */
	@PostMapping("/remove")
	public ApiResult remove(Long skuId, @CurrentCart Cart currentCart) {
		Map<String, Object> data = new HashMap<>();
		Sku sku = skuService.find(skuId);

		Set<CartItem>  cartItemSet=currentCart.getCartItems();
		Set<CartItem>  set=new HashSet<CartItem>();
		for (CartItem c:cartItemSet){
			if (c.getIsBuy()==1){
				set.add(c);
			}
		}
		//计算被选中的价格
		Cart currentCart1=new Cart();
		currentCart1.setCartItems(set);

		if (sku == null) {
			return ResultUtils.unprocessableEntity("shop.cart.skuNotExist");
		}
		if (currentCart == null || currentCart.isEmpty()) {
			return ResultUtils.unprocessableEntity("shop.cart.notEmpty");
		}
		if (!currentCart.contains(sku)) {
			return ResultUtils.unprocessableEntity("shop.cart.cartItemNotExist");
		}
		Store store = sku.getProduct().getStore();
		cartService.remove(currentCart, sku);

		data.put("quantity", currentCart.getQuantity(false));
		data.put("rewardPoint", currentCart.getRewardPoint());
		data.put("effectivePrice", currentCart1.getEffectivePrice());
		data.put("discount", currentCart.getDiscount());

		Map<String, Object> storeMap = new HashMap<>();
		storeMap.put("giftNames", currentCart.getGiftNames(store));
		storeMap.put("promotionNames", currentCart.getPromotionNames(store));
		data.put("store", storeMap);
		return ResultUtils.ok(data);
	}
	
	/**
	 * 清空
	 */
	@PostMapping("/clear")
	public ApiResult clear(@CurrentCart Cart currentCart) {
		if (currentCart != null) {
			cartService.clear(currentCart);
		}
		return ResultUtils.ok();
	}



	/**
	 * 复选
	 */

	@GetMapping("/updateBuy")
	public ApiResult updateIsBuy(String cartItemId,String cartId) {

		if(cartItemId!=""){
				String [] cartItemIds=cartItemId.split(",");
				//查询出所有购物车明细
				Set<CartItem>  itemList =cartItemService.fingItemByCartId(Long.valueOf(cartId));

				StringBuffer  stringBuffer=new StringBuffer();
				 for(CartItem cart:itemList){
					 stringBuffer.append(cart.getId()).append(",");
				 }

				 String [] cartIds=stringBuffer.toString().split(",");
				 //得不到不相同的部分
			     List<String> arrayList = compare(cartItemIds,cartIds);


				for(String s:cartItemIds){
					if(s!=""){
						this.init(Long.valueOf(s),1);
					}

				}
				if(arrayList.size()>0){
					for (String s:arrayList){
						if(s!=""){
							this.init(Long.valueOf(s),0);
						}
					}
				}
		}else{
			Set<CartItem>  itemList =cartItemService.fingItemByCartId(Long.valueOf(cartId));
			if(itemList!=null&&itemList.size()>0){
				for(CartItem cartItem:itemList){
					if (cartItem!=null){
						this.init(cartItem.getId(),0);
					}
				}
			}

		}

		return ResultUtils.ok();
	}

	public   List<String> compare(String[] t1, String[] t2) {
		List<String> list1 = Arrays.asList(t1);
		List<String> list2 = new ArrayList<String>();
		for (String t : t2) {
			if (!list1.contains(t)) {
				list2.add(t);
			}
		}
		return list2;
	}


	private void  init(Long itemId,int isBuy){
		CartItem  ct=cartItemService.find(itemId);
		CartItem  cartItem1 =new CartItem();
		cartItem1.setId(ct.getId());
		cartItem1.setIsBuy(isBuy);
		cartItem1.setVersion(ct.getVersion());
		cartItem1.setLastModifiedDate(new Date());
		cartItem1.setQuantity(null);
		cartItemService.update(cartItem1);
	}


}

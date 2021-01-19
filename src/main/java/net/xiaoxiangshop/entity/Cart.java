package net.xiaoxiangshop.entity;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.util.Assert;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.util.JsonUtils;
import net.xiaoxiangshop.util.SpringUtils;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Entity - 购物车
 *
 */
@Entity
public class Cart extends BaseEntity<Cart> implements Iterable<CartItem> {

	private static final long serialVersionUID = -6565967051825794561L;

	/**
	 * 超时时间
	 */
	public static final int TIMEOUT = 604800;

	/**
	 * 最大购物车项数量
	 */
	public static final Integer MAX_CART_ITEM_SIZE = 100;

	/**
	 * "密钥"Cookie名称
	 */
	public static final String KEY_COOKIE_NAME = "cartKey";

	/**
	 * "标签"Cookie名称
	 */
	public static final String TAG_COOKIE_NAME = "cartTag";

	/**
	 * 密钥
	 */
	@TableField(value = "cart_key")
	private String key;

	/**
	 * 过期时间
	 */
	private Date expire;

	/**
	 * 会员
	 */
	@JsonIgnore
	@TableField(exist = false)
	private Member member;

	/**
	 * 购物车项
	 */
	@JsonIgnore
	@TableField(exist = false)
	private Set<CartItem> cartItems = new HashSet<>();

	/**
	 * 获取密钥
	 *
	 * @return 密钥
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 设置密钥
	 *
	 * @param key
	 *            密钥
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取过期时间
	 *
	 * @return 过期时间
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * 设置过期时间
	 *
	 * @param expire
	 *            过期时间
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}

	/**
	 * 获取会员
	 *
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 *
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取购物车项
	 *
	 * @return 购物车项
	 */
	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	/**
	 * 设置购物车项
	 *
	 * @param cartItems
	 *            购物车项
	 */
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	/**
	 * 获取购物车项
	 *
	 * @param sku
	 *            SKU
	 * @return 购物车项
	 */
	@Transient
	public CartItem getCartItem(final Sku sku) {
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");

		return (CartItem) CollectionUtils.find(getCartItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && cartItem.getSku() != null && cartItem.getSku().equals(sku);
			}
		});
	}

	/**
	 * 获取购物车项
	 *
	 * @param store
	 *            店铺
	 * @return 购物车项
	 */
	@SuppressWarnings("unchecked")
	@Transient
	public Set<CartItem> getCartItems(final Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		return new HashSet<>(CollectionUtils.select(getCartItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && cartItem.getStore() != null && cartItem.getStore().equals(store);
			}
		}));
	}

	/**
	 * 获取购物车项组
	 *
	 * @return 购物车项组
	 */
	@JsonIgnore
	@Transient
	public Map<Store, Set<CartItem>> getCartItemGroup() {
		Map<Store, Set<CartItem>> cartItemGroup = new HashMap<>();
		for (Store store : getStores()) {
			cartItemGroup.put(store, getCartItems(store));
		}
		return cartItemGroup;
	}

	/**
	 * 获取店铺
	 *
	 * @return 店铺
	 */
	@JsonIgnore
	@Transient
	public Set<Store> getStores() {
		Set<Store> stores = new HashSet<>();
		if (getCartItems() != null) {
			for (CartItem cartItem : getCartItems()) {
				if (cartItem != null && cartItem.getStore() != null) {
					stores.add(cartItem.getStore());
				}
			}
		}
		return stores;
	}

	/**
	 * 判断是否包含SKU
	 *
	 * @param sku
	 *            SKU
	 * @return 是否包含SKU
	 */
	@Transient
	public boolean contains(Sku sku) {
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");

		return getCartItem(sku) != null;
	}

	/**
	 * 判断是否包含店铺类型
	 *
	 * @return 是否包含店铺类型
	 */
	@Transient
	public boolean contains(final Store.Type type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		return CollectionUtils.exists(getStores(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Store store = (Store) object;
				return store != null && store.getType() != null && store.getType().equals(type);
			}
		});
	}

	/**
	 * 获取重量
	 *
	 * @param store
	 *            店铺
	 * @param includeGift
	 *            包含赠品
	 * @param excludeFreeShipping
	 *            排除免运费商品
	 * @return 重量
	 */
	@Transient
	public int getWeight(Store store, boolean includeGift, boolean excludeFreeShipping) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		int weight = 0;
		for (CartItem cartItem : getCartItems(store)) {
			if (cartItem == null || cartItem.getWeight() == null || (excludeFreeShipping && isFreeShipping(cartItem.getSku()))) {
				continue;
			}
			weight += cartItem.getWeight();
		}
		if (includeGift) {
			for (Sku gift : getGifts(store)) {
				if (gift == null || gift.getWeight() == null || (excludeFreeShipping && isFreeShipping(gift))) {
					continue;
				}
				weight += gift.getWeight();
			}
		}
		return weight;
	}

	/**
	 * 获取数量
	 *
	 * @param store
	 *            店铺
	 * @param includeGift
	 *            包含赠品
	 * @return 数量
	 */
	@Transient
	public int getQuantity(Store store, boolean includeGift) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		int quantity = 0;
		for (CartItem cartItem : getCartItems(store)) {
			if (cartItem != null && cartItem.getQuantity() != null) {
				quantity += cartItem.getQuantity();
			}
		}
		if (includeGift) {
			quantity += getGifts(store).size();
		}
		return quantity;
	}



	//获取额外的运费
	@Transient
	public BigDecimal getMoreFreight(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		BigDecimal freight=BigDecimal.ZERO ;
		for (CartItem cartItem : getCartItems(store)) {
			if(cartItem.getSku().getProduct().getMoreFreight()!=null){
				freight =freight.add(cartItem.getSku().getProduct().getMoreFreight().multiply(new BigDecimal(cartItem.getQuantity())));
			}
		}
		return freight;
	}
	/**
	 * 获取数量
	 *
	 * @param includeGift
	 *            包含赠品
	 * @return 数量
	 */
	@Transient
	public int getQuantity(boolean includeGift) {
		int quantity = 0;
		for (Store store : getStores()) {
			quantity += getQuantity(store, includeGift);
		}
		return quantity;
	}

	/**
	 * 获取价格
	 *
	 * @param store
	 *            店铺
	 * @return 价格
	 */
	@Transient
	public BigDecimal getPrice(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getCartItems(store)) {
			price = price.add(cartItem.getSubtotal());
		}
		return price;
	}

	/**
	 * 获取价格
	 *
	 * @return 价格
	 */
	@Transient
	public BigDecimal getPrice() {
		BigDecimal price = BigDecimal.ZERO;
		for (Store store : getStores()) {
			price = price.add(getPrice(store));
		}
		return price;
	}

	/**
	 * 获取赠送积分
	 *
	 * @param store
	 *            店铺
	 * @return 赠送积分
	 */
	@Transient
	public long getRewardPoint(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		long rewardPoint = 0L;
		for (CartItem cartItem : getCartItems(store)) {
			rewardPoint += cartItem.getRewardPoint();
		}
		return rewardPoint;
	}

	/**
	 * 获取赠送积分
	 *
	 * @return 赠送积分
	 */
	@Transient
	public long getRewardPoint() {
		long rewardPoint = 0L;
		for (Store store : getStores()) {
			rewardPoint += getRewardPoint(store);
		}
		return rewardPoint;
	}

	/**
	 * 获取兑换积分
	 *
	 * @param store
	 *            店铺
	 * @return 兑换积分
	 */
	@Transient
	public long getExchangePoint(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		long exchangePoint = 0L;
		for (CartItem cartItem : getCartItems(store)) {
			exchangePoint += cartItem.getExchangePoint();
		}
		return exchangePoint;
	}

	/**
	 * 获取兑换积分
	 *
	 * @return 兑换积分
	 */
	@Transient
	public long getExchangePoint() {
		long exchangePoint = 0L;
		for (Store store : getStores()) {
			exchangePoint += getExchangePoint(store);
		}
		return exchangePoint;
	}

	/**
	 * 获取折扣
	 *
	 * @param store
	 *            店铺
	 * @return 折扣
	 */
	@Transient
	public BigDecimal getDiscount(Store store) {
		Setting setting = SystemUtils.getSetting();
		return setting.setScale(BigDecimal.ZERO);
	}


	//单品折扣
	@Transient
	public BigDecimal getItemDiscount(Store store,CartItem  item) {
		Setting setting = SystemUtils.getSetting();
		return setting.setScale(BigDecimal.ZERO);
	}



	/**
	 * 获取折扣
	 *
	 * @return 折扣
	 */
	@Transient
	public BigDecimal getDiscount() {
		BigDecimal discount = BigDecimal.ZERO;
		for (Store store : getStores()) {
			discount = discount.add(getDiscount(store));
		}
		return discount;
	}

	/**
	 * 获取有效价格
	 *
	 * @param store
	 *            店铺
	 * @return 有效价格
	 */
	@Transient
	public BigDecimal getEffectivePrice(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		BigDecimal effectivePrice = getPrice(store).subtract(getDiscount(store));
		return effectivePrice.compareTo(BigDecimal.ZERO) >= 0 ? effectivePrice : BigDecimal.ZERO;
	}

	/**
	 * 获取有效价格
	 *
	 * @return 有效价格
	 */
	@Transient
	public BigDecimal getEffectivePrice() {
		BigDecimal effectivePrice = BigDecimal.ZERO;
		for (Store store : getStores()) {
			effectivePrice = effectivePrice.add(getEffectivePrice(store));
		}
		return effectivePrice;
	}

	/**
	 * 获取赠品
	 *
	 * @param store
	 *            店铺
	 * @return 赠品
	 */
	@Transient
	public Set<Sku> getGifts(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		Set<Sku> gifts = new HashSet<>();
		return gifts;
	}

	/**
	 * 获取赠品名称
	 *
	 * @param store
	 *            店铺
	 * @return 赠品名称
	 */
	@Transient
	public List<String> getGiftNames(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		List<String> giftNames = new ArrayList<>();
		for (Sku gift : getGifts(store)) {
			giftNames.add(gift.getName());
		}
		return giftNames;
	}

	/**
	 * 获取促销名称
	 *
	 * @param store
	 *            店铺
	 * @return 促销名称
	 */
	@Transient
	public List<String> getPromotionNames(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		List<String> promotionNames = new ArrayList<>();
		return promotionNames;
	}


	/**
	 * 获取是否需要物流
	 *
	 * @param store
	 *            店铺
	 * @return 是否需要物流
	 */
	@Transient
	public boolean getIsDelivery(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		return CollectionUtils.exists(getCartItems(store), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && BooleanUtils.isTrue(cartItem.getIsDelivery());
			}
		}) || CollectionUtils.exists(getGifts(store), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Sku sku = (Sku) object;
				return sku != null && BooleanUtils.isTrue(sku.getIsDelivery());
			}
		});
	}

	/**
	 * 获取是否需要物流
	 *
	 * @return 是否需要物流
	 */
	@Transient
	public boolean getIsDelivery() {
		return CollectionUtils.exists(getStores(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Store store = (Store) object;
				return store != null && getIsDelivery(store);
			}
		});
	}

	/**
	 * 获取标签
	 *
	 * @return 标签
	 */
	@Transient
	public String getTag() {
		Set<Map<String, Object>> items = new HashSet<>();
		for (CartItem cartItem : this) {
			Map<String, Object> item = new HashMap<>();
			item.put("skuId", cartItem.getSku().getId());
			item.put("quantity", cartItem.getQuantity());
			item.put("price", cartItem.getPrice());
			items.add(item);
		}
		return DigestUtils.md5Hex(JsonUtils.toJson(items));
	}

	/**
	 * 获取购物车项
	 *
	 * @param store
	 *            店铺
	 * @return 购物车项
	 */
	private Set<CartItem> getCartItems(Store store, boolean flag) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		Set<CartItem> cartItems = new HashSet<>();
		for (CartItem cartItem : getCartItems(store)) {
			if (cartItem != null && cartItem.getSku() != null ) {
				cartItems.add(cartItem);
			}
		}
		return cartItems;
	}

	/**
	 * 获取数量
	 *
	 * @param store
	 *            店铺
	 * @return 数量
	 */
	private int getQuantity(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		int quantity = 0;
		for (CartItem cartItem : getCartItems(store, true)) {
			if (cartItem != null && cartItem.getQuantity() != null) {
				quantity += cartItem.getQuantity();
			}
		}
		return quantity;
	}

	/**
	 * 获取价格
	 *
	 * @param store
	 *            店铺
	 * @return 价格
	 */
	private BigDecimal getPrice(Store store, boolean flag) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");
		BigDecimal price = BigDecimal.ZERO;
		for (CartItem cartItem : getCartItems(store, flag)) {
			price = price.add(cartItem.getSubtotal());
		}
		return price;
	}




	/**
	 * 判断是否存在无效商品
	 *
	 * @return 是否存在无效商品
	 */
	@Transient
	public boolean hasNotActive() {
		return CollectionUtils.exists(getCartItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && !cartItem.getIsActive();
			}
		});
	}

	/**
	 * 判断是否存在已下架商品
	 *
	 * @return 是否存在已下架商品
	 */
	@Transient
	public boolean hasNotMarketable() {
		return CollectionUtils.exists(getCartItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && !cartItem.getIsMarketable();
			}
		});
	}

	/**
	 * 判断是否存在库存不足商品
	 *
	 * @return 是否存在库存不足商品
	 */
	@Transient
	public boolean hasLowStock() {
		return CollectionUtils.exists(getCartItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && cartItem.getIsLowStock();
			}
		});
	}

	/**
	 * 判断是否存在过期店铺商品
	 *
	 * @return 是否存在过期店铺商品
	 */
	@Transient
	public boolean hasExpiredProduct() {
		return CollectionUtils.exists(getCartItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && cartItem.hasExpiredProduct();
			}
		});
	}

	/**
	 * 判断是否免运费
	 *
	 * @param sku
	 *            SKU
	 * @return 是否免运费
	 */
	@SuppressWarnings("unchecked")
	@Transient
	public boolean isFreeShipping(final Sku sku) {
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		if (!sku.getIsDelivery()) {
			return true;
		}
		return  false;
	}

	/**
	 * 判断是否免运费
	 *
	 * @param store
	 *            店铺
	 * @return 是否免运费
	 */
	@Transient
	public boolean isFreeShipping(Store store) {
		Assert.notNull(store, "[Assertion failed] - store is required; it must not be null");

		return !CollectionUtils.exists(getCartItems(store), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				CartItem cartItem = (CartItem) object;
				return cartItem != null && cartItem.getSku() != null && !isFreeShipping(cartItem.getSku());
			}
		});
	}

	/**
	 * 购物车项数量
	 *
	 * @return 购物车项数量
	 */
	@Transient
	public int size() {
		return getCartItems() != null ? getCartItems().size() : 0;
	}

	/**
	 * 判断购物车项是否为空
	 *
	 * @return 购物车项是否为空
	 */
	@Transient
	public boolean isEmpty() {
		return CollectionUtils.isEmpty(getCartItems());
	}

	/**
	 * 添加购物车项
	 *
	 * @param cartItem
	 *            购物车项
	 */
	@Transient
	public void add(CartItem cartItem) {
		Assert.notNull(cartItem, "[Assertion failed] - cartItem is required; it must not be null");

		if (getCartItems() == null) {
			setCartItems(new HashSet<CartItem>());
		}
		getCartItems().add(cartItem);
	}

	/**
	 * 移除购物车项
	 *
	 * @param cartItem
	 *            购物车项
	 */
	@Transient
	public void remove(CartItem cartItem) {
		Assert.notNull(cartItem, "[Assertion failed] - cartItem is required; it must not be null");

		if (getCartItems() != null) {
			getCartItems().remove(cartItem);
		}
	}

	/**
	 * 清空购物车项
	 */
	@Transient
	public void clear() {
		if (getCartItems() != null) {
			getCartItems().clear();
		}
	}

	/**
	 * 实现iterator方法
	 *
	 * @return Iterator
	 */
	@Override
	@Transient
	public Iterator<CartItem> iterator() {
		return getCartItems() != null ? getCartItems().iterator() : Collections.<CartItem>emptyIterator();
	}

}
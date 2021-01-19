package net.xiaoxiangshop.event;

import net.xiaoxiangshop.entity.Cart;
import net.xiaoxiangshop.entity.Sku;

/**
 * Event - 添加购物车SKU
 * 
 */
public class CartAddedEvent extends CartEvent {

	private static final long serialVersionUID = 4639867730725258843L;

	/**
	 * SKU
	 */
	private Sku sku;

	/**
	 * 数量
	 */
	private int quantity;

	/**
	 * 构造方法
	 * 
	 * @param source
	 *            事件源
	 * @param cart
	 *            购物车
	 * @param sku
	 *            SKU
	 * @param quantity
	 *            数量
	 */
	public CartAddedEvent(Object source, Cart cart, Sku sku, int quantity) {
		super(source, cart);
		this.sku = sku;
		this.quantity = quantity;
	}

	/**
	 * 获取SKU
	 * 
	 * @return SKU
	 */
	public Sku getSku() {
		return sku;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	public int getQuantity() {
		return quantity;
	}

}
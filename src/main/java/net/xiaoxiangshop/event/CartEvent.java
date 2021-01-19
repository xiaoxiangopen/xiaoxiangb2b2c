package net.xiaoxiangshop.event;

import org.springframework.context.ApplicationEvent;

import net.xiaoxiangshop.entity.Cart;

/**
 * Event - 购物车
 * 
 */
public abstract class CartEvent extends ApplicationEvent {

	private static final long serialVersionUID = -4180050946434009635L;

	/**
	 * 购物车
	 */
	private Cart cart;

	/**
	 * 构造方法
	 * 
	 * @param source
	 *            事件源
	 * @param cart
	 *            购物车
	 */
	public CartEvent(Object source, Cart cart) {
		super(source);
		this.cart = cart;
	}

	/**
	 * 获取购物车
	 * 
	 * @return 购物车
	 */
	public Cart getCart() {
		return cart;
	}

}
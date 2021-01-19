package net.xiaoxiangshop.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import net.xiaoxiangshop.dao.CartDao;
import net.xiaoxiangshop.dao.CartItemDao;
import net.xiaoxiangshop.entity.Cart;
import net.xiaoxiangshop.entity.CartItem;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Sku;
import net.xiaoxiangshop.event.CartAddedEvent;
import net.xiaoxiangshop.event.CartClearedEvent;
import net.xiaoxiangshop.event.CartMergedEvent;
import net.xiaoxiangshop.event.CartModifiedEvent;
import net.xiaoxiangshop.event.CartRemovedEvent;
import net.xiaoxiangshop.service.CartService;
import net.xiaoxiangshop.service.UserService;
import net.xiaoxiangshop.util.WebUtils;

/**
 * Service - 购物车
 * 
 */
@Service
public class CartServiceImpl extends BaseServiceImpl<Cart> implements CartService {

	@Inject
	private ApplicationEventPublisher applicationEventPublisher;
	@Inject
	private CartDao cartDao;
	@Inject
	private CartItemDao cartItemDao;
	@Inject
	private UserService userService;

	@Override
	@Transactional(readOnly = true)
	public Cart getCurrent() {
		Member currentUser = userService.getCurrent(Member.class);
		return currentUser != null ? currentUser.getCart() : getAnonymousCart();
	}

	@Override
	public Cart create() {
		Member currentUser = userService.getCurrent(Member.class);
		if (currentUser != null && currentUser.getCart() != null) {
			return currentUser.getCart();
		}
		Cart cart = new Cart();
		if (currentUser != null) {
			cart.setMember(currentUser);
			currentUser.setCart(cart);
		}
		cart.setKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		if (cart.getMember() == null) {
			cart.setExpire(DateUtils.addSeconds(new Date(), Cart.TIMEOUT));
		} else {
			cart.setExpire(null);
		}
		super.save(cart);
		return cart;
	}

	@Override
	public void add(Cart cart, Sku sku, int quantity) {
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.isTrue(!cart.isNew(), "[Assertion failed] - cart must be new");
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		Assert.isTrue(!sku.isNew(), "[Assertion failed] - sku must be new");
		Assert.state(quantity > 0, "[Assertion failed] - quantity must be greater than 0");

		addInternal(cart, sku, quantity);

		applicationEventPublisher.publishEvent(new CartAddedEvent(this, cart, sku, quantity));
	}

	@Override
	public void modify(Cart cart, Sku sku, int quantity) {
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.isTrue(!cart.isNew(), "[Assertion failed] - cart must be new");
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		Assert.isTrue(!sku.isNew(), "[Assertion failed] - sku must be new");
		Assert.isTrue(cart.contains(sku), "[Assertion failed] - cart must contains sku");
		Assert.state(quantity > 0, "[Assertion failed] - quantity must be greater than 0");

		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			return;
		}

		CartItem cartItem = cart.getCartItem(sku);
		cartItem.setQuantity(quantity);
		cartItem.setLastModifiedDate(new Date());
		cartItemDao.update(cartItem);

		applicationEventPublisher.publishEvent(new CartModifiedEvent(this, cart, sku, quantity));
	}

	@Override
	public void remove(Cart cart, Sku sku) {
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.isTrue(!cart.isNew(), "[Assertion failed] - cart must be new");
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		Assert.isTrue(!sku.isNew(), "[Assertion failed] - sku must be new");
		Assert.isTrue(cart.contains(sku), "[Assertion failed] - cart must contains sku");

		CartItem cartItem = cart.getCartItem(sku);
		cartItemDao.remove(cartItem);
		cart.remove(cartItem);

		applicationEventPublisher.publishEvent(new CartRemovedEvent(this, cart, sku));
	}

	@Override
	public void clear(Cart cart) {
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.isTrue(!cart.isNew(), "[Assertion failed] - cart must be new");

		for (CartItem cartItem : cart) {
			cartItemDao.remove(cartItem);
		}
		cart.clear();

		applicationEventPublisher.publishEvent(new CartClearedEvent(this, cart));
	}

	@Override
	public void merge(Cart cart) {
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.isTrue(!cart.isNew(), "[Assertion failed] - cart must be new");
		Assert.notNull(cart.getMember(), "[Assertion failed] - cart member is required; it must not be null");

		Cart anonymousCart = getAnonymousCart();
		if (anonymousCart != null) {
			for (CartItem cartItem : anonymousCart) {
				Sku sku = cartItem.getSku();
				int quantity = cartItem.getQuantity();
				addInternal(cart, sku, quantity);
			}
			cartDao.remove(anonymousCart);
		}

		applicationEventPublisher.publishEvent(new CartMergedEvent(this, cart));
	}

	@Override
	public void deleteExpired() {
		cartDao.deleteExpired();
	}

	@Override
	public List<Cart> findCartByUserId(Member member) {
		return cartDao.findCartByUserId(member);
	}

	/**
	 * 获取匿名购物车
	 * 
	 * @return 匿名购物车，若不存在则返回null
	 */
	private Cart getAnonymousCart() {
		HttpServletRequest request = WebUtils.getRequest();
		if (request == null) {
			return null;
		}
		String key = WebUtils.getCookie(request, Cart.KEY_COOKIE_NAME);
		Cart cart = StringUtils.isNotEmpty(key) ? cartDao.findByAttribute("cart_key", key) : null;
		return cart != null && cart.getMember() == null ? cart : null;
	}

	/**
	 * 添加购物车SKU
	 * 
	 * @param cart
	 *            购物车
	 * @param sku
	 *            SKU
	 * @param quantity
	 *            数量
	 */
	private void addInternal(Cart cart, Sku sku, int quantity) {
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.isTrue(!cart.isNew(), "[Assertion failed] - cart must be new");
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		Assert.isTrue(!sku.isNew(), "[Assertion failed] - sku must be new");
		Assert.state(quantity > 0, "[Assertion failed] - quantity must be greater than 0");

		if (cart.contains(sku)) {
			CartItem cartItem = cart.getCartItem(sku);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
				return;
			}
			cartItem.add(quantity);
			cartItemDao.update(cartItem);
		} else {
			if (Cart.MAX_CART_ITEM_SIZE != null && cart.size() >= Cart.MAX_CART_ITEM_SIZE) {
				return;
			}
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return;
			}
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setSku(sku);
			cartItem.setCart(cart);
			cartItem.setId(IdWorker.getId());
			cartItem.setCreatedDate(new Date());
			cartItem.setVersion(0L);
			cartItemDao.save(cartItem);
			cart.add(cartItem);
		}
	}

}
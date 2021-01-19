package net.xiaoxiangshop.api.controller.member;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import net.xiaoxiangshop.*;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;

import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.config.WxPayConfiguration;
import net.xiaoxiangshop.plugin.PaymentPlugin;
import net.xiaoxiangshop.security.CurrentCart;
import net.xiaoxiangshop.security.CurrentUser;
import net.xiaoxiangshop.util.SpringUtils;
import net.xiaoxiangshop.util.SystemUtils;
import net.xiaoxiangshop.util.WebUtils;

/**
 * 订单 - 接口类
 */
@RestController
@RequestMapping("/api/member/order")
public class OrderAPIController extends BaseAPIController {

	private static final Logger _logger = LoggerFactory.getLogger(OrderAPIController.class);
	//积分卡描述
	private String JIFEN_CARD_DESN="积分卡|0306";
	@Inject
	private SkuService skuService;
	@Inject
	private AreaService areaService;
	@Inject
	private ReceiverService receiverService;
	@Inject
	private PaymentMethodService paymentMethodService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private UserService userService;
	@Inject
	private OrderService orderService;
	@Inject
	private PaymentTransactionService paymentTransactionService;
	@Inject
	private PluginService pluginService;
	@Inject
    private WxPayService wxService;
	@Inject
	private OrderPlusService orderPlusService;
	@Inject
	private MemberService memberService;
	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private ErpSyncService erpSyncService;
	@Inject
	private OrderPaymentService orderPaymentService;
	@Inject
	private SnService snService;
	/**
	 * 检查购物车
	 */
	@GetMapping("/check_cart")
	public ApiResult checkCart(@CurrentCart Cart currentCart) {
		if (currentCart == null || currentCart.isEmpty()) {
			return ResultUtils.unprocessableEntity("shop.order.cartEmpty");
		}
		if (currentCart.hasNotActive()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasNotActive");
		}
		if (currentCart.hasNotMarketable()) {
			//商品下架给出提示是哪件商品
			for (CartItem c:currentCart.getCartItems()) {
				if(!BooleanUtils.isTrue(c.getSku().getProduct().getIsMarketable())){
					String  msg = "编号为" + c.getSku().getProduct().getInternalNumber()+ "的商品:" + c.getSku().getProduct().getName() + "已下架，暂时无法购买。";
					return ResultUtils.unprocessableEntity(msg);
				}
			}
//			return ResultUtils.unprocessableEntity("shop.order.cartHasNotMarketable");
		}
		if (currentCart.hasLowStock()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasLowStock");
		}
		if (currentCart.hasExpiredProduct()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasExpiredProduct");
		}
		return ResultUtils.ok(true);
	}
	
	/**
	 * 收货地址列表
	 */
	@GetMapping("/receiver_list")
	public ApiResult receiverList() {
		Member currentUser = userService.getCurrent(Member.class);
		List<Map<String, Object>> map = new ArrayList<>();
		for (Receiver receiver : receiverService.findList(currentUser)) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", receiver.getId());
			item.put("consignee", receiver.getConsignee());
			item.put("areaName", receiver.getAreaName());
			item.put("address", receiver.getAddress());
			item.put("isDefault", receiver.getIsDefault());
			item.put("phone", receiver.getPhone());
			item.put("areaId", receiver.getArea().getId());
			map.add(item);
		}
		return ResultUtils.ok(map);
	}
	
	/**
	 * 添加收货地址
	 */
	@PostMapping("/add_receiver")
	public ApiResult addReceiver(@RequestBody Receiver receiver, @RequestParam("areaId") Long areaId) {
		
		Member currentUser = userService.getCurrent(Member.class);
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		if (Receiver.MAX_RECEIVER_COUNT != null && currentUser.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return ResultUtils.unprocessableEntity("shop.order.addReceiverCountNotAllowed", Receiver.MAX_RECEIVER_COUNT);
		}
		receiver.setAreaName(null);
		receiver.setMember(currentUser);
		return ResultUtils.ok(receiverService.save(receiver));
	}
	
	/**
	 * 结算
	 */
	@GetMapping("/checkout")
	public ApiResult checkout(Long skuId, Integer quantity,BigDecimal balance,String code,  @CurrentUser Member currentUser, @CurrentCart Cart currentCart) {
		Cart cart;
		Order.Type orderType;
		if (skuId != null) {
			Sku sku = skuService.find(skuId);
			if (sku == null) {
				return ResultUtils.UNPROCESSABLE_ENTITY;
			}
			if (Product.Type.GIFT.equals(sku.getType())) {
				return ResultUtils.UNPROCESSABLE_ENTITY;
			}
			if (quantity == null || quantity < 1) {
				return ResultUtils.UNPROCESSABLE_ENTITY;
			}

			cart = generateCart(currentUser, sku, quantity);

			switch (sku.getType()) {
			case GENERAL:
				orderType = Order.Type.GENERAL;
				break;
			case EXCHANGE:
				orderType = Order.Type.EXCHANGE;
				break;
			default:
				orderType = null;
				break;
			}
		} else {
			cart = currentCart;
			orderType = Order.Type.GENERAL;
		}



		if (cart == null || cart.isEmpty()) {
			return ResultUtils.unprocessableEntity("shop.order.cartEmpty");
		}

		Set<CartItem> cartItemSet =cart.getCartItems();
		Set<CartItem>  itemSet=new HashSet<CartItem>();
		for (CartItem c:cartItemSet){
			if (c.getIsBuy()==1){
				itemSet.add(c);
			}
		}
		cart.setCartItems(itemSet);

		if (cart.hasNotActive()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasNotActive");
		}
		if (cart.hasNotMarketable()) {

			//商品下架给出提示是哪件商品
			for (CartItem c:itemSet) {
				if(!BooleanUtils.isTrue(c.getSku().getProduct().getIsMarketable())){
					String  msg = "编号为" + c.getSku().getProduct().getInternalNumber() + "的商品:" + c.getSku().getProduct().getName() + "已下架，暂时无法购买。";
					return ResultUtils.unprocessableEntity(msg);
				}
			}

//			return ResultUtils.unprocessableEntity("shop.order.cartHasNotMarketable");
		}
		if (cart.hasLowStock()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasLowStock");
		}
		if (cart.hasExpiredProduct()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasExpiredProduct");
		}
		if (orderType == null) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}

		Receiver defaultReceiver = receiverService.findDefault(currentUser);

		PaymentMethod paymentMethod=paymentMethodService.find(1L);
		ShippingMethod shippingMethod=shippingMethodService.find(1L);

		List<Order> orders = orderService.generate(orderType, cart, defaultReceiver, paymentMethod, shippingMethod,null, null, null);

		Map<String, Object> data = new HashMap<>();
		
		// 收货地址
		Map<String, Object> receiver = new HashMap<>();
		if (defaultReceiver != null) {
			receiver.put("id", String.valueOf(defaultReceiver.getId()));
			receiver.put("consignee", defaultReceiver.getConsignee());
			receiver.put("phone", defaultReceiver.getPhone());
			receiver.put("areaName", defaultReceiver.getAreaName());
			receiver.put("address", defaultReceiver.getAddress());
			receiver.put("zipCode", defaultReceiver.getZipCode());
			data.put("receiver", receiver);
		}
		data.put("existReceiver", MapUtils.isEmpty(receiver) ? false : true);
		
		// 订单
		if (CollectionUtils.isNotEmpty(orders)) {
//			Order order = orders.get(0);

			BigDecimal price = BigDecimal.ZERO;
			BigDecimal fee = BigDecimal.ZERO;
			BigDecimal freight = BigDecimal.ZERO;
			BigDecimal tax = BigDecimal.ZERO;
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal amountPayable = BigDecimal.ZERO;
			Integer orderQuantity = 0;

			Setting setting = SystemUtils.getSetting();
			List<Map<String, Object>> orderItems = new ArrayList<>();

			for(Order order:orders ){
				price = price.add(order.getPrice());
				fee = fee.add(order.getFee());
				freight = freight.add(order.getFreight());
				tax = tax.add(order.getTax());
				amount = amount.add(order.getAmount());
				amountPayable = amountPayable.add(order.getAmountPayable());
				orderQuantity=orderQuantity+order.getQuantity();

				for (OrderItem orderItem : order.getOrderItems()) {
					Product product = orderItem.getSku().getProduct();
					Map<String, Object> item = new HashMap<>();
					item.put("orderItemName", orderItem.getName());
					item.put("thumbnail", StringUtils.isEmpty(orderItem.getThumbnail()) ? setting.getSiteUrl() + setting.getDefaultThumbnailProductImage() : setting.getSiteUrl() + orderItem.getThumbnail());
					item.put("specifications", orderItem.getSpecifications());
					item.put("price", orderItem.getPrice());
					item.put("quantity", orderItem.getQuantity());
					item.put("subtotal", orderItem.getSubtotal());
					item.put("productId", product.getId());
					orderItems.add(item);
				}
			}

			data.put("price", price);
			data.put("fee", fee);
			data.put("freight",freight);
			data.put("tax", tax);
			data.put("amount", amount);
			data.put("amountPayable", amountPayable);
			data.put("quantity", orderQuantity);
			data.put("balance", BigDecimal.ZERO);

			data.put("orderItems", orderItems);
		}
		data.put("userBalance",currentUser.getBalance());
		return ResultUtils.ok(data);
	}
	
	/**
	 * 创建
	 */
	@PostMapping("/create")
	public ApiResult create(Long skuId, Integer quantity, String code, String invoiceTitle, String invoiceTaxNumber, String balance, String memo, @CurrentUser Member currentUser, @CurrentCart Cart currentCart) {
		Cart cart;
		Order.Type orderType;


		if (skuId != null) {
			Sku sku = skuService.find(skuId);
			if (sku == null) {
				return ResultUtils.UNPROCESSABLE_ENTITY;
			}
			if (Product.Type.GIFT.equals(sku.getType())) {
				return ResultUtils.UNPROCESSABLE_ENTITY;
			}
			if (quantity == null || quantity < 1) {
				return ResultUtils.UNPROCESSABLE_ENTITY;
			}

			cart = generateCart(currentUser, sku, quantity);

			switch (sku.getType()) {
			case GENERAL:
				orderType = Order.Type.GENERAL;
				break;
			case EXCHANGE:
				orderType = Order.Type.EXCHANGE;
				break;
			default:
				orderType = null;
				break;
			}
		} else {
			cart = currentCart;
			orderType = Order.Type.GENERAL;
		}

		if (cart == null || cart.isEmpty()) {
			return ResultUtils.unprocessableEntity("shop.order.cartEmpty");
		}

		Set<CartItem> cartItemSet =cart.getCartItems();
		Set<CartItem>  itemSet=new HashSet<CartItem>();
		for (CartItem c:cartItemSet){
			if (c.getIsBuy()==1){
				itemSet.add(c);
			}
		}
		cart.setCartItems(itemSet);

		if (cart.hasNotActive()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasNotActive");
		}
		if (cart.hasNotMarketable()) {

			//商品下架给出提示是哪件商品
			for (CartItem c:itemSet) {
				if(!BooleanUtils.isTrue(c.getSku().getProduct().getIsMarketable())){
					String  msg = "编号为" + c.getSku().getProduct().getInternalNumber() + "的商品:" + c.getSku().getProduct().getName() + "已下架，暂时无法购买。";
					return ResultUtils.unprocessableEntity(msg);
				}
			}

//			return ResultUtils.unprocessableEntity("shop.order.cartHasNotMarketable");
		}
		if (cart.hasLowStock()) {
			return ResultUtils.unprocessableEntity("shop.order.cartHasLowStock");
		}
		if (cart.hasExpiredProduct()) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		if (orderType == null) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}

		//商品价格小于0不能购买
		for (CartItem c:itemSet) {
			if(c.getPrice().compareTo(BigDecimal.ZERO)<=0){
				String  msg = "编号为" + c.getSku().getProduct().getInternalNumber() + "的商品:" + c.getSku().getProduct().getName() + "暂时无法购买，请选择其他商品。";
				return ResultUtils.unprocessableEntity(msg);
			}
		}

		Receiver receiver = cart.getIsDelivery() ? receiverService.findDefault(currentUser) : null;
		if (cart.getIsDelivery() && (receiver == null || !currentUser.equals(receiver.getMember()))) {
			return ResultUtils.unprocessableEntity("请选择收货地址！");
		}
		else{
			String areaId= String.valueOf(receiver.getArea().getId());
			boolean  bool=false;
			Area a=  areaService.find(receiver.getArea().getId());
			String threePath = a.getTreePath();
			//用户所有的areaId
			String areaIds=threePath+areaId;
			String msg="";
			int i=0;
			for (CartItem c:itemSet) {
				//商品的areaId
				String s = c.getSku().getProduct().getAreaIds();
				List<String> same = new ArrayList<>();
				if(s != null){
					String [] a1= areaIds.split(",");
					String [] a2= s.split(",");
					for (String str : a1) {
						if (Arrays.binarySearch(a2, str) >= 0) {
							same.add(str);
						}
					}
				}
				if(same.size()>0||s==null){
					bool=true;
					i++;
				}
				if(same.size()==0&&s!=null){
					msg = "编号为" + c.getSku().getProduct().getSn() + "的商品:" + c.getSku().getProduct().getName() + "不在配送范围内，请选择其他商品。";
				}

			}
			Boolean f=false;
			if(itemSet.size()==1&&!bool){
				//没权限
				f=true;
			}
			if(itemSet.size()>0&&i!=itemSet.size()){
				//没权限
				f=true;
			}
			if(f){
				ApiResult  apiResult=new ApiResult();
				apiResult.setMessage(msg);
				apiResult.setStatus(HttpStatus.NOT_EXTENDED);
				return apiResult;
			}
		}

		BigDecimal balance0=new BigDecimal(balance);

		if (balance0 != null && balance0.compareTo(BigDecimal.ZERO) < 0) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
		if (balance0 != null && balance0.compareTo(currentUser.getAvailableBalance()) > 0) {
			return ResultUtils.unprocessableEntity("shop.order.insufficientBalance");
		}
		if (currentUser.getPoint() < cart.getExchangePoint()) {
			return ResultUtils.unprocessableEntity("shop.order.lowPoint");
		}

		if("undefined".equals(code)){
			code="";
		}
		if("undefined".equals(invoiceTitle)){
			invoiceTitle="";
		}
		if("undefined".equals(invoiceTaxNumber)){
			invoiceTaxNumber="";
		}
		if("undefined".equals(memo)){
			memo="";
		}


		PaymentMethod paymentMethod = paymentMethodService.find(1L);
		ShippingMethod shippingMethod = shippingMethodService.find(1L);
		Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) ? new Invoice(invoiceTitle, invoiceTaxNumber, null) : null;
		List<Order> orders = orderService.create(orderType, cart, receiver, paymentMethod, shippingMethod,  invoice, balance0, memo,"3");
		if (CollectionUtils.isEmpty(orders)) {
			return ResultUtils.UNPROCESSABLE_ENTITY;
		}
//		Order order = orders.get(0);

		//需要支付现金的订单
		List<Order> orderNew=new ArrayList<>();
		for (Order order:orders){
			System.out.println(order.getStatus());
			if(!Order.Status.PENDING_REVIEW.equals(order.getStatus())){
				orderNew.add(order);
			}
		}

		if(orderNew.size()>0){
			return getPay(orderNew, currentUser);
		}else{
			ApiResult  apiResult=new ApiResult();
			//选用511，标识支付完成
			apiResult.setStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
			Map<String, Object> data = new HashMap<>();
//			data.put("orderSn",orderSn);
			apiResult.setData(data);
			return apiResult;
		}
	}

	/**
	 * 支付
	 */
	@PostMapping("/payment")
	public ApiResult payment(@CurrentUser Member currentUser, String orderSn) {
		if (StringUtils.isEmpty(orderSn)) {
			return ResultUtils.unprocessableEntity("订单号不能为空!");
		}

		Order order = orderService.findBySn(orderSn);

		if (order == null) {
			return ResultUtils.unprocessableEntity("订单为空!");
		}

		//查询订单下面的库存是否足够
		List<OrderItem> orderItemList= order.getOrderItems();
		Boolean  flag=false;
		String  msg="";
		String  type="0";
		for (OrderItem orderItem:orderItemList){
			if(orderItem.getQuantity()>orderItem.getSku().getAvailableStock()){
				msg= "编号为" + orderItem.getSku().getProduct().getInternalNumber() + "的商品:" + orderItem.getSku().getProduct().getName() + "库存不足，暂时无法完成付款。";
				flag=true;
			}
		}

		if(flag){
			return ResultUtils.unprocessableEntity(msg);
		}

		BigDecimal amountPayable = order.getAmountPayable();
		if (order.getAmount().compareTo(order.getAmountPaid()) <= 0 || amountPayable.compareTo(BigDecimal.ZERO) <= 0) {
			return ResultUtils.unprocessableEntity("应付金额小于0");
		}
		
		return getPay1(orderSn, currentUser);
	}
	
	/**
	 * 生成购物车
	 * 
	 * @param member
	 *            会员
	 * @param sku
	 *            SKU
	 * @param quantity
	 *            数量
	 * @return 购物车
	 */
	public Cart generateCart(Member member, Sku sku, Integer quantity) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		Assert.notNull(sku, "[Assertion failed] - sku is required; it must not be null");
		Assert.state(!Product.Type.GIFT.equals(sku.getType()), "[Assertion failed] - sku type can't be GIFT");
		Assert.notNull(quantity, "[Assertion failed] - quantity is required; it must not be null");
		Assert.state(quantity > 0, "[Assertion failed] - quantity must be greater than 0");

		Cart cart = new Cart();
		Set<CartItem> cartItems = new HashSet<>();
		CartItem cartItem = new CartItem();
		cartItem.setSku(sku);
		cartItem.setQuantity(quantity);
		cartItems.add(cartItem);
		cartItem.setCart(cart);
		cart.setMember(member);
		cartItem.setIsBuy(1);
		cart.setCartItems(cartItems);
		return cart;
	}

	/**
	 * 列表
	 */
	@GetMapping(path = "/list")
	public ApiResult list(@CurrentUser Member currentUser, Order.Status status, Boolean hasExpired) {

		Pageable pageable=new Pageable();
		pageable.setPageSize(50);

		Page<Order> pages = orderService.findPage(null, status, null, currentUser, null,null,  null, null, null, hasExpired, pageable);
	 	Map<String, Object> data = new HashMap<>();
		Setting setting = SystemUtils.getSetting();
		
		// 订单头
		List<Map<String, Object>> list = new ArrayList<>();
		for (Order pOrder : pages.getContent()) {
			Map<String, Object> order = new HashMap<>();
			// 状态
			Map<String, Object> pStatus = new HashMap<>();
			pStatus.put("value", pOrder.getStatus().ordinal());
			pStatus.put("text", SpringUtils.getMessage("Order.Status." + pOrder.getStatus().name()));
			order.put("status", pStatus);
			order.put("sn", pOrder.getSn());
			order.put("amount", pOrder.getAmount());
			order.put("createdDate", pOrder.getCreatedDate());
			order.put("quantity", pOrder.getQuantity());

			Boolean  zf=false;
			Boolean  qx=false;
			Boolean  qr=false;
			//显示支付
			if(pOrder.getPaymentMethod()!=null&&pOrder.getAmountPayable().compareTo(BigDecimal.ZERO)==1){
				zf=true;
			}
			//显示取消
			if((!pOrder.hasExpired())&&(pOrder.getStatus().getValue()==0||pOrder.getStatus().getValue()==1)){
				qx=true;
			}
			//显示确认收货
			if((!pOrder.hasExpired())&&pOrder.getStatus().getValue()==3){
				qr=true;
			}

			order.put("zf",zf);
			order.put("qx",qx);
			order.put("qr",qr);

			list.add(order);
			
			// 订单行
			List<Map<String, Object>> items = new ArrayList<>();
			for (OrderItem orderItem : pOrder.getOrderItems()) {
				Map<String, Object> item = new HashMap<>();
				item.put("thumbnail", StringUtils.isEmpty(orderItem.getThumbnail()) ? setting.getSiteUrl() + setting.getDefaultThumbnailProductImage() : setting.getSiteUrl() + orderItem.getThumbnail());
				item.put("price", orderItem.getPrice());
				item.put("quantity", orderItem.getQuantity());
				items.add(item);
			}
			order.put("items", items);
		}
		data.put("list", list);
		return ResultUtils.ok(data);
	}
	
	
	/**
	 * 查看
	 */
	@GetMapping("/view")
	public ApiResult view(@CurrentUser Member currentUser, @RequestParam("orderSn") String orderSn) {
		Map<String, Object> data = new HashMap<>();
		Order pOrder = orderService.findBySn(orderSn);
		if (pOrder == null) {
			return ResultUtils.badRequest("订单不存在");
		}
		if (pOrder != null && !currentUser.equals(pOrder.getMember())) {
			return ResultUtils.badRequest("订单用户与当前用户不一致");
		}
		
		Map<String, Object> order = new HashMap<>();
		// 状态
		Map<String, Object> pStatus = new HashMap<>();
		pStatus.put("value", pOrder.getStatus().ordinal());
		pStatus.put("text", SpringUtils.getMessage("Order.Status." + pOrder.getStatus().name()));
		order.put("status", pStatus);
		order.put("sn", pOrder.getSn());
		order.put("amountPayable", pOrder.getAmountPayable());
		order.put("freight", pOrder.getFreight());
		order.put("createdDate", pOrder.getCreatedDate());
		order.put("consignee", pOrder.getConsignee());
		order.put("address", pOrder.getAddress());
		order.put("phone", pOrder.getPhone());
		order.put("areaName", pOrder.getAreaName());
		order.put("quantity", pOrder.getQuantity());


		Boolean  zf=false;
		Boolean  qx=false;
		Boolean  qr=false;
		//显示支付
		if(pOrder.getPaymentMethod()!=null&&pOrder.getAmountPayable().compareTo(BigDecimal.ZERO)==1){
			zf=true;
		}

		if((!pOrder.hasExpired())&&(pOrder.getStatus().getValue()==0||pOrder.getStatus().getValue()==1)){
			qx=true;
		}
		//显示确认收货
		if((!pOrder.hasExpired())&&pOrder.getStatus().getValue()==3){
			qr=true;
		}

		order.put("zf",zf);
		order.put("qx",qx);
		order.put("qr",qr);

		
		// 物流信息
		String deliveryCorp = "";
		String trackingNo = "";
		Set<OrderShipping> orderShippings = pOrder.getOrderShippings();
		for (OrderShipping orderShipping : orderShippings) {
			deliveryCorp += orderShipping.getDeliveryCorp();
			trackingNo += orderShipping.getTrackingNo();
		}
		order.put("deliveryCorp", deliveryCorp);
		order.put("trackingNo", trackingNo);
		data.put("order", order);
		
		BigDecimal skuTotal = BigDecimal.ZERO;
		Setting setting = SystemUtils.getSetting();
		List<Map<String, Object>> items = new ArrayList<>();
		for (OrderItem orderItem : pOrder.getOrderItems()) {
			Map<String, Object> item = new HashMap<>();
			item.put("itemName", orderItem.getName());
			item.put("thumbnail", StringUtils.isEmpty(orderItem.getThumbnail()) ? setting.getSiteUrl() + setting.getDefaultThumbnailProductImage() : setting.getSiteUrl() + orderItem.getThumbnail());
			item.put("specifications", orderItem.getSpecifications());
			item.put("price", orderItem.getPrice());
			Sku sku=  orderItem.getSku();
			Product product=sku.getProduct();
			item.put("id", String.valueOf(product.getId()));
			item.put("quantity", orderItem.getQuantity());
			items.add(item);
			skuTotal = skuTotal.add(orderItem.getSubtotal());
			
		}
		order.put("skuTotal", skuTotal);
		order.put("items", items);
		return ResultUtils.ok(data);
	}
	
	
	/**
	 * 取消
	 */
	@PostMapping("/cancel")
	public ApiResult cancel(@CurrentUser Member currentUser, @RequestParam("orderSn") String orderSn) {
		Order order = orderService.findBySn(orderSn);
		if (order == null) {
			return ResultUtils.badRequest("订单不存在");
		}

		if (order.hasExpired() || (!Order.Status.PENDING_PAYMENT.equals(order.getStatus()) && !Order.Status.PENDING_REVIEW.equals(order.getStatus()))) {
			return ResultUtils.unprocessableEntity("订单过期或状态非[等待审核]或[等待付款]");
		}
		if (!orderService.acquireLock(order, currentUser)) {
			return ResultUtils.unprocessableEntity("member.order.locked");
		}
		orderService.cancel(order);
		return ResultUtils.ok();
	}
	
	/**
	 * 收货
	 */
	@PostMapping("/receive")
	public ApiResult receive(@CurrentUser Member currentUser, @RequestParam("orderSn") String orderSn) {
		Order order = orderService.findBySn(orderSn);
		if (order == null) {
			return ResultUtils.badRequest("订单不存在");
		}

		if (order.hasExpired() || !Order.Status.SHIPPED.equals(order.getStatus())) {
			return ResultUtils.badRequest("订单不是[已发货]");
		}
		if (!orderService.acquireLock(order, currentUser)) {
			return ResultUtils.unprocessableEntity("member.order.locked");
		}
		orderService.receive(order);
		return ResultUtils.ok();
	}


	private ApiResult getPay(List<Order> orders, Member currentUser) {

		List<PaymentTransaction> paymentTransactions=new ArrayList<>();


		String  tnt_no=XmlUtils.getTnt();
		for (Order order:orders){
			PaymentItem paymentItem = new PaymentItem();
			paymentItem.setOrderSn(order.getSn());
			paymentItem.setType(PaymentItem.Type.ORDER_PAYMENT);
			String rePayUrl="/order/payment?orderSns="+order.getSn();
			PaymentTransaction.LineItem lineItem = paymentTransactionService.generate(paymentItem);
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(WxPayConfiguration.paymentPluginId);
			PaymentTransaction paymentTransaction = paymentTransactionService.generate(lineItem, paymentPlugin, rePayUrl,tnt_no);
			paymentTransactions.add(paymentTransaction);
		}

		BigDecimal amount=BigDecimal.ZERO;
		for (PaymentTransaction paymentTransaction:paymentTransactions){
			amount=amount.add(paymentTransaction.getAmount());
		}
		try {
			WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
			HttpServletRequest request = WebUtils.getRequest();

			orderRequest.setBody(StringUtils.abbreviate(getPaymentDescription(paymentTransactions.get(0)).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 600));
			orderRequest.setOutTradeNo(paymentTransactions.get(0).getSn()+XmlUtils.getRandom(2));
			orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(String.valueOf(amount))); //元转成分
			orderRequest.setOpenid(currentUser.getAttributeValue1());
			orderRequest.setSpbillCreateIp(request.getLocalAddr());
			orderRequest.setTimeStart(DateFormatUtils.format(new Date(), CommonAttributes.DATE_PATTERNS[8]));
			orderRequest.setTimeExpire(DateFormatUtils.format(DateUtils.addMinutes(new Date(),10), CommonAttributes.DATE_PATTERNS[8]));
			orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
			WxPayMpOrderResult result = wxService.createOrder(orderRequest);

			_logger.info("\n接收统一下单的消息：appId = [{}], nonceStr = [{}], packageValue = [{}], paySign = [{}], signType = [{}], timeStamp = [{}]",
					result.getAppId(), result.getNonceStr(),result.getPackageValue(), result.getPaySign(), result.getSignType(), result.getTimeStamp());

			return ResultUtils.ok(result);
		} catch (Exception e) {
			_logger.error("微信支付失败！订单号：{},原因:{}", orders.get(0), e.getMessage());
			e.printStackTrace();
			return ResultUtils.unprocessableEntity("支付失败，请稍后重试！");
		}
	}



	/**
	 * 获取支付信息
	 * @param orderSn
	 * @param currentUser
	 * @return
	 */
	private ApiResult getPay1(String orderSn, Member currentUser) {
		// 产生支付记录

		PaymentItem paymentItem = new PaymentItem();
		paymentItem.setOrderSn(orderSn);
		paymentItem.setType(PaymentItem.Type.ORDER_PAYMENT);
		String rePayUrl="/order/payment?orderSns="+orderSn;
		PaymentTransaction.LineItem lineItem = paymentTransactionService.generate(paymentItem);
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(WxPayConfiguration.paymentPluginId);
		PaymentTransaction paymentTransaction = paymentTransactionService.generate(lineItem, paymentPlugin, rePayUrl,XmlUtils.getTnt());
		
		try {
			WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
			HttpServletRequest request = WebUtils.getRequest();

			orderRequest.setBody(StringUtils.abbreviate(getPaymentDescription(paymentTransaction).replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", StringUtils.EMPTY), 600));
			orderRequest.setOutTradeNo(paymentTransaction.getSn());
//			orderRequest.setOutTradeNo(orderSn);
			orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(String.valueOf(paymentTransaction.getAmount()))); //元转成分
			orderRequest.setOpenid(currentUser.getAttributeValue1());
			orderRequest.setSpbillCreateIp(request.getLocalAddr());
			orderRequest.setTimeStart(DateFormatUtils.format(new Date(), CommonAttributes.DATE_PATTERNS[8]));
			orderRequest.setTimeExpire(DateFormatUtils.format(DateUtils.addMinutes(new Date(),10), CommonAttributes.DATE_PATTERNS[8]));
			orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
			WxPayMpOrderResult result = wxService.createOrder(orderRequest);

			_logger.info("\n接收统一下单的消息：appId = [{}], nonceStr = [{}], packageValue = [{}], paySign = [{}], signType = [{}], timeStamp = [{}]",
					result.getAppId(), result.getNonceStr(),result.getPackageValue(), result.getPaySign(), result.getSignType(), result.getTimeStamp());

			return ResultUtils.ok(result);
		} catch (Exception e) {
			_logger.error("微信支付失败！订单号：{},原因:{}", orderSn, e.getMessage());
			e.printStackTrace();
			return ResultUtils.unprocessableEntity("支付失败，请稍后重试！");
		}
	}
	
	/**
	 * 获取支付描述
	 *
	 * @param paymentTransaction
	 *            支付事务
	 * @return 支付描述
	 */
	private String getPaymentDescription(PaymentTransaction paymentTransaction) {
		Assert.notNull(paymentTransaction, "[Assertion failed] - paymentTransaction is required; it must not be null");
		if (CollectionUtils.isEmpty(paymentTransaction.getChildren())) {
			Assert.notNull(paymentTransaction.getType(), "[Assertion failed] - paymentTransaction type is required; it must not be null");
		} else {
			return SpringUtils.getMessage("shop.payment.paymentDescription", paymentTransaction.getSn());
		}

		switch (paymentTransaction.getType()) {
			case ORDER_PAYMENT:
				return SpringUtils.getMessage("shop.payment.orderPaymentDescription", paymentTransaction.getOrder().getSn());
			default:
				return SpringUtils.getMessage("shop.payment.paymentDescription", paymentTransaction.getSn());
		}
	}



	/**
	 * 历史订单列表
	 */
	@GetMapping(path = "/historyList")
	public ApiResult historyList(@CurrentUser Member currentUser) {

		Pageable pageable=new Pageable();
		pageable.setPageSize(50);

		Page<OrderPlus> pages = orderPlusService.findPage(null, null, null, currentUser, null, null, null, null, null, null, pageable);
		Map<String, Object> data = new HashMap<>();
		Setting setting = SystemUtils.getSetting();
		// 订单头
		List<Map<String, Object>> list = new ArrayList<>();
		for (OrderPlus pOrder : pages.getContent()) {
			Map<String, Object> order = new HashMap<>();
			// 状态
			Map<String, Object> pStatus = new HashMap<>();
			pStatus.put("value", pOrder.getStatus().ordinal());
			pStatus.put("text", SpringUtils.getMessage("Order.Status." + pOrder.getStatus().name()));
			order.put("status", pStatus);
			order.put("sn", pOrder.getSn());
			order.put("id", String.valueOf(pOrder.getId()));
			order.put("amount", pOrder.getAmount());
			order.put("createdDate", pOrder.getCreatedDate());
			order.put("quantity", pOrder.getQuantity());
			list.add(order);

			// 订单行
			List<Map<String, Object>> items = new ArrayList<>();
			for (OrderPlusItem orderItem : pOrder.getOrderItems()) {
				Map<String, Object> item = new HashMap<>();
				item.put("thumbnail", StringUtils.isEmpty(orderItem.getThumbnail()) ? setting.getSiteUrl() + setting.getDefaultThumbnailProductImage() : setting.getSiteUrl() + orderItem.getThumbnail());
				item.put("price", orderItem.getPrice());
				item.put("quantity", orderItem.getQuantity());
				item.put("id", String.valueOf(orderItem.getId()));
				items.add(item);
			}
			order.put("items", items);
		}
		data.put("list", list);
		return ResultUtils.ok(data);
	}



	/**
	 * 查看历史订单详细
	 */
	@GetMapping("/historyView")
	public ApiResult historyView(@CurrentUser Member currentUser, Long orderId) {
		Map<String, Object> data = new HashMap<>();
		OrderPlus pOrder = orderPlusService.find(orderId);
		if (pOrder == null) {
			return ResultUtils.badRequest("订单不存在");
		}
		if (pOrder != null && !currentUser.equals(pOrder.getMember())) {
			return ResultUtils.badRequest("订单用户与当前用户不一致");
		}

		Map<String, Object> order = new HashMap<>();
		// 状态
		Map<String, Object> pStatus = new HashMap<>();
		pStatus.put("value", pOrder.getStatus().ordinal());
		pStatus.put("text", SpringUtils.getMessage("Order.Status." + pOrder.getStatus().name()));
		order.put("status", pStatus);
		order.put("sn", pOrder.getSn());
		order.put("amountPayable", pOrder.getAmountPayable());
		order.put("freight", pOrder.getFreight());
		order.put("createdDate", pOrder.getCreatedDate());
		order.put("consignee", pOrder.getConsignee());
		order.put("address", pOrder.getAddress());
		order.put("phone", pOrder.getPhone());
		order.put("areaName", pOrder.getAreaName());
		order.put("quantity", pOrder.getQuantity());
		order.put("id", String.valueOf(pOrder.getId()));
		// 物流信息
		String deliveryCorp = "";
		String trackingNo = "";
		Set<OrderShipping> orderShippings = pOrder.getOrderShippings();
		for (OrderShipping orderShipping : orderShippings) {
			deliveryCorp += orderShipping.getDeliveryCorp();
			trackingNo += orderShipping.getTrackingNo();
		}
		order.put("deliveryCorp", deliveryCorp);
		order.put("trackingNo", trackingNo);
		data.put("order", order);

		BigDecimal skuTotal = BigDecimal.ZERO;
		Setting setting = SystemUtils.getSetting();
		List<Map<String, Object>> items = new ArrayList<>();
		for (OrderPlusItem orderItem : pOrder.getOrderItems()) {
			Map<String, Object> item = new HashMap<>();
			item.put("itemName", orderItem.getName());
			item.put("thumbnail", StringUtils.isEmpty(orderItem.getThumbnail()) ? setting.getDefaultThumbnailProductImage() : orderItem.getThumbnail());
			item.put("specifications", orderItem.getSpecifications());
			item.put("price", orderItem.getPrice());

			item.put("quantity", orderItem.getQuantity());
			items.add(item);
			skuTotal = skuTotal.add(orderItem.getSubtotal());

		}
		order.put("skuTotal", skuTotal);
		order.put("items", items);
		return ResultUtils.ok(data);
	}


	/**
	 * 使用余额一键付款
	 */
	@PostMapping("/payAmount")
	public ApiResult calculateAmount(String  orderId, @CurrentUser Member currentUser) {
		Map<String, Object> data = new HashMap<>();
		Order order= orderService.findBySn(orderId);
		if(order==null){
			return ResultUtils.badRequest("订单不存在");
		}

		//查询订单下面的库存是否足够
		List<OrderItem> orderItemList= order.getOrderItems();
		Boolean  flag=false;
		String  msg="";
		String  type="0";
		for (OrderItem orderItem:orderItemList){
			if(orderItem.getQuantity()>orderItem.getSku().getAvailableStock()){
				msg= "编号为" + orderItem.getSku().getProduct().getSn() + "的商品:" + orderItem.getSku().getProduct().getName() + "库存不足，暂时无法完成付款。";
				flag=true;
			}
		}

		if(flag){
			data.put("msg", msg);
			data.put("type", type);
			return ResultUtils.ok(data);
		}

		//用户余额
		BigDecimal balance= currentUser.getBalance();
		//总金额
		BigDecimal amount=order.getAmount();
		//已付金额
		BigDecimal amountPaid=order.getAmountPaid();
		//未付
		BigDecimal wfAmount=amount.subtract(amountPaid);
		//账户余额够付
		if(balance.compareTo(wfAmount)==1){
			//扣掉用户余额
			currentUser.setBalance(currentUser.getBalance().subtract(wfAmount));
			memberService.update(currentUser);
			order.setAmountPaid(amount);
			order.setStatus(Order.Status.PENDING_REVIEW);
			orderService.update(order);


			OrderPayment orderPayment = new OrderPayment();
			orderPayment.setMethod(OrderPayment.Method.DEPOSIT);
			orderPayment.setFee(BigDecimal.ZERO);
			orderPayment.setOrder(order);
			orderPayment.setAmount(wfAmount);
			orderPayment.setPaymentMethod(JIFEN_CARD_DESN);
			orderPayment.setPayer(String.valueOf(order.getMember().getId()));

			orderPayment.setSn(snService.generate(Sn.Type.ORDER_PAYMENT));
			orderPayment.setOrder(order);
			orderPaymentService.save(orderPayment);


			MemberDepositLog memberDepositLog = new MemberDepositLog();
			memberDepositLog.setId(IdWorker.getId());
			memberDepositLog.setVersion(0L);
			memberDepositLog.setCreatedDate(new Date());
			memberDepositLog.setType(MemberDepositLog.Type.ORDER_PAYMENT);
			memberDepositLog.setCredit(BigDecimal.ZERO);
			memberDepositLog.setDebit(wfAmount);
			memberDepositLog.setBalance(currentUser.getBalance());
			memberDepositLog.setMemo("积分卡余额支付"+currentUser.getAttributeValue0());
			memberDepositLog.setMember(currentUser);
			memberDepositLog.setOrders(order.getId());
//            memberDepositLog.setCardNo(currentUser.getAttributeValue0());
			memberDepositLogService.insert(memberDepositLog);
			//上传数据到中台
			if(order.getStatus().equals(Order.Status.PENDING_REVIEW)&&order.getAmount().compareTo(order.getAmountPaid())==0){
				erpSyncService.uploadOrder(order.getSn());
			}

			msg="您已经使用余额￥"+wfAmount.setScale(2, RoundingMode.HALF_UP)+"付款成功。";
			type="1";

		}else{
			msg="您当前账户余额￥"+balance.setScale(2, RoundingMode.HALF_UP)+"不够支付商品剩余未支付余额，请充值。";
		}
		data.put("msg", msg);
		data.put("type", type);
		return ResultUtils.ok(data);
	}

}

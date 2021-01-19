package net.xiaoxiangshop.service.impl;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.inject.Inject;
import net.xiaoxiangshop.dao.*;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.entity.Order.CommissionType;
import net.xiaoxiangshop.entity.Order.Status;
import net.xiaoxiangshop.entity.Order.Type;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Service - 订单
 * 
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private StoreDao storeDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private ProductDao productDao;
	@Inject
	private OrderDao orderDao;
	@Inject
	private OrderItemDao orderItemDao;
	@Inject
	private OrderLogService orderLogService;
	@Inject
	private CartDao cartDao;
	@Inject
	private CartItemDao cartItemDao;
	@Inject
	private SnService snService;
	@Inject
	private OrderShippingDao orderShippingDao;
	@Inject
	private OrderShippingItemDao orderShippingItemDao;
	@Inject
	private OrderPaymentService orderPaymentService;
	@Inject
	private OrderRefundsService orderRefundsService;
	@Inject
	private OrderShippingService orderShippingService;
	@Inject
	private OrderReturnsService orderReturnsService;
	@Inject
	private DistributionCommissionService distributionCommissionService;
	@Inject
	private UserService userService;
	@Inject
	private MemberService memberService;
	@Inject
	private BusinessService businessService;
	@Inject
	private ProductService productService;
	@Inject
	private SkuService skuService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private ErpSyncService erpSyncService;
	@Inject
	private AreaService areaService;
	@Inject
	private PaymentTransactionService paymentTransactionService;
	@Inject
	private PaymentMethodService paymentMethodService;




	//免邮的区域ID
	@Value("${region_freemail_range}")
	private String region_freemail_range;

	//免邮起步值
	@Value("${region_freemail_max}")
	private String region_freemail_max;

	@Override
	@Transactional(readOnly = true)
	public Order findBySn(String sn) {
		return orderDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Order> findList(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds,  Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
			List<net.xiaoxiangshop.Order> orders,Long isRefund) {
		QueryWrapper<Order> queryWrapper = createQueryWrapper(null, count, filters, orders);
		return orderDao.findList(queryWrapper, type, status, store, member, product, isPendingReceive, isPendingRefunds, isExchangePoint, isAllocatedStock, hasExpired,isRefund);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Order> findPage(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable) {
		IPage<Order> iPage = getPluginsPage(pageable);
		if (pageable.getOrderDirection() == null && pageable.getOrderProperty() == null) {
			pageable.setOrderDirection(net.xiaoxiangshop.Order.Direction.DESC);
			pageable.setOrderProperty("createdDate");
		}
		QueryWrapper<Order> queryWrapper=getPageable(pageable);
		List<Filter> filters=pageable.getFilters();
		if(filters!=null){
			for(Filter filter:filters){
				String property=filter.getProperty();
				Object value=filter.getValue();
				Filter.Operator operator=filter.getOperator();
				if(operator.equals(Filter.Operator.GE)){
					queryWrapper.ge(true,property,value);
				}
				if(operator.equals(Filter.Operator.LE)){
					queryWrapper.le(true,property,value);
				}
				if(operator.equals(Filter.Operator.OTHER)){//自定义拼接查询
					if("transactionSn".equals(property)){//支付流水号
						queryWrapper.inSql(true,"id","SELECT orders FROM payment_transaction WHERE sn = '"+value+"' AND is_success=TRUE");
					}
				}
			}
			//重置一下排序
			String orderProperty = net.xiaoxiangshop.util.StringUtils.camel2Underline(pageable.getOrderProperty());
			if (StringUtils.isNotEmpty(orderProperty)) {
				if (pageable.getOrderDirection().equals(net.xiaoxiangshop.Order.Direction.ASC)) {
					queryWrapper.orderByAsc(true, orderProperty);
				}
				if (pageable.getOrderDirection().equals(net.xiaoxiangshop.Order.Direction.DESC)) {
					queryWrapper.orderByDesc(true, orderProperty);
				}
			}
		}
		iPage.setRecords(orderDao.findPage(iPage,queryWrapper, type, status, store, member, product, isPendingReceive, isPendingRefunds,  isExchangePoint, isAllocatedStock, hasExpired));
		return super.findPage(iPage, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Type type, Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds,  Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
		return orderDao.count(type, status, store, member, product, isPendingReceive, isPendingRefunds, isExchangePoint, isAllocatedStock, hasExpired);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Order.Type type, Order.Status status, Long storeId, Long memberId, Long productId, Boolean isPendingReceive, Boolean isPendingRefunds,  Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
		Store store = storeDao.find(storeId);
		if (storeId != null && store == null) {
			return 0L;
		}
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return 0L;
		}
		Product product = productDao.find(productId);
		if (productId != null && product == null) {
			return 0L;
		}

		return orderDao.count(type, status, store, member, product, isPendingReceive, isPendingRefunds,  isExchangePoint, isAllocatedStock, hasExpired);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateTax(BigDecimal price, BigDecimal offsetAmount) {
		Assert.notNull(price, "[Assertion failed] - price is required; it must not be null");
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - price must be equal or greater than 0");

		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsTaxPriceEnabled()) {
			return BigDecimal.ZERO;
		}
		BigDecimal amount = price;

		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		BigDecimal tax = amount.multiply(new BigDecimal(String.valueOf(setting.getTaxRate())));
		return tax.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(tax) : BigDecimal.ZERO;
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateTax(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");

		if (order.getInvoice() == null) {
			return BigDecimal.ZERO;
		}
		return calculateTax(order.getPrice(),  order.getOffsetAmount());
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount) {
		Assert.notNull(price, "[Assertion failed] - price is required; it must not be null");
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - price must be equal or greater than 0");
		Assert.state(fee == null || fee.compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - fee must be null or fee must be equal or greater than 0");
		Assert.state(freight == null || freight.compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - freight must be null or freight must be equal or greater than 0");
		Assert.state(tax == null || tax.compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - tax must be null or tax must be equal or greater than 0");

		Setting setting = SystemUtils.getSetting();
		BigDecimal amount = price;
		if (fee != null) {
			amount = amount.add(fee);
		}
		if (freight != null) {
			amount = amount.add(freight);
		}
		if (tax != null) {
			amount = amount.add(tax);
		}

		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		return amount.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(amount) : BigDecimal.ZERO;
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");

		return calculateAmount(order.getPrice(), order.getFee(), order.getFreight(), order.getTax(), order.getOffsetAmount());
	}

	@Override
	@Transactional(readOnly = true)
	public boolean acquireLock(Order order, User user) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isTrue(!user.isNew(), "[Assertion failed] - user must not be new");

		Long orderId = order.getId();
		Ehcache cache = cacheManager.getEhcache(Order.ORDER_LOCK_CACHE_NAME);
		cache.acquireWriteLockOnKey(orderId);
		try {
			Element element = cache.get(orderId);
			if (element != null && !user.getId().equals(element.getObjectValue())) {
				return false;
			}
			cache.put(new Element(orderId, user.getId()));
		} finally {
			cache.releaseWriteLockOnKey(orderId);
		}
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean acquireLock(Order order) {
		User currentUser = userService.getCurrent();
		return currentUser != null && acquireLock(order, currentUser);
	}

	@Override
	@Transactional(readOnly = true)
	public void releaseLock(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");

		Ehcache cache = cacheManager.getEhcache(Order.ORDER_LOCK_CACHE_NAME);
		cache.remove(order.getId());
	}

	@Override
	public void expiredRefundHandle() {
		while (true) {
			QueryWrapper<Order> queryWrapper = createQueryWrapper(null, 100, null, null);
			List<Order> orders = orderDao.findList(queryWrapper, null, null, null, null, null, true, null, null, null, true,1L);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					expiredRefund(order);
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	@Override
	public void expiredRefund(Order order) {
		if (order.getMember()==null||order == null || order.getRefundableAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}

		OrderRefunds orderRefunds = new OrderRefunds();
//		orderRefunds.setSn(snService.generate(Sn.Type.ORDER_REFUNDS));
		orderRefunds.setSn(order.getSn());
		orderRefunds.setMethod(OrderRefunds.Method.DEPOSIT);
		//这里减掉第三方支付的余额
		PaymentTransaction paymentTransaction= paymentTransactionService.findByOrders(order.getId());
		if(paymentTransaction!=null&&paymentTransaction.getIsSuccess()){
			orderRefunds.setAmount(order.getRefundableAmount().subtract(paymentTransaction.getAmount()));
		}else{
			orderRefunds.setAmount(order.getRefundableAmount());
		}
		orderRefunds.setOrder(order);
		orderRefundsService.save(orderRefunds);

		memberService.addSnBalance(order, orderRefunds.getAmount(), MemberDepositLog.Type.ORDER_REFUNDS, null);
		//修改订单的状态为退款成功
		order.setIsRefund(1L);
		orderDao.updateIsRefund(order);
		order.setAmountPaid(order.getAmountPaid().subtract(orderRefunds.getAmount()));
		order.setRefundAmount(order.getRefundAmount().add(orderRefunds.getAmount()));

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.REFUNDS);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);
	}

	public void appendMethodC(String content, String erp_type) {
		try {
			String fileName = "";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date = df.format(new Date());
			fileName = "/www/upload/txt/upload/" + erp_type + "_" + date + ".txt";
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void undoExpiredExchangePoint() {
		while (true) {
			QueryWrapper<Order> queryWrapper = createQueryWrapper(null, 100, null, null);
			List<Order> orders = orderDao.findList(queryWrapper, null, null, null, null, null, null, null, true, null, true,null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					undoExchangePoint(order);
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	@Override
	public void releaseExpiredAllocatedStock() {
		while (true) {
			QueryWrapper<Order> queryWrapper = createQueryWrapper(null, 100, null, null);
			List<Order> orders = orderDao.findList(queryWrapper, null, null, null, null, null, null, null, null, true, true,null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					releaseAllocatedStock(order);
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}



	@Override
	@Transactional(readOnly = true)
	public List<Order> generate(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Invoice invoice, BigDecimal balance, String memo) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.notNull(cart.getMember(), "[Assertion failed] - cart member is required; it must not be null");
		Assert.state(!cart.isEmpty(), "[Assertion failed] - cart must not be empty");

		Setting setting = SystemUtils.getSetting();
		Member member = cart.getMember();
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal discount = BigDecimal.ZERO;
		Long rewardPoint = 0L;


		List<Order> orders = new ArrayList<>();


		//剩余优惠券可用
		BigDecimal availablePrice = BigDecimal.ZERO;


		int i=0;
		for (Map.Entry<Store, Set<CartItem>> entry : cart.getCartItemGroup().entrySet()) {
			i++;
			Store store = entry.getKey();
			Set<CartItem> cartItems = entry.getValue();
			price = cart.getPrice(store);
			discount = cart.getDiscount(store);
			rewardPoint = cart.getRewardPoint(store);




			Order order = new Order();
			order.setType(type);
			order.setPrice(price);
			order.setFee(BigDecimal.ZERO);
			order.setOffsetAmount(BigDecimal.ZERO);
			order.setRefundAmount(BigDecimal.ZERO);
			order.setRewardPoint(rewardPoint);
			order.setExchangePoint(cart.getExchangePoint(store));
			order.setWeight(cart.getWeight(store, true, false));
			order.setQuantity(cart.getQuantity(store, true));
			order.setShippedQuantity(0);
			order.setReturnedQuantity(0);
			order.setMemo(memo);
			order.setIsReviewed(false);
			order.setIsExchangePoint(false);
			order.setIsAllocatedStock(false);
			order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
			order.setPaymentMethod(paymentMethod);
			order.setMember(member);
			order.setStore(store);
			order.setPromotionNames(new ArrayList<>(cart.getPromotionNames(store)));


			if (shippingMethod != null && shippingMethod.isSupported(paymentMethod) && cart.getIsDelivery(store)) {
				if(receiver!=null){
					order.setFreight(!cart.isFreeShipping(store) ? shippingMethodService.calculateFreight(shippingMethod, store, receiver, cart.getWeight(store, true, true)) : BigDecimal.ZERO);
				}
				else{
					order.setFreight(BigDecimal.ZERO);
				}
				order.setShippingMethod(shippingMethod);
			} else {
				order.setFreight(BigDecimal.ZERO);
				order.setShippingMethod(null);
			}


			order.setTax(calculateTax(order));

			if(receiver!=null){
				//1931 广东
				Long  id=receiver.getArea().getId();
				Area area= areaService.find(id);

				String [] three=(area.getTreePath()+id).split(",");

				BigDecimal yf=order.getFreight();
				order.setFreight(new BigDecimal(0));
				BigDecimal count =calculateAmount(order);

				if(store.getAreaTids()!=null){

					String [] ats=store.getAreaTids().split(",");
					boolean  bool=false;

					List<String> same = new ArrayList<>();
					for (String str : three) {
						if (Arrays.binarySearch(ats, str) >= 0) {
							same.add(str);
						}
					}
					if(same.size()>0){
						bool=true;
					}

					if(bool){
						if(count.compareTo(new BigDecimal(store.getMaxPrice()))> -1&&yf.compareTo(new BigDecimal(0))==1){
//							order.setFreight(new BigDecimal(0));
							order.setFreight(store.getExtraFreight());
						}else{
							order.setFreight(yf);
						}
					}else{
						//增加额外运费
						BigDecimal freight =cart.getMoreFreight(store);
						order.setFreight(yf.add(freight));
					}
				}else{
					if(count.compareTo(new BigDecimal(store.getMaxPrice()))> -1&&yf.compareTo(new BigDecimal(0))==1){
						order.setFreight(store.getExtraFreight());
					}
					else{
						BigDecimal freight =cart.getMoreFreight(store);
						order.setFreight(yf.add(freight));
					}
				}
			}

			order.setAmount(calculateAmount(order));

			if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(member.getAvailableBalance()) <= 0) {
				BigDecimal currentAmountPaid = balance.compareTo(order.getAmount()) > 0 ? order.getAmount() : balance;
				order.setAmountPaid(currentAmountPaid);
				balance = balance.subtract(currentAmountPaid);
			} else {
				order.setAmountPaid(BigDecimal.ZERO);
			}

			if (cart.getIsDelivery(store) && receiver != null) {
				order.setConsignee(receiver.getConsignee());
				order.setAreaName(receiver.getAreaName());
				order.setAddress(receiver.getAddress());
				order.setZipCode(receiver.getZipCode());
				order.setPhone(receiver.getPhone());
				order.setArea(receiver.getArea());
			}

			List<OrderItem> orderItems = order.getOrderItems();
			for (CartItem cartItem : cartItems) {
				Sku sku = cartItem.getSku();
				if (sku != null) {
					OrderItem orderItem = new OrderItem();
					orderItem.setSn(sku.getSn());
					orderItem.setName(sku.getName());
					orderItem.setType(sku.getType());
					orderItem.setPrice(cartItem.getPrice());
					orderItem.setWeight(sku.getWeight());
					orderItem.setIsDelivery(sku.getIsDelivery());
					orderItem.setThumbnail(sku.getThumbnail());
					orderItem.setQuantity(cartItem.getQuantity());
					orderItem.setShippedQuantity(0);
					orderItem.setReturnedQuantity(0);
					orderItem.setSku(cartItem.getSku());
					orderItem.setOrder(order);
					orderItem.setSpecifications(sku.getSpecifications());
					orderItems.add(orderItem);
				}
			}

			for (Sku gift : cart.getGifts(store)) {
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(gift.getSn());
				orderItem.setName(gift.getName());
				orderItem.setType(gift.getType());
				orderItem.setPrice(BigDecimal.ZERO);
				orderItem.setWeight(gift.getWeight());
				orderItem.setIsDelivery(gift.getIsDelivery());
				orderItem.setThumbnail(gift.getThumbnail());
				orderItem.setQuantity(1);
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setSku(gift);
				orderItem.setOrder(order);
				orderItem.setSpecifications(gift.getSpecifications());
				orderItems.add(orderItem);
			}
			orders.add(order);
		}
		return orders;
	}

	@Override
	public List<Order> create(Order.Type type, final Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Invoice invoice, BigDecimal balance, String memo,String dataSource) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");
		Assert.notNull(cart, "[Assertion failed] - cart is required; it must not be null");
		Assert.notNull(cart.getMember(), "[Assertion failed] - cart member is required; it must not be null");
		Assert.state(!cart.isEmpty(), "[Assertion failed] - cart must not be empty");

		if (cart.getIsDelivery()) {
			Assert.notNull(receiver, "[Assertion failed] - receiver is required; it must not be null");
			Assert.notNull(shippingMethod, "[Assertion failed] - shippingMethod is required; it must not be null");
			Assert.state(shippingMethod.isSupported(paymentMethod), "[Assertion failed] - shippingMethod must supported paymentMethod");
		} else {
			Assert.isNull(receiver, "[Assertion failed] - receiver must be null");
			Assert.isNull(shippingMethod, "[Assertion failed] - shippingMethod must be null");
		}

		for (CartItem cartItem : cart.getCartItems()) {
			Sku sku = cartItem.getSku();
			if (sku == null || !sku.getIsMarketable() || cartItem.getQuantity() > sku.getAvailableStock()) {
				throw new IllegalArgumentException();
			}
		}


		//剩余优惠券可用
		BigDecimal availablePrice = BigDecimal.ZERO;


		List<Order> orders = new ArrayList<>();
		int i=0;
		for (Map.Entry<Store, Set<CartItem>> entry : cart.getCartItemGroup().entrySet()) {
			i++;
			Store store = entry.getKey();
			Set<CartItem> cartItems = entry.getValue();

			for (Sku gift : cart.getGifts(store)) {
				if (!gift.getIsMarketable() || gift.getIsOutOfStock()) {
					throw new IllegalArgumentException();
				}
			}



			Setting setting = SystemUtils.getSetting();
			Member member = cart.getMember();
			Order order = new Order();
			order.setSn(snService.generate(Sn.Type.ORDER));
			order.setType(type);
			order.setPrice(cart.getPrice(store));
			order.setFee(BigDecimal.ZERO);
			order.setFreight(cart.getIsDelivery(store) && !cart.isFreeShipping(store) ? shippingMethodService.calculateFreight(shippingMethod, store, receiver, cart.getWeight(store, true, true)) : BigDecimal.ZERO);
			order.setOffsetAmount(BigDecimal.ZERO);
			order.setAmountPaid(BigDecimal.ZERO);
			order.setRefundAmount(BigDecimal.ZERO);
			order.setRewardPoint(cart.getRewardPoint(store));
			order.setExchangePoint(cart.getExchangePoint(store));
			order.setWeight(cart.getWeight(store, true, false));
			order.setQuantity(cart.getQuantity(store, true));
			order.setShippedQuantity(0);
			order.setReturnedQuantity(0);

			if("2".equals(dataSource)){
				order.setDataSource(XmlUtils.DATA_SOURCE_TWO);
			}else if("3".equals(dataSource)){
				order.setDataSource(XmlUtils.DATA_SOURCE_THREE);
			}else {
				order.setDataSource(XmlUtils.DATA_SOURCE_ONE);
			}

			if (cart.getIsDelivery(store)) {
				order.setConsignee(receiver.getConsignee());
				order.setAreaName(receiver.getAreaName());
				order.setAddress(receiver.getAddress());
				order.setZipCode(receiver.getZipCode());
				order.setPhone(receiver.getPhone());
				order.setArea(receiver.getArea());
				order.setShippingMethod(shippingMethod);
			}
			order.setMemo(memo);
			order.setIsReviewed(false);
			order.setIsExchangePoint(false);
			order.setIsAllocatedStock(false);
			order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
			order.setMember(member);
			order.setStore(store);
			order.setPromotionNames(cart.getPromotionNames(store));

			order.setTax(calculateTax(order));

			if(receiver!=null){
				Long  id=receiver.getArea().getId();
				Area area= areaService.find(id);

				//1931 广东
				String [] three=(area.getTreePath()+id).split(",");

				BigDecimal yf=order.getFreight();
				order.setFreight(new BigDecimal(0));
				BigDecimal count =calculateAmount(order);

				if(store.getAreaTids()!=null){
					String [] ats=store.getAreaTids().split(",");
					boolean  bool=false;
					List<String> same = new ArrayList<>();
					for (String str : three) {
						if (Arrays.binarySearch(ats, str) >= 0) {
							same.add(str);
						}
					}
					if(same.size()>0){
						bool=true;
					}
					if(bool){
						if(count.compareTo(new BigDecimal(store.getMaxPrice()))> -1&&yf.compareTo(new BigDecimal(0))==1){
//							order.setFreight(new BigDecimal(0));
							order.setFreight(store.getExtraFreight());
						}else{
							order.setFreight(yf);
						}
					}else{
						//增加额外运费
						BigDecimal freight =cart.getMoreFreight(store);
						order.setFreight(yf.add(freight));
						}
				}else {
					if(count.compareTo(new BigDecimal(store.getMaxPrice()))> -1&&yf.compareTo(new BigDecimal(0))==1){
						order.setFreight(store.getExtraFreight());
					}
					else{
						BigDecimal freight =cart.getMoreFreight(store);
						order.setFreight(yf.add(freight));
					}

				}
			}

			order.setAmount(calculateAmount(order));

			if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(member.getAvailableBalance()) > 0)) {
				throw new IllegalArgumentException();
			}
			BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
			if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
				if (paymentMethod == null) {
					throw new IllegalArgumentException();
				}
				order.setStatus(PaymentMethod.Type.DELIVERY_AGAINST_PAYMENT.equals(paymentMethod.getType()) ? Order.Status.PENDING_PAYMENT : Order.Status.PENDING_REVIEW);
				order.setPaymentMethod(paymentMethod);
				if (paymentMethod.getTimeout() != null && Order.Status.PENDING_PAYMENT.equals(order.getStatus())) {
					order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
				}
			} else {
				order.setStatus(Order.Status.PENDING_REVIEW);
			}

			PaymentMethod p1=paymentMethodService.find(1L);
			p1.setName("网上支付");
			order.setPaymentMethod(p1);
			
			order.setSn(StringUtils.lowerCase(order.getSn()));
			if (order.getArea() != null) {
				order.setAreaName(order.getArea().getFullName());
			}
			if (order.getPaymentMethod() != null) {
				order.setPaymentMethodName(order.getPaymentMethod().getName());
				order.setPaymentMethodType(order.getPaymentMethod().getType());
			}
			if (order.getShippingMethod() != null) {
				order.setShippingMethodName(order.getShippingMethod().getName());
			}


			List<OrderItem> orderItems = order.getOrderItems();
			for (CartItem cartItem : cartItems) {
				Sku sku = cartItem.getSku();
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(sku.getSn());
				orderItem.setName(sku.getName());
				orderItem.setType(sku.getType());

				orderItem.setPrice(cartItem.getPrice());
				BigDecimal m=cart.getItemDiscount(store,cartItem);
				orderItem.setTotaldiscount(m);
				if(m.compareTo(BigDecimal.ZERO)==1){
					orderItem.setCounterdiscount(setting.setScale(m.divide(new BigDecimal(cartItem.getQuantity()),MathContext.DECIMAL128)));
				}else{
					orderItem.setCounterdiscount(BigDecimal.ZERO);
				}

				orderItem.setWeight(sku.getWeight());
				orderItem.setIsDelivery(sku.getIsDelivery());
				orderItem.setThumbnail(sku.getThumbnail());
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setPlatformCommissionTotals(sku.getPlatformCommission(store.getType()).multiply(new BigDecimal(cartItem.getQuantity())));
				orderItem.setDistributionCommissionTotals(sku.getMaxCommission().multiply(new BigDecimal(cartItem.getQuantity())));
				orderItem.setSku(cartItem.getSku());
				orderItem.setOrder(order);
				orderItem.setId(IdWorker.getId());
				orderItem.setCreatedDate(new Date());
				orderItem.setVersion(0L);
				orderItem.setSpecifications(sku.getSpecifications());
				orderItem.setProportion(BigDecimal.ZERO);
				orderItems.add(orderItem);
			}

			for (Sku gift : cart.getGifts(store)) {
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(gift.getSn());
				orderItem.setName(gift.getName());
				orderItem.setType(gift.getType());
				orderItem.setPrice(BigDecimal.ZERO);
				orderItem.setWeight(gift.getWeight());
				orderItem.setIsDelivery(gift.getIsDelivery());
				orderItem.setThumbnail(gift.getThumbnail());
				orderItem.setQuantity(1);
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setPlatformCommissionTotals(gift.getPlatformCommission(store.getType()).multiply(new BigDecimal("1")));
				orderItem.setDistributionCommissionTotals(gift.getMaxCommission().multiply(new BigDecimal("1")));
				orderItem.setSku(gift);
				orderItem.setOrder(order);
				orderItem.setId(IdWorker.getId());
				orderItem.setCreatedDate(new Date());
				orderItem.setVersion(0L);
				orderItem.setSpecifications(gift.getSpecifications());
				orderItem.setProportion(null);
				orderItems.add(orderItem);
			}
			super.save(order);
			orderItemDao.saveBatch(orderItems);

			
			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.CREATE);
			orderLog.setOrder(order);
			orderLogService.save(orderLog);

			exchangePoint(order);
			if (Setting.StockAllocationTime.ORDER.equals(setting.getStockAllocationTime())
					|| (Setting.StockAllocationTime.PAYMENT.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
				allocateStock(order);
			}

			if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
				OrderPayment orderPayment = new OrderPayment();
				orderPayment.setMethod(OrderPayment.Method.DEPOSIT);
				orderPayment.setFee(BigDecimal.ZERO);
				orderPayment.setOrder(order);
				BigDecimal currentAmount = balance.compareTo(order.getAmount()) > 0 ? order.getAmount() : balance;
				orderPayment.setAmount(currentAmount);
				orderPayment.setPaymentMethod("积分卡|0306");
				orderPayment.setPayer(String.valueOf(order.getMember().getId()));
				balance = balance.subtract(currentAmount);
				payment(order, orderPayment);
			}

			//等待审核，上传到ERP
			if(order.getStatus().equals(Order.Status.PENDING_REVIEW)){
				if(order.getAmount().compareTo(order.getAmountPaid())==0){
					erpSyncService.uploadOrder(order.getSn());
				}else{
					order.setStatus(Order.Status.PENDING_PAYMENT);
					super.update(order);
				}
			}

			orders.add(order);

		}

		if (!cart.isNew()) {

			Set<CartItem> cartItemSet=cartItemDao.findSet("cart_id",cart.getId());
			boolean  f=false;
			for (CartItem s:cartItemSet){
				if (s.getIsBuy()==1){
					cartItemDao.remove(s);
				}else{
					f=true;
				}
			}
			if(f==false){
				cartDao.remove(cart);
			}
		}
		return orders;
	}

	@Override
	public void modify(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(!order.hasExpired() && (Order.Status.PENDING_PAYMENT.equals(order.getStatus()) || Order.Status.PENDING_REVIEW.equals(order.getStatus())), "[Assertion failed] - order must not be expired and order status must be PENDING_PAYMENT, or order status must be PENDING_REVIEW");

		order.setAmount(calculateAmount(order));
		if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(Order.Status.PENDING_REVIEW);
			order.setExpire(null);
		} else {
			if (order.getPaymentMethod() != null && PaymentMethod.Type.DELIVERY_AGAINST_PAYMENT.equals(order.getPaymentMethod().getType())) {
				order.setStatus(Order.Status.PENDING_PAYMENT);
			} else {
				order.setStatus(Order.Status.PENDING_REVIEW);
				order.setExpire(null);
			}
		}
		if (order.getArea() != null) {
			order.setAreaName(order.getArea().getFullName());
		}
		if (order.getPaymentMethod() != null) {
			order.setPaymentMethodName(order.getPaymentMethod().getName());
			order.setPaymentMethodType(order.getPaymentMethod().getType());
		}
		if (order.getShippingMethod() != null) {
			order.setShippingMethodName(order.getShippingMethod().getName());
		}
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.MODIFY);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	public void cancel(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(!order.hasExpired() && (Order.Status.PENDING_PAYMENT.equals(order.getStatus()) || Order.Status.PENDING_REVIEW.equals(order.getStatus())), "[Assertion failed] - order must not be expired and order status must be PENDING_PAYMENT, or order status must be PENDING_REVIEW");

		order.setStatus(Order.Status.CANCELED);
		order.setExpire(null);
		super.update(order);

		undoExchangePoint(order);
		releaseAllocatedStock(order);

		if (order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0) {
//			businessService.addBalance(order.getStore().getBusiness(), order.getRefundableAmount(), BusinessDepositLog.Type.ORDER_REFUNDS, null);
//		 	businessService.addMemberBalance(order.getMember(),order.getRefundableAmount());
			expiredRefund(order);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.CANCEL);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	public void review(Order order, boolean passed) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(!order.hasExpired() && Order.Status.PENDING_REVIEW.equals(order.getStatus()), "[Assertion failed] - order must not be expired and order status must be PENDING_REVIEW");

		if (passed) {
			order.setStatus(Order.Status.PENDING_SHIPMENT);
		} else {
			order.setStatus(Order.Status.DENIED);

			if (order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0) {
				businessService.addBalance(order.getStore().getBusiness(), order.getRefundableAmount(), BusinessDepositLog.Type.ORDER_REFUNDS, null);
			}
			undoExchangePoint(order);
			releaseAllocatedStock(order);
		}
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.REVIEW);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	public void payment(Order order, OrderPayment orderPayment) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.notNull(orderPayment, "[Assertion failed] - orderPayment is required; it must not be null");
		Assert.isTrue(orderPayment.isNew(), "[Assertion failed] - orderPayment must be new");
		Assert.notNull(orderPayment.getAmount(), "[Assertion failed] - orderPayment amount is required; it must not be null");
		Assert.state(orderPayment.getAmount().compareTo(BigDecimal.ZERO) > 0, "[Assertion failed] - orderPayment amount must be greater than 0");

		orderPayment.setSn(snService.generate(Sn.Type.ORDER_PAYMENT));
		orderPayment.setOrder(order);
		orderPaymentService.save(orderPayment);

		if (order.getMember() != null && OrderPayment.Method.DEPOSIT.equals(orderPayment.getMethod())) {
			memberService.addSnBalance(order, orderPayment.getEffectiveAmount().negate(), MemberDepositLog.Type.ORDER_PAYMENT, null);
		}

		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.PAYMENT.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		order.setAmountPaid(order.getAmountPaid().add(orderPayment.getEffectiveAmount()));
		order.setFee(order.getFee().add(orderPayment.getFee()));
		if (!order.hasExpired() && Order.Status.PENDING_PAYMENT.equals(order.getStatus()) && order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(Order.Status.PENDING_REVIEW);
			order.setExpire(null);
		}
		super.update(order);
		
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.PAYMENT);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	public void refunds(Order order, OrderRefunds orderRefunds) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0 || order.getIsAllowRefund(), "[Assertion failed] - order refundableAmount must be greater than 0 or order must allow refund");
		Assert.notNull(orderRefunds, "[Assertion failed] - orderRefunds is required; it must not be null");
		Assert.isTrue(orderRefunds.isNew(), "[Assertion failed] - orderRefunds must be new");
		Assert.state(order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || orderRefunds.getAmount().compareTo(order.getAmountPaid()) <= 0, "[Assertion failed] - order amountPaid must be greater than 0 or orderRefunds must be equal or less than 0");
		Assert.notNull(orderRefunds.getAmount(), "[Assertion failed] - orderRefunds amount is required; it must not be null");
		Assert.state(orderRefunds.getAmount().compareTo(BigDecimal.ZERO) > 0 && (orderRefunds.getAmount().compareTo(order.getRefundableAmount()) <= 0 || order.getIsAllowRefund()),
				"[Assertion failed] - orderRefunds must be greater than 0 and order refundableAmount must be equal or less than refundableAmount or order must allow refund");
		Assert.state(!OrderRefunds.Method.DEPOSIT.equals(orderRefunds.getMethod()) || order.getStore().getBusiness().getBalance().compareTo(orderRefunds.getAmount()) >= 0, "[Assertion failed] - orderRefunds method must not be DEPOSIT or business balance must equal or greater than orderRefunds");

		orderRefunds.setSn(snService.generate(Sn.Type.ORDER_REFUNDS));
		orderRefunds.setOrder(order);
		orderRefundsService.save(orderRefunds);

		if (OrderRefunds.Method.DEPOSIT.equals(orderRefunds.getMethod())) {
			memberService.addBalance(order.getMember(), orderRefunds.getAmount(), MemberDepositLog.Type.ORDER_REFUNDS, null);
			businessService.addBalance(order.getStore().getBusiness(), orderRefunds.getAmount().negate(), BusinessDepositLog.Type.ORDER_REFUNDS, null);
		}

		order.setAmountPaid(order.getAmountPaid().subtract(orderRefunds.getAmount()));
		order.setRefundAmount(order.getRefundAmount().add(orderRefunds.getAmount()));
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.REFUNDS);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	public void shipping(Order order, OrderShipping orderShipping) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
//		Assert.state(order.getShippableQuantity() > 0, "[Assertion failed] - order shippableQuantity must be greater than 0");
		Assert.notNull(orderShipping, "[Assertion failed] - orderShipping is required; it must not be null");
		Assert.isTrue(orderShipping.isNew(), "[Assertion failed] - orderShipping must be new");
//		Assert.notEmpty(orderShipping.getOrderShippingItems(), "[Assertion failed] - orderShippingItems must not be empty: it must contain at least 1 element");

		orderShipping.setSn(snService.generate(Sn.Type.ORDER_SHIPPING));
		orderShipping.setOrder(order);
		orderShippingService.save(orderShipping);
		if(orderShipping.getOrderShippingItems().size()>0){
			orderShippingItemDao.saveBatch(orderShipping.getOrderShippingItems());
			for (OrderShippingItem orderShippingItem : orderShipping.getOrderShippingItems()) {
				OrderItem orderItem = order.getOrderItem(orderShippingItem.getSn());
				if (orderItem == null || orderShippingItem.getQuantity() > orderItem.getShippableQuantity()) {
					throw new IllegalArgumentException();
				}
				orderItem.setShippedQuantity(orderItem.getShippedQuantity() + orderShippingItem.getQuantity());
				orderItemDao.update(orderItem);
				Sku sku = orderShippingItem.getSku();
				if (sku != null) {
//					if (orderShippingItem.getQuantity() > sku.getStock()) {
//						throw new IllegalArgumentException();
//					}
					skuService.addStock(sku, -orderShippingItem.getQuantity(), StockLog.Type.STOCK_OUT, null);
					if (BooleanUtils.isTrue(order.getIsAllocatedStock())) {
						skuService.addAllocatedStock(sku, -orderShippingItem.getQuantity());
					}
				}
			}
		}
		
		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.SHIP.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}


		order.setShippedQuantity(order.getShippedQuantity() + orderShipping.getQuantity());
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setStatus(Order.Status.SHIPPED);
			order.setIsAllocatedStock(false);
		}
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.SHIPPING);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	public void returns(Order order, OrderReturns orderReturns) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(order.getReturnableQuantity() > 0 || order.getIsAllowReturns(), "[Assertion failed] - order returnableQuantity must be greater than 0 or order must allow returns");
		Assert.notNull(orderReturns, "[Assertion failed] - orderReturns is required; it must not be null");
		Assert.isTrue(orderReturns.isNew(), "[Assertion failed] - orderReturns must not be new");
		Assert.notEmpty(orderReturns.getOrderReturnsItems(), "[Assertion failed] - orderReturnsItems must not be empty: it must contain at least 1 element");

		orderReturns.setSn(snService.generate(Sn.Type.ORDER_RETURNS));
		orderReturns.setOrder(order);
		orderReturnsService.save(orderReturns);

		for (OrderReturnsItem orderReturnsItem : orderReturns.getOrderReturnsItems()) {
			OrderItem orderItem = order.getOrderItem(orderReturnsItem.getSn());
//			if (orderItem == null || orderItem.getSku() == null || orderReturnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
//				throw new IllegalArgumentException();
//			}
			skuService.addStock(orderItem.getSku(), orderReturnsItem.getQuantity(), StockLog.Type.STOCK_IN, null);
			orderItem.setReturnedQuantity(orderItem.getReturnedQuantity() + orderReturnsItem.getQuantity());
		}

		order.setReturnedQuantity(order.getReturnedQuantity() + orderReturns.getQuantity());
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.RETURNS);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	public void receive(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(!order.hasExpired() && Order.Status.SHIPPED.equals(order.getStatus()), "[Assertion failed] - order must not be expired and order status must be SHIPPED");

		order.setStatus(Order.Status.RECEIVED);
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.RECEIVE);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void complete(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(!order.hasExpired() && Order.Status.RECEIVED.equals(order.getStatus()), "[Assertion failed] - order must not be expired and order status must be RECEIVED");

		Member member = order.getMember();
		if (order.getRewardPoint() > 0) {
			memberService.addPoint(member, order.getRewardPoint(), PointLog.Type.REWARD, null);
		}

		if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
			memberService.addAmount(member, order.getAmountPaid());
		}
		BigDecimal grantedDistributionCommissionTotals = BigDecimal.ZERO;
		BigDecimal orderDistributionCommission = order.getDistributionCommission();
		if (orderDistributionCommission.compareTo(BigDecimal.ZERO) > 0) {
			Setting setting = SystemUtils.getSetting();
			Distributor distributor = member.getDistributor();
			Double[] distributionCommissionRates = setting.getDistributionCommissionRates();
			if (distributor != null && ArrayUtils.isNotEmpty(distributionCommissionRates)) {
				for (double distributionCommissionRate : distributionCommissionRates) {
					Distributor parentDistributor = distributor.getParent();
					if (parentDistributor != null && distributionCommissionRate > 0) {
						BigDecimal amount = orderDistributionCommission.multiply(BigDecimal.valueOf(distributionCommissionRate).movePointLeft(2));
						memberService.addBalance(parentDistributor.getMember(), amount, MemberDepositLog.Type.DISTRIBUTION_COMMISSION, null);
						grantedDistributionCommissionTotals = grantedDistributionCommissionTotals.add(amount);
						distributor = parentDistributor;

						DistributionCommission distributionCommission = new DistributionCommission();
						distributionCommission.setAmount(amount);
						distributionCommission.setOrder(order);
						distributionCommission.setDistributor(distributor);
						distributionCommissionService.save(distributionCommission);
					}
				}
			}
		}
		BigDecimal settlementAmount = order.getSettlementAmount(grantedDistributionCommissionTotals);
		if (settlementAmount.compareTo(BigDecimal.ZERO) > 0) {
			businessService.addBalance(order.getStore().getBusiness(), settlementAmount, BusinessDepositLog.Type.ORDER_SETTLEMENT, null);
		}
		for (OrderItem orderItem : order.getOrderItems()) {
			Sku sku = orderItem.getSku();
			if (sku != null && sku.getProduct() != null) {
				productService.addSales(sku.getProduct(), orderItem.getQuantity());
			}
		}

		order.setStatus(Order.Status.COMPLETED);
		order.setCompleteDate(new Date());
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.COMPLETE);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

//		mailService.sendCompleteOrderMail(order);
//		smsService.sendCompleteOrderSms(order);
	}

	@Override
	public void fail(Order order) {
		Assert.notNull(order, "[Assertion failed] - order is required; it must not be null");
		Assert.isTrue(!order.isNew(), "[Assertion failed] - order must not be new");
		Assert.state(!order.hasExpired() && (Order.Status.PENDING_SHIPMENT.equals(order.getStatus()) || Order.Status.SHIPPED.equals(order.getStatus()) || Order.Status.RECEIVED.equals(order.getStatus())),
				"[Assertion failed] - order must not be expired and order status must be PENDING_SHIPMENT, SHIPPED or RECEIVED");

		order.setStatus(Order.Status.FAILED);

		undoExchangePoint(order);
		releaseAllocatedStock(order);

		if (order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0) {
			businessService.addBalance(order.getStore().getBusiness(), order.getRefundableAmount(), BusinessDepositLog.Type.ORDER_REFUNDS, null);
		}
		super.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.FAIL);
		orderLog.setOrder(order);
		orderLogService.save(orderLog);

	}

	@Override
	@Transactional
	public void delete(Order order) {
		if (order != null && !Order.Status.COMPLETED.equals(order.getStatus())) {
			undoExchangePoint(order);
			releaseAllocatedStock(order);
		}

		super.delete(order);
	}

	/**
	 * 积分兑换
	 * 
	 * @param order
	 *            订单
	 */
	private void exchangePoint(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		memberService.addPoint(order.getMember(), -order.getExchangePoint(), PointLog.Type.EXCHANGE, null);
		order.setIsExchangePoint(true);
	}

	/**
	 * 积分兑换撤销
	 * 
	 * @param order
	 *            订单
	 */
	private void undoExchangePoint(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		memberService.addPoint(order.getMember(), order.getExchangePoint(), PointLog.Type.UNDO_EXCHANGE, null);
		order.setIsExchangePoint(false);
		super.update(order);
	}

	/**
	 * 分配库存
	 * 
	 * @param order
	 *            订单
	 */
	private void allocateStock(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsAllocatedStock())) {
			return;
		}
		if (order.getOrderItems() != null) {
			for (OrderItem orderItem : order.getOrderItems()) {
				Sku sku = orderItem.getSku();
				if (sku != null) {
					skuService.addAllocatedStock(sku, orderItem.getQuantity() - orderItem.getShippedQuantity());
				}
			}
		}
		order.setIsAllocatedStock(true);
	}

	/**
	 * 释放已分配库存
	 * 
	 * @param order
	 *            订单
	 */
	private void releaseAllocatedStock(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsAllocatedStock())) {
			return;
		}
		if (order.getOrderItems() != null) {
			for (OrderItem orderItem : order.getOrderItems()) {
				Sku sku = orderItem.getSku();
				if (sku != null) {
					skuService.addAllocatedStock(sku, -(orderItem.getQuantity() - orderItem.getShippedQuantity()));
				}
			}
		}
		order.setIsAllocatedStock(false);
		super.update(order);
	}

	@Override
	@Transactional(readOnly = true)
	public Long completeOrderCount(Store store, Date beginDate, Date endDate) {
		return orderDao.completeOrderCount(store, beginDate, endDate);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal completeOrderAmount(Store store, Date beginDate, Date endDate) {
		return orderDao.completeOrderAmount(store, beginDate, endDate);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal grantedCommissionTotalAmount(Store store, CommissionType commissionType, Date beginDate, Date endDate, Status... statuses) {
		return orderDao.grantedCommissionTotalAmount(store, commissionType, beginDate, endDate, statuses);
	}



	@Override
	public void automaticReceive() {
		Date currentTime = new Date();
		for (int i = 0;; i += 100) {
			QueryWrapper<Order> queryWrapper = createQueryWrapper(i, 100, null, null);
			List<Order> orders = orderDao.findList(queryWrapper, null, Order.Status.SHIPPED, null, null, null, null, null, null, null, false,null);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					OrderShipping orderShipping = orderShippingDao.findLast(order);
					Date automaticReceiveTime = DateUtils.addDays(orderShipping.getCreatedDate(), SystemUtils.getSetting().getAutomaticReceiveTime());
					if (automaticReceiveTime.compareTo(currentTime) < 0) {
						order.setStatus(Order.Status.RECEIVED);
						super.update(order);
					}
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	@Override
	public void calculationOrder(Cart cart,Long orderId) {
		Order order=orderDao.find(orderId);
		order.setPrice(cart.getPrice(order.getStore()));
		orderDao.update(order);
		//变量，amount，price，促销是否可变
		Set<CartItem> cartItemSet=cartItemDao.findSet("cart_id",cart.getId());
		for (CartItem s:cartItemSet){
			cartItemDao.remove(s);
		}
		cartDao.remove(cart);
	}

	@Override
	public Set<Order> findListByUserId(Member member) {
		return orderDao.findSet("member_id",member.getId());
	}
}
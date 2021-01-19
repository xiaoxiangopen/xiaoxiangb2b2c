package net.xiaoxiangshop.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.Order.CommissionType;

/**
 * Service - 订单
 * 
 */
public interface OrderService extends BaseService<Order> {

	/**
	 * 根据编号查找订单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	Order findBySn(String sn);

	/**
	 * 查找订单
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 订单
	 */
	List<Order> findList(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
			List<net.xiaoxiangshop.Order> orders,Long isRefund);

	/**
	 * 查找订单分页
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	Page<Order> findPage(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds,  Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable);

	/**
	 * 查询订单数量
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @return 订单数量
	 */
	Long count(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired);

	/**
	 * 查询订单数量
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param storeId
	 *            店铺ID
	 * @param memberId
	 *            会员ID
	 * @param productId
	 *            商品ID
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @return 订单数量
	 */
	Long count(Order.Type type, Order.Status status, Long storeId, Long memberId, Long productId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired);

	/**
	 * 计算税金
	 * 
	 * @param price
	 *            SKU价格
	 * @param offsetAmount
	 *            调整金额
	 * @return 税金
	 */
	BigDecimal calculateTax(BigDecimal price,  BigDecimal offsetAmount);

	/**
	 * 计算税金
	 * 
	 * @param order
	 *            订单
	 * @return 税金
	 */
	BigDecimal calculateTax(Order order);

	/**
	 * 计算订单金额
	 * 
	 * @param price
	 *            SKU价格
	 * @param fee
	 *            支付手续费
	 * @param freight
	 *            运费
	 * @param tax
	 *            税金

	 * @param offsetAmount
	 *            调整金额
	 * @return 订单金额
	 */
	BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount);

	/**
	 * 计算订单金额
	 * 
	 * @param order
	 *            订单
	 * @return 订单金额
	 */
	BigDecimal calculateAmount(Order order);

	/**
	 * 指定用户获取订单锁
	 * 
	 * @param order
	 *            订单
	 * @param user
	 *            用户
	 * @return 是否获取成功
	 */
	boolean acquireLock(Order order, User user);

	/**
	 * 当前登录用户获取订单锁
	 * 
	 * @param order
	 *            订单
	 * @return 是否获取成功
	 */
	boolean acquireLock(Order order);

	/**
	 * 释放订单锁
	 * 
	 * @param order
	 *            订单
	 */
	void releaseLock(Order order);

	/**
	 * 过期订单退款处理
	 */
	void expiredRefundHandle();

	/**
	 * 过期订单退款
	 * 
	 * @param order
	 *            订单
	 */
	void expiredRefund(Order order);

	/**
	 * 过期订单积分兑换撤销
	 */
	void undoExpiredExchangePoint();

	/**
	 * 释放过期订单已分配库存
	 */
	void releaseExpiredAllocatedStock();

	/**
	 * 自动收货
	 */
	void automaticReceive();

	/**
	 * 订单生成
	 * 
	 * @param type
	 *            类型
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param paymentMethod
	 *            支付方式
	 * @param shippingMethod
	 *            配送方式
	 * @param invoice
	 *            发票
	 * @param balance
	 *            使用余额
	 * @param memo
	 *            附言
	 * @return 订单
	 */
	List<Order> generate(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Invoice invoice, BigDecimal balance, String memo);

	/**
	 * 订单创建
	 * 
	 * @param type
	 *            类型
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param paymentMethod
	 *            支付方式
	 * @param shippingMethod
	 *            配送方式
	 * @param invoice
	 *            发票
	 * @param balance
	 *            使用余额
	 * @param memo
	 *            附言
	 * @return 订单
	 */
	List<Order> create(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Invoice invoice, BigDecimal balance, String memo,String dataSource);

	/**
	 * 订单更新
	 * 
	 * @param order
	 *            订单
	 */
	void modify(Order order);

	/**
	 * 订单取消
	 * 
	 * @param order
	 *            订单
	 */
	void cancel(Order order);

	/**
	 * 订单审核
	 * 
	 * @param order
	 *            订单
	 * @param passed
	 *            是否审核通过
	 */
	void review(Order order, boolean passed);

	/**
	 * 订单收款
	 * 
	 * @param order
	 *            订单
	 * @param orderPayment
	 *            订单支付
	 */
	void payment(Order order, OrderPayment orderPayment);

	/**
	 * 订单退款
	 * 
	 * @param order
	 *            订单
	 * @param orderRefunds
	 *            订单退款
	 */
	void refunds(Order order, OrderRefunds orderRefunds);

	/**
	 * 订单发货
	 * 
	 * @param order
	 *            订单
	 * @param orderShipping
	 *            订单发货
	 */
	void shipping(Order order, OrderShipping orderShipping);

	/**
	 * 订单退货
	 * 
	 * @param order
	 *            订单
	 * @param orderReturns
	 *            订单退货
	 */
	void returns(Order order, OrderReturns orderReturns);

	/**
	 * 订单收货
	 * 
	 * @param order
	 *            订单
	 */
	void receive(Order order);

	/**
	 * 订单完成
	 * 
	 * @param order
	 *            订单
	 */
	void complete(Order order);

	/**
	 * 订单失败
	 * 
	 * @param order
	 *            订单
	 */
	void fail(Order order);

	/**
	 * 订单完成数量
	 * 
	 * @param store
	 *            店铺
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单完成数量
	 */
	Long completeOrderCount(Store store, Date beginDate, Date endDate);

	/**
	 * 订单完成金额
	 * 
	 * @param store
	 *            店铺
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单完成金额
	 */
	BigDecimal completeOrderAmount(Store store, Date beginDate, Date endDate);

	/**
	 * 查询已发放佣金总额
	 * 
	 * @param store
	 *            店铺
	 * @param commissionType
	 *            佣金类型
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param statuses
	 *            订单状态
	 * @return 已发放佣金总额
	 */
	BigDecimal grantedCommissionTotalAmount(Store store, CommissionType commissionType, Date beginDate, Date endDate, Order.Status... statuses);

    void calculationOrder(Cart cart,Long orderId);

	Set<Order> findListByUserId(Member member);


}
package net.xiaoxiangshop.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.xiaoxiangshop.entity.PaymentItem;
import net.xiaoxiangshop.entity.PaymentTransaction;
import net.xiaoxiangshop.plugin.PaymentPlugin;

/**
 * Service - 支付事务
 * 
 */
public interface PaymentTransactionService extends BaseService<PaymentTransaction> {

	/**
	 * 根据编号查找支付事务
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 支付事务，若不存在则返回null
	 */
	PaymentTransaction findBySn(String sn);

	/**
	 * 生成支付事务
	 * 
	 * @param lineItem
	 *            支付明细
	 * @param paymentPlugin
	 *            支付插件
	 * @param rePayUrl
	 *            重新支付URL
	 * @return 支付事务
	 */
	PaymentTransaction generate(PaymentTransaction.LineItem lineItem, PaymentPlugin paymentPlugin, String rePayUrl,String tnt);

	/**
	 * 生成父支付事务
	 * 
	 * @param lineItems
	 *            支付明细
	 * @param paymentPlugin
	 *            支付插件
	 * @param rePayUrl
	 *            重新支付URL
	 * @return 父支付事务
	 */
	PaymentTransaction generateParent(Collection<PaymentTransaction.LineItem> lineItems, PaymentPlugin paymentPlugin, String rePayUrl,String tnt_no);

	/**
	 * 支付处理
	 * 
	 * @param paymentTransaction
	 *            支付事务
	 */
	void handle(PaymentTransaction paymentTransaction);

	/**
	 * 生成支付明细
	 * 
	 * @param paymentItem
	 *            支付项
	 * @return 支付明细
	 */
	PaymentTransaction.LineItem generate(PaymentItem paymentItem);


	PaymentTransaction findByOrders(Long orderId);

	Set<PaymentTransaction> getListByTntNo(String tnt_no);
}
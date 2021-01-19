package net.xiaoxiangshop.service;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.BusinessCash;

/**
 * Service - 商家提现
 * 
 */
public interface BusinessCashService extends BaseService<BusinessCash> {

	/**
	 * 申请商家提现
	 * 
	 * @param businessCash
	 *            商家提现
	 * @param business
	 *            商家
	 */
	void applyCash(BusinessCash businessCash, Business business);

	/**
	 * 查找商家提现记录分页
	 * 
	 * @param status
	 *            状态
	 * @param bank
	 *            收款银行
	 * @param account
	 *            收款账号
	 * @param business
	 *            商家
	 * @param pageable
	 *            分页信息
	 * @return 商家提现记录分页
	 */
	Page<BusinessCash> findPage(BusinessCash.Status status, String bank, String account, Business business, Pageable pageable);

	/**
	 * 审核商家提现
	 * 
	 * @param businessCash
	 *            商家提现
	 * @param isPassed
	 *            是否审核通过
	 */
	void review(BusinessCash businessCash, Boolean isPassed);

	/**
	 * 查找商家提现数量
	 * 
	 * @param business
	 *            商家
	 * @param status
	 *            状态
	 * @param bank
	 *            收款银行
	 * @param account
	 *            收款账号
	 * @return 商家提现数量
	 */
	Long count(Business business, BusinessCash.Status status, String bank, String account);

	/**
	 * 查找商家提现数量
	 * 
	 * @param businessId
	 *            商家ID
	 * @param status
	 *            状态
	 * @param bank
	 *            收款银行
	 * @param account
	 *            收款账号
	 * @return 商家提现数量
	 */
	Long count(Long businessId, BusinessCash.Status status, String bank, String account);

}
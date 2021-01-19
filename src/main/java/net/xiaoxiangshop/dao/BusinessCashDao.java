package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.BusinessCash;

/**
 * Dao - 商家提现
 * 
 */
public interface BusinessCashDao extends BaseDao<BusinessCash> {

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
	List<BusinessCash> findPage(IPage<BusinessCash> iPage, @Param("ew")QueryWrapper<BusinessCash> queryWrapper, @Param("status")BusinessCash.Status status, @Param("bank")String bank, @Param("account")String account, @Param("business")Business business);

	/**
	 * 查找商家提现数量
	 * 
	 * @param status
	 *            状态
	 * @param bank
	 *            收款银行
	 * @param account
	 *            收款账号
	 * @param business
	 *            商家
	 * @return 商家提现数量
	 */
	Long count(@Param("status")BusinessCash.Status status, @Param("bank")String bank, @Param("account")String account, @Param("business")Business business);

}
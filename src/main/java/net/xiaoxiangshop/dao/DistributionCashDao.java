package net.xiaoxiangshop.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.DistributionCash;
import net.xiaoxiangshop.entity.Distributor;

/**
 * Dao - 分销提现
 * 
 */
public interface DistributionCashDao extends BaseDao<DistributionCash> {

	/**
	 * 查找分销提现记录分页
	 * 
	 * @param status
	 *            状态
	 * @param bank
	 *            收款银行
	 * @param account
	 *            收款账号
	 * @param accountHolder
	 *            开户人
	 * @param distributor
	 *            分销员
	 * @param pageable
	 *            分页信息
	 * @return 分销提现记录分页
	 */
	List<DistributionCash> findPage(IPage<DistributionCash> iPage, @Param("ew")QueryWrapper<DistributionCash> queryWrapper, @Param("status")DistributionCash.Status status, @Param("bank")String bank, @Param("account")String account, @Param("accountHolder")String accountHolder, @Param("distributor")Distributor distributor);

	/**
	 * 查询提现总额
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 提现总额
	 */
	BigDecimal cashTotalAmount(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 查找分销提现数量
	 * 
	 * @param status
	 *            状态
	 * @param bank
	 *            收款银行
	 * @param account
	 *            收款账号
	 * @param accountHolder
	 *            开户人
	 * @param distributor
	 *            分销员
	 * @return 分销提现数量
	 */
	Long count(@Param("status")DistributionCash.Status status, @Param("bank")String bank, @Param("account")String account, @Param("accountHolder")String accountHolder, @Param("distributor")Distributor distributor);

}
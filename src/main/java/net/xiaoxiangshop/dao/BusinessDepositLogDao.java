package net.xiaoxiangshop.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.entity.BusinessDepositLog;

/**
 * Dao - 商家预存款记录
 * 
 */
public interface BusinessDepositLogDao extends BaseDao<BusinessDepositLog> {

	/**
	 * 查找商家预存款记录分页
	 * 
	 * @param business
	 *            商家
	 * @param pageable
	 *            分页信息
	 * @return 商家预存款记录分页
	 */
	List<BusinessDepositLog> findPage(IPage<BusinessDepositLog> iPage, @Param("ew")QueryWrapper<BusinessDepositLog> queryWrapper, @Param("business")Business business);

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

}
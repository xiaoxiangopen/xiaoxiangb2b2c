package net.xiaoxiangshop.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.Business;

/**
 * Dao - 商家
 * 
 */
public interface BusinessDao extends BaseDao<Business> {

	/**
	 * 通过名称查找商家
	 * 
	 * @param keyword
	 *            关键词
	 * @param count
	 *            数量
	 * @return 商家
	 */
	List<Business> search(@Param("keyword")String keyword, @Param("count")Integer count);

	/**
	 * 查询商家数量
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 商家数量
	 */
	Long count(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 查询商家对象总数
	 * 
	 * @return 商家对象总数
	 */
	Long total();
	
	/**
	 * 查询总余额
	 * 
	 * @return 总余额
	 */
	BigDecimal totalBalance();

	/**
	 * 查询冻结总额
	 * 
	 * @return 冻结总额
	 */
	BigDecimal frozenTotalAmount();
	
	/**
	 * 删除实体对象
	 * 
	 * @param id
	 *            ID
	 */
	void delete(Long id);

	void addMemberBalance(@Param("business")Business business);


}
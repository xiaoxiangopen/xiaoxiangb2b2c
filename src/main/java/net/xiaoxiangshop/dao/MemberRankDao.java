package net.xiaoxiangshop.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.MemberRank;

/**
 * Dao - 会员等级
 * 
 */
public interface MemberRankDao extends BaseDao<MemberRank> {

	/**
	 * 查找默认会员等级
	 * 
	 * @return 默认会员等级，若不存在则返回null
	 */
	MemberRank findDefault();

	/**
	 * 根据消费金额查找符合此条件的最高会员等级
	 * 
	 * @param amount
	 *            消费金额
	 * @return 会员等级，不包含特殊会员等级，若不存在则返回null
	 */
	MemberRank findByAmount(@Param("amount")BigDecimal amount);

	/**
	 * 清除默认
	 */
	void clearDefault();

	/**
	 * 清除默认
	 * 
	 * @param exclude
	 *            排除会员等级
	 */
	void clearDefaultExclude(@Param("exclude")MemberRank exclude);

}
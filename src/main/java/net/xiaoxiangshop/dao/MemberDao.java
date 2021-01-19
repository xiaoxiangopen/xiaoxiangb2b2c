package net.xiaoxiangshop.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Member;

/**
 * Dao - 会员
 * 
 */
public interface MemberDao extends BaseDao<Member> {

	/**
	 * 查找会员分页
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param pageable
	 *            分页信息
	 * @return 会员分页
	 */
	List<Member> findPage(IPage<Member> iPage, @Param("ew")QueryWrapper<Member> queryWrapper, @Param("rankingType")Member.RankingType rankingType);

	/**
	 * 查询会员数量
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 会员数量
	 */
	Long count(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);
	/**
	 * 查询会员数量
	 *
	 * @param source
	 *            数据来源
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 会员数量
	 */
	long statisticCount(Integer source,Date beginDate, Date endDate);
	/**
	 * 查询会员对象总数
	 * 
	 * @return 会员对象总数
	 */
	Long total();

	/**
	 * 通过名称查找会员
	 * 
	 * @param keyword
	 *            关键词
	 * @param excludes
	 *            排除会员
	 * @param count
	 *            数量
	 * @return 会员
	 */
	List<Member> search(@Param("keyword")String keyword, @Param("excludes")Set<Member> excludes, @Param("count")Integer count);

	/**
	 * 清除会员注册项值
	 * 
	 * @param memberAttribute
	 *            会员注册项
	 */
	void clearAttributeValue(@Param("propertyName")String propertyName);

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

	List<Member> findMembers(@Param("member")Member member);

	void removeMemberCard(@Param("vipCard")String vipCard);

	List<Member> findHistoryUser(@Param("member")Member member);

    List<Member> memberList(@Param("mobile")String mobile);
}
package net.xiaoxiangshop.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberDepositLog;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.PointLog;
import net.xiaoxiangshop.security.AuthenticationProvider;

/**
 * Service - 会员
 * 
 */
public interface MemberService extends IService<Member>, AuthenticationProvider {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	Member find(Long id);
	
	/**
	 * 查找实体对象集合
	 * 
	 * @param ids
	 *            ID
	 * @return 实体对象集合
	 */
	List<Member> findList(Long... ids);
	
	/**
	 * 删除实体对象
	 * 
	 * @param ids
	 *            ID
	 */
	void delete(Long... ids);
	
	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @param ignoreProperties
	 *            忽略属性
	 * @return 实体对象
	 */
	Member update(Member entity, String... ignoreProperties);
	
	/**
	 * 查找实体对象分页
	 * 
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	Page<Member> findPage(Pageable pageable);
	
	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 手机号判断用户名 手机号是否存在
	 * @param mobile
	 * @return
	 */
	boolean usernameMobileExists(String mobile);


	/**
	 * 根据用户名查找会员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByUsername(String username);

	/**
	 * 判断E-mail是否存在
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExists(String email);

	/**
	 * 判断E-mail是否唯一
	 * 
	 * @param id
	 *            ID
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否唯一
	 */
	boolean emailUnique(Long id, String email);

	/**
	 * 根据E-mail查找会员
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByEmail(String email);

	/**
	 * 判断手机是否存在
	 * 
	 * @param mobile
	 *            手机(忽略大小写)
	 * @return 手机是否存在
	 */
	boolean mobileExists(String mobile);

	/**
	 * 判断手机是否唯一
	 * 
	 * @param id
	 *            ID
	 * @param mobile
	 *            手机(忽略大小写)
	 * @return 手机是否唯一
	 */
	boolean mobileUnique(Long id, String mobile);

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
	List<Member> search(String keyword, Set<Member> excludes, Integer count);

	/**
	 * 根据手机查找会员
	 * 
	 * @param mobile
	 *            手机(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByMobile(String mobile);

	/**
	 * 查找会员分页
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param pageable
	 *            分页信息
	 * @return 会员分页
	 */
	Page<Member> findPage(Member.RankingType rankingType, Pageable pageable);

	/**
	 * 增加余额
	 * 
	 * @param member
	 *            会员
	 * @param amount
	 *            值
	 * @param type
	 *            类型
	 * @param memo
	 *            备注
	 */
	void addBalance(Member member, BigDecimal amount, MemberDepositLog.Type type, String memo);

	//余额支付增加卡号
	void addSnBalance(Order order, BigDecimal amount, MemberDepositLog.Type type, String memo);

	/**
	 * 增加冻结金额
	 * 
	 * @param member
	 *            会员
	 * @param amount
	 *            值
	 */
	void addFrozenAmount(Member member, BigDecimal amount);

	/**
	 * 增加积分
	 * 
	 * @param member
	 *            会员
	 * @param amount
	 *            值
	 * @param type
	 *            类型
	 * @param memo
	 *            备注
	 */
	void addPoint(Member member, long amount, PointLog.Type type, String memo);

	/**
	 * 增加消费金额
	 * 
	 * @param member
	 *            会员
	 * @param amount
	 *            值
	 */
	void addAmount(Member member, BigDecimal amount);

	/**
	 * 查询总余额
	 * 
	 * @return 总余额
	 */
	BigDecimal totalBalance();

	/**
	 * 查询会员数量
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 会员数量
	 */
	long count(Date beginDate, Date endDate);
	/**
	 * 查询会员数量
	 *
	 * @param source
	 *            来源
	 * @param beginDate
	 *            开始日期
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
	long total();


	/**
	 * 根据会员卡同步会员信息
	 * @param member
	 * @param vipcard
	 * @return
	 */
	Member synchroMember(Member member,String vipcard);

	List<Member> findMembers(Member member);

	/**
	 * 解绑已绑定的会员卡
	 * @param vipCard
	 */
	void removeMemberCard(String vipCard);

	List<Member> findHistoryUser(Member member);

	/**
	 * 系统自动添加会员
	 * @param mobile
	 * @return
	 */
//	void sysAddMember(String mobile);

	List<Member> memberList(String mobile);

    void updateMembers(Member member);
}
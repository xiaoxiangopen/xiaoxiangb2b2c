package net.xiaoxiangshop.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.inject.Inject;
import com.alibaba.fastjson.JSONObject;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.WebUtils;
import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.MemberDao;
import net.xiaoxiangshop.dao.MemberDepositLogDao;
import net.xiaoxiangshop.dao.MemberRankDao;
import net.xiaoxiangshop.dao.PointLogDao;

/**
 * Service - 会员
 * 
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

	/**
	 * E-mail身份配比
	 */
	private static final Pattern EMAIL_PRINCIPAL_PATTERN = Pattern.compile(".*@.*");

	/**
	 * 手机身份配比
	 *  Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$");
	 */
	private static final Pattern MOBILE_PRINCIPAL_PATTERN = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$");

	/**
	 * 更新忽略属性
	 */
	private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATED_DATE_PROPERTY_NAME, BaseEntity.LAST_MODIFIED_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };
	
	@Inject
	private MemberDao memberDao;
	@Inject
	private MemberRankDao memberRankDao;
	@Inject
	private MemberDepositLogDao memberDepositLogDao;
	@Inject
	private PointLogDao pointLogDao;

	@Inject
	private MemberService memberService;
	@Inject
	private ReceiverService receiverService;
	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private OrderService orderService;
	@Inject
	private OrderPlusService orderPlusService;
	@Inject
	private SocialUserService socialUserService;

	/**
	 * ERP中台接口URL
	 */
	@Value("${erp_basic_url}")
	private String erp_basic_url;
	@Override
	public Member find(Long id) {
		return memberDao.find(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Member> findList(Long... ids) {
		List<Member> result = new ArrayList<>();
		if (ids != null) {
			for (Long id : ids) {
				Member entity = this.find(id);
				if (entity != null) {
					result.add(entity);
				}
			}
		}
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Member getUser(Object principal) {
		Assert.notNull(principal, "[Assertion failed] - principal is required; it must not be null");
		Assert.isInstanceOf(String.class, principal);

		String value = String.valueOf(principal);
		if (EMAIL_PRINCIPAL_PATTERN.matcher(value).matches()) {
			return findByEmail(value);
		} else if (MOBILE_PRINCIPAL_PATTERN.matcher(value).matches()&&value.length()==11) {
			return findByMobile(value);
		} else {
			return findByUsername(value);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isInstanceOf(Member.class, user);

		return Member.PERMISSIONS;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean supports(Class<?> userClass) {
		return userClass != null && Member.class.isAssignableFrom(userClass);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return memberDao.exists("username", StringUtils.lowerCase(username));
	}
	@Override
	@Transactional(readOnly = true)
	public boolean usernameMobileExists(String mobile){
		Boolean exists=mobileExists(mobile);
		if(!exists){
			if(!usernameExists(mobile)){//判断用户名是存在
				return false;
			}
		}
		return true;
	}
	@Override
	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByAttribute("username", StringUtils.lowerCase(username));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return memberDao.exists("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailUnique(Long id, String email) {
		return memberDao.unique(id, "email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public Member findByEmail(String email) {
		return memberDao.findByAttribute("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean mobileExists(String mobile) {
		return memberDao.exists("mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean mobileUnique(Long id, String mobile) {
		return memberDao.unique(id, "mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> search(String keyword, Set<Member> excludes, Integer count) {
		if (StringUtils.isEmpty(keyword)) {
			return Collections.emptyList();
		}
		return memberDao.search(keyword, excludes, count);
	}

	@Override
	@Transactional(readOnly = true)
	public Member findByMobile(String mobile) {
		return memberDao.findByAttribute("mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Member> findPage(Member.RankingType rankingType, Pageable pageable) {
		IPage<Member> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<Member>(pageable.getPageNumber(), pageable.getPageSize());
		iPage.setRecords(memberDao.findPage(iPage, getPageable(pageable), rankingType));
		Page<Member> page = new Page<Member>(iPage.getRecords(), iPage.getTotal(), pageable);
		return page;
	}

	@Override
	public void addBalance(Member member, BigDecimal amount, MemberDepositLog.Type type, String memo) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		Assert.notNull(amount, "[Assertion failed] - amount is required; it must not be null");
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(member.getBalance(), "[Assertion failed] - member balance is required; it must not be null");
		Assert.state(member.getBalance().add(amount).compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - member balance must be equal or greater than 0");

		member.setBalance(member.getBalance().add(amount));
		memberDao.update(member);

		MemberDepositLog memberDepositLog = new MemberDepositLog();
		memberDepositLog.setId(IdWorker.getId());
		memberDepositLog.setVersion(0L);
		memberDepositLog.setCreatedDate(new Date());
		memberDepositLog.setType(type);
		memberDepositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		memberDepositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		memberDepositLog.setBalance(member.getBalance());
		memberDepositLog.setMemo(memo);
		memberDepositLog.setMember(member);
		memberDepositLogDao.save(memberDepositLog);
	}


	//积分卡付款记录订单信息
	@Override
	public void addSnBalance(net.xiaoxiangshop.entity.Order order, BigDecimal amount, MemberDepositLog.Type type, String memo) {
		Member member=order.getMember();
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		Assert.notNull(amount, "[Assertion failed] - amount is required; it must not be null");
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(member.getBalance(), "[Assertion failed] - member balance is required; it must not be null");
		Assert.state(member.getBalance().add(amount).compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - member balance must be equal or greater than 0");

		member.setBalance(member.getBalance().add(amount));
		memberDao.update(member);

		MemberDepositLog memberDepositLog = new MemberDepositLog();
		memberDepositLog.setId(IdWorker.getId());
		memberDepositLog.setVersion(0L);
		memberDepositLog.setCreatedDate(new Date());
		memberDepositLog.setType(type);
		memberDepositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		memberDepositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		memberDepositLog.setBalance(member.getBalance());
		memberDepositLog.setMemo(memo);
		memberDepositLog.setMember(member);
		memberDepositLog.setOrders(order.getId());
		memberDepositLogDao.save(memberDepositLog);
	}


	@Override
	public void addFrozenAmount(Member member, BigDecimal amount) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		Assert.notNull(amount, "[Assertion failed] - amount is required; it must not be null");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(member.getFrozenAmount(), "[Assertion failed] - member frozenAmount is required; it must not be null");
		Assert.state(member.getFrozenAmount().add(amount).compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - member frozenAmount must be equal or greater than 0");

		member.setFrozenAmount(member.getFrozenAmount().add(amount));
		memberDao.update(member);
	}

	@Override
	public void addPoint(Member member, long amount, PointLog.Type type, String memo) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		if (amount == 0) {
			return;
		}

		Assert.notNull(member.getPoint(), "[Assertion failed] - member point is required; it must not be null");
		Assert.state(member.getPoint() + amount >= 0, "[Assertion failed] - member point must be equal or greater than 0");

		member.setPoint(member.getPoint() + amount);
		memberDao.update(member);

		PointLog pointLog = new PointLog();
		pointLog.setId(IdWorker.getId());
		pointLog.setVersion(0L);
		pointLog.setCreatedDate(new Date());
		pointLog.setType(type);
		pointLog.setCredit(amount > 0 ? amount : 0L);
		pointLog.setDebit(amount < 0 ? Math.abs(amount) : 0L);
		pointLog.setBalance(member.getPoint());
		pointLog.setMemo(memo);
		pointLog.setMember(member);
		pointLogDao.save(pointLog);
	}

	@Override
	public void addAmount(Member member, BigDecimal amount) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		Assert.notNull(amount, "[Assertion failed] - amount is required; it must not be null");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		if (member.getAmount()==null){
			member.setAmount(BigDecimal.ZERO);
		}
		Assert.notNull(member.getAmount(), "[Assertion failed] - member amount is required; it must not be null");
		Assert.state(member.getAmount().add(amount).compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - member amount must be equal or greater than 0");
		member.setAmount(member.getAmount().add(amount));
		MemberRank memberRank = member.getMemberRank();
		if (memberRank != null && BooleanUtils.isFalse(memberRank.getIsSpecial())) {
			MemberRank newMemberRank = memberRankDao.findByAmount(member.getAmount());
			if (newMemberRank != null && newMemberRank.getAmount() != null && newMemberRank.getAmount().compareTo(memberRank.getAmount()) > 0) {
				member.setMemberRank(newMemberRank);
			}
		}
		memberDao.update(member);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Member> findPage(Pageable pageable) {
		IPage<Member> iPage = super.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<Member>(pageable.getPageNumber(), pageable.getPageSize()), getPageable(pageable));
		Page<Member> page = new Page<Member>(iPage.getRecords(), iPage.getTotal(), pageable);
		return page;
	}
	
	@Override
	@Transactional
	public boolean save(Member member) {
		Assert.notNull(member, "[Assertion failed] - member is required; it must not be null");
		member.setId(IdWorker.getId());
		member.setVersion(0L);
		member.setCreatedDate(new Date());
		return memberDao.save(member);
	}

	@Override
	@Transactional
	public Member update(Member entity, String... ignoreProperties) {
		Assert.notNull(entity, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(!entity.isNew(), "[Assertion failed] - entity must not be new");

		Member persistant = this.find(entity.getId());
		System.out.println("=================:"+persistant);
		if (persistant != null) {
			BeanUtils.copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			persistant.setLastModifiedDate(new Date());
			memberDao.update(persistant);
		}
		return entity;
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal totalBalance() {
		return memberDao.totalBalance();
	}

	@Override
	@Transactional(readOnly = true)
	public long count(Date beginDate, Date endDate) {
		return memberDao.count(beginDate, endDate);
	}
	@Override
	@Transactional(readOnly = true)
	public long statisticCount(Integer source,Date beginDate, Date endDate){
		return memberDao.statisticCount(source,beginDate, endDate);
	}
	@Override
	@Transactional(readOnly = true)
	public long total() {
		return memberDao.total();
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		memberDao.delete(Arrays.asList(ids));
	}
	
	/**
	 * 转换分页信息
	 * 
	 */
	protected QueryWrapper<Member> getPageable(Pageable pageable) {
		QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
		// 增加搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			//queryWrapper.and(wrapper -> wrapper.like(searchProperty, searchValue));
			queryWrapper.like(searchProperty, searchValue);
		}
		// 排序属性
		String orderProperty = net.xiaoxiangshop.util.StringUtils.camel2Underline(pageable.getOrderProperty());
		if (StringUtils.isNotEmpty(orderProperty)) {
			if (pageable.getOrderDirection().equals(Order.Direction.ASC)) {
				queryWrapper.orderByAsc(true, orderProperty);
			}
			if (pageable.getOrderDirection().equals(Order.Direction.DESC)) {
				queryWrapper.orderByDesc(true, orderProperty);
			}
		}
		// 筛选
		if (CollectionUtils.isNotEmpty(pageable.getFilters())) {
			queryWrapper = convertFilter(pageable.getFilters());
		}
		// 排序
		if (CollectionUtils.isNotEmpty(pageable.getOrders())) {
			queryWrapper = convertOrders(pageable.getOrders());
		}
		return queryWrapper;
	}
	
	/**
	 * 转换为Filter
	 * @param filters
	 * @return
	 */
	private QueryWrapper<Member> convertFilter(List<Filter> filters) {
		QueryWrapper<Member> queryWrapper = new QueryWrapper<Member>();
		if (CollectionUtils.isEmpty(filters)) {
			return queryWrapper;
		}
		for (Filter filter : filters) {
			if (filter == null) {
				continue;
			}
			String property = filter.getProperty();
			Filter.Operator operator = filter.getOperator();
			Object value = filter.getValue();
			Boolean ignoreCase = filter.getIgnoreCase();
			if (StringUtils.isEmpty(property)) {
				continue;
			}
			switch (operator) {
			case EQ:
				if (value != null) {
					if (BooleanUtils.isTrue(ignoreCase) && String.class.isAssignableFrom(property.getClass()) && value instanceof String) {
						queryWrapper.eq(property, ((String) value).toLowerCase());
					} else {
						queryWrapper.eq(property, value);
					}
				} else {
					queryWrapper.isNull(property);
				}
				break;
			case NE:
				if (value != null) {
					if (BooleanUtils.isTrue(ignoreCase) && String.class.isAssignableFrom(property.getClass()) && value instanceof String) {
						queryWrapper.ne(property, ((String) value).toLowerCase());
					} else {
						queryWrapper.ne(property, value);
					}
				} else {
					queryWrapper.isNotNull(property);
				}
				break;
			case GT:
				if (Number.class.isAssignableFrom(property.getClass()) && value instanceof Number) {
					queryWrapper.gt(property, (Number) value);
				}
				break;
			case LT:
				if (Number.class.isAssignableFrom(property.getClass()) && value instanceof Number) {
					queryWrapper.lt(property, (Number) value);
				}
				break;
			case GE:
				if (Number.class.isAssignableFrom(property.getClass()) && value instanceof Number) {
					queryWrapper.ge(property, (Number) value);
				}
				break;
			case LE:
				if (Number.class.isAssignableFrom(property.getClass()) && value instanceof Number) {
					queryWrapper.le(property, (Number) value);
				}
				break;
			case LIKE:
				if (String.class.isAssignableFrom(property.getClass()) && value instanceof String) {
					if (BooleanUtils.isTrue(ignoreCase)) {
						queryWrapper.like(property, ((String) value).toLowerCase());
					} else {
						queryWrapper.like(property, (String) value);
					}
				}
				break;
			case IN:
				queryWrapper.in(property, (String) value);
				break;
			case IS_NULL:
				queryWrapper.isNull(property);
				break;
			case IS_NOT_NULL:
				queryWrapper.isNotNull(property);
				break;
			}
		}
		return queryWrapper;
	}

	/**
	 * 转换为Order
	 * @param orders
	 * @return
	 */
	private QueryWrapper<Member> convertOrders(List<Order> orders) {
		QueryWrapper<Member> orderList = new QueryWrapper<Member>();
		if (CollectionUtils.isEmpty(orders)) {
			return orderList;
		}
		for (Order order : orders) {
			if (order == null) {
				continue;
			}
			String property = order.getProperty();
			Order.Direction direction = order.getDirection();
			if (StringUtils.isEmpty(property) || direction == null) {
				continue;
			}
			String[] columns = new String[] { property };
			switch (direction) {
			case ASC:
				orderList.orderByAsc(columns);
				break;
			case DESC:
				orderList.orderByDesc(columns);
				break;
			}
		}
		return orderList;
	}

	/**
	 * 根据会员卡同步会员信息
	 * @param member
	 * @param vipcard
	 * @return
	 */
	public Member synchroMember(Member member,String vipcard){
		if (null == member|| StringUtils.isEmpty(vipcard)) {
			return null;
		}
		HashMap hashMap = new HashMap();
		SimpleDateFormat formatter_log = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("method", "getvipinfo");
			jsonParam.put("vipid", vipcard);
			String param = jsonParam.toJSONString();
			hashMap.put("sendTime",formatter_log.format(new Date()));
			hashMap.put("sendText",param);
			hashMap.put("type","1");
			String retV = WebUtils.sendPost(erp_basic_url, param);
			hashMap.put("resultTime",formatter_log.format(new Date()));
			hashMap.put("resultText",retV);
			JSONObject obj = JSONObject.parseObject(retV);
			Integer code = obj.getInteger("code");
			hashMap.put("resultCode",code);

			try {
//                        erpResultService.add(hashMap);
			}catch (Exception ex)
			{
				ex.printStackTrace();
			}

			if (code == 0) {
				JSONObject obj_data = obj.getJSONObject("data");
				if(obj_data!=null){
					Integer sex = obj_data.getInteger("xb");
					String apoints = obj_data.getString("apoints");
					String points = obj_data.getString("points");
					String hyjb = obj_data.getString("hyjb");
					//会员卡号
					String hykh = obj_data.getString("hykh");
					String mobile = obj_data.getString("sjhm");
					BigDecimal bigDecimal_apoints =new BigDecimal(apoints);
					BigDecimal bigDecimal_points =new BigDecimal(points);
					Long rank = 1l;
					switch(hyjb)
					{
						case "10":
							rank = 2l;
							break;
						case "30":
							rank = 3l;
							break;
						case "32":
							rank = 4l;
							break;
					}

					MemberRank memberRank = new MemberRank();
					memberRank.setId(rank);

					if (sex == 1)
						member.setGender(Member.Gender.FEMALE);
					else
						member.setGender(Member.Gender.MALE);

					member.setApoints(bigDecimal_apoints);
					member.setPoints(bigDecimal_points);
					member.setMobile(mobile);
					member.setMemberRank(memberRank);
					member.setAttributeValue0(hykh);
				}else{
					return member;
				}


			}

		} catch (Exception e) {
			return member;
		}
		return member;
	}

	public void removeMemberCard(String vipCard){

	}

	@Override
	public List<Member> findHistoryUser(Member member) {
		return memberDao.findHistoryUser(member);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Member> findMembers(Member member) {
		return memberDao.findMembers(member);
	}

//	@Override
//	@Transactional(readOnly = true)
//	public void sysAddMember(String mobile){
//
//		if(!usernameExists(mobile)){
//			/**新增会员*/
//          Member member=new Member();
//		  MemberRank memberRank = new MemberRank();
//		  memberRank.setId(1L);
//		  member.setMemberRank(memberRank);
//		  member.setPoints(BigDecimal.ZERO);
//		  member.setMobile(mobile);
//		  //调用中台接口
//          synchroMember(member,mobile);
//		  addUser(member);
//		}
//
//	}



	@Override
	public List<Member> memberList(String mobile) {
		return memberDao.memberList(mobile);
	}

	@Override
	public void updateMembers(Member member) {

		if(member.getMobile()!=null){
			List<Member> memberList=memberService.memberList(member.getMobile());
			if(memberList!=null) {
				for (Member memberOld : memberList) {
					if(!memberOld.getId().equals(member.getId())){
						//更新用户信息
						//合并之前余额
						BigDecimal balancet=member.getBalance();
						//合并之前后余额
						BigDecimal balance=member.getBalance().add(memberOld.getBalance());
						member.setBalance(balance);
						member.setPoints(member.getPoints().add(memberOld.getPoints()));
						member.setUsername(memberOld.getUsername());
						member.setEmail(memberOld.getEmail());
						member.setEncodedPassword(memberOld.getEncodedPassword());

						//更新会员充值记录
						//新账户增加记录
						MemberDepositLog log=new MemberDepositLog();
						log.setBalance(balance);
						log.setCredit(memberOld.getBalance());
						log.setDebit(BigDecimal.ZERO);
						log.setMember(member);
						log.setMemo("账户合并,老账户ID："+memberOld.getId()+",新账户ID："+member.getId());
						log.setType(MemberDepositLog.Type.ADJUSTMENT);
						memberDepositLogService.save(log);

						//老账户扣减
						MemberDepositLog log1=new MemberDepositLog();
						log1.setBalance(BigDecimal.ZERO);
						log1.setCredit(BigDecimal.ZERO);
						log1.setDebit(memberOld.getBalance());
						log1.setMember(memberOld);
						log1.setMemo("账户合并,老账户ID："+memberOld.getId()+",新账户ID："+member.getId());
						log1.setType(MemberDepositLog.Type.ADJUSTMENT);
						memberDepositLogService.save(log1);
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String msg=sf.format(new Date()) +"用户ID："+memberOld.getId()+",向用户ID为："+member.getId()+"的账户支付余额："+memberOld.getBalance()+"，当前新用户支付之前余额"+balancet+",当前新用户的余额为："+balance;
						XmlUtils.appendMethodC(msg,"账户合并");

						//更新用户收货地址
						List<Receiver> receiverList = receiverService.findList(memberOld);
						if (receiverList != null) {
							for (Receiver receiver : receiverList) {
								receiver.setMember(member);
								receiver.setLastModifiedDate(new Date());
								receiverService.update(receiver);
							}
						}
						//更新充值记录
//						Set<MemberDepositLog> memberDepositLogList = memberDepositLogService.findListByUserId(memberOld);
//						if (memberDepositLogList != null) {
//							for (MemberDepositLog memberDepositLog : memberDepositLogList) {
//								memberDepositLog.setMember(member);
//								memberDepositLog.setLastModifiedDate(new Date());
//								memberDepositLogService.update(memberDepositLog);
//							}
//						}
						//更新订单
						Set<net.xiaoxiangshop.entity.Order> orderList = orderService.findListByUserId(memberOld);
						if (orderList != null) {
							for (net.xiaoxiangshop.entity.Order order : orderList) {
								order.setMember(member);
								order.setLastModifiedDate(new Date());
								orderService.update(order);
							}
						}
						//更新历史订单
						Set<OrderPlus> orderPluses = orderPlusService.findListByUserId(memberOld);
						if (orderPluses != null) {
							for (OrderPlus orderPlus : orderPluses) {
								orderPlus.setMember(member);
								orderPlus.setLastModifiedDate(new Date());
								orderPlusService.update(orderPlus);
							}
						}
						//更新微信扫码登录绑定关系
						Set<SocialUser> socialUserSet = socialUserService.finByUserId(memberOld.getId());
						if (socialUserSet != null) {
							for (SocialUser socialUser : socialUserSet) {
								socialUser.setUser(member);
								socialUser.setLastModifiedDate(new Date());
								socialUserService.update(socialUser);
							}
						}

						//注销当前重复的老用户
						memberOld.setIsEnabled(false);
						memberOld.setBalance(new BigDecimal(0));
						memberOld.setPoints(new BigDecimal(0));
						memberService.update(memberOld);
					}
				}
			}
		}
		memberService.update(member);
	}

}
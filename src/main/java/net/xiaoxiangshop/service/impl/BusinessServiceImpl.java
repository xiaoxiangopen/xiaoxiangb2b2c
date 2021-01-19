package net.xiaoxiangshop.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.xiaoxiangshop.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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
import net.xiaoxiangshop.dao.BusinessDao;
import net.xiaoxiangshop.dao.BusinessDepositLogDao;
import net.xiaoxiangshop.service.BusinessService;

/**
 * Service - 商家
 * 
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessDao, Business> implements BusinessService {

	/**
	 * 更新忽略属性
	 */
	private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATED_DATE_PROPERTY_NAME, BaseEntity.LAST_MODIFIED_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };

	/**
	 * E-mail身份配比
	 */
	private static final Pattern EMAIL_PRINCIPAL_PATTERN = Pattern.compile(".*@.*");

	/**
	 * 手机身份配比
	 */
	private static final Pattern MOBILE_PRINCIPAL_PATTERN = Pattern.compile("\\d+");

	@Inject
	private BusinessDao businessDao;
	@Inject
	private BusinessDepositLogDao businessDepositLogDao;
//	@Inject
//	private FreeMarkerConfigurer freeMarkerConfigurer;

	
	@Override
	@Transactional(readOnly = true)
	public Business find(Long id) {
		return businessDao.find(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Business> findAll() {
		return businessDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Business> findPage(Pageable pageable) {
		IPage<Business> iPage = super.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<Business>(pageable.getPageNumber(), pageable.getPageSize()), getPageable(pageable));
		Page<Business> page = new Page<Business>(iPage.getRecords(), iPage.getTotal(), pageable);
		return page;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Business getUser(Object principal) {
		Assert.notNull(principal, "[Assertion failed] - principal is required; it must not be null");
		Assert.isInstanceOf(String.class, principal);

		String value = String.valueOf(principal);
		if (EMAIL_PRINCIPAL_PATTERN.matcher(value).matches()) {
			return findByEmail(value);
		} else if (MOBILE_PRINCIPAL_PATTERN.matcher(value).matches()) {
			return findByMobile(value);
		} else {
			return findByUsername(value);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isInstanceOf(Business.class, user);

		Business business = (Business) user;
		Business pBusiness = businessDao.find(business.getId());
		Store store = pBusiness.getStore();
		return store != null && store.isActive() ? Business.NORMAL_BUSINESS_PERMISSIONS : Business.RESTRICT_BUSINESS_PERMISSIONS;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean supports(Class<?> userClass) {
		return userClass != null && Business.class.isAssignableFrom(userClass);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return businessDao.exists("username", StringUtils.lowerCase(username));
	}

	@Override
	@Transactional(readOnly = true)
	public Business findByUsername(String username) {
		return businessDao.findByAttribute("username", StringUtils.lowerCase(username));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return businessDao.exists("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailUnique(Long id, String email) {
		return businessDao.unique(id, "email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public Business findByEmail(String email) {
		return businessDao.findByAttribute("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean mobileExists(String mobile) {
		return businessDao.exists("mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean mobileUnique(Long id, String mobile) {
		return businessDao.unique(id, "mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public Business findByMobile(String mobile) {
		return businessDao.findByAttribute("mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Business> search(String keyword, Integer count) {
		return businessDao.search(keyword, count);
	}

	@Override
	@Transactional
	public boolean save(Business business) {
		Assert.notNull(business, "[Assertion failed] - business is required; it must not be null");
		
		business.setId(IdWorker.getId());
		business.setCreatedDate(new Date());
		business.setVersion(0L);
		return businessDao.save(business);
	}

	@Override
	@Transactional
	public Business update(Business business) {
		Assert.notNull(business, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(!business.isNew(), "[Assertion failed] - entity must not be new");

		Business persistant = this.find(business.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(business, persistant, UPDATE_IGNORE_PROPERTIES);
					
			persistant.setLastModifiedDate(new Date());
			businessDao.update(persistant);
		}
		return business;
	}

	@Override
	@Transactional
	public Business update(Business business, String... ignoreProperties) {
		Assert.notNull(business, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(!business.isNew(), "[Assertion failed] - entity must not be new");

		Business persistant = this.find(business.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(business, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			persistant.setLastModifiedDate(new Date());
			businessDao.update(persistant);
		}
		return business;
	}
	
	@Override
	public void addBalance(Business business, BigDecimal amount, BusinessDepositLog.Type type, String memo) {
		Assert.notNull(business, "[Assertion failed] - business is required; it must not be null");
		Assert.notNull(amount, "[Assertion failed] - amount is required; it must not be null");
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(business.getBalance(), "[Assertion failed] - business balance is required; it must not be null");
		Assert.state(business.getBalance().add(amount).compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - business balance must be equal or greater than 0");

		business.setBalance(business.getBalance().add(amount));
		businessDao.update(business);

		BusinessDepositLog businessDepositLog = new BusinessDepositLog();
		businessDepositLog.setType(type);
		businessDepositLog.setCredit(amount.compareTo(BigDecimal.ZERO) > 0 ? amount : BigDecimal.ZERO);
		businessDepositLog.setDebit(amount.compareTo(BigDecimal.ZERO) < 0 ? amount.abs() : BigDecimal.ZERO);
		businessDepositLog.setBalance(business.getBalance());
		businessDepositLog.setMemo(memo);
		businessDepositLog.setBusiness(business);
		businessDepositLog.setId(IdWorker.getId());
		businessDepositLog.setVersion(0L);
		businessDepositLog.setCreatedDate(new Date());
		businessDepositLogDao.save(businessDepositLog);
	}

	@Override
	public void addFrozenAmount(Business business, BigDecimal amount) {
		amount=amount.abs();
		Assert.notNull(business, "[Assertion failed] - business is required; it must not be null");
		Assert.notNull(amount, "[Assertion failed] - amount is required; it must not be null");

		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		Assert.notNull(business.getFrozenAmount(), "[Assertion failed] - business frozenAmount is required; it must not be null");
		Assert.state(business.getFrozenAmount().add(amount).compareTo(BigDecimal.ZERO) >= 0, "[Assertion failed] - business frozenAmount must be equal or greater than 0");

		business.setFrozenAmount(business.getFrozenAmount().add(amount));
		businessDao.update(business);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal totalBalance() {
		return businessDao.totalBalance();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal frozenTotalAmount() {
		return businessDao.frozenTotalAmount();
	}

	@Override
	@Transactional(readOnly = true)
	public long count(Date beginDate, Date endDate) {
		return businessDao.count(beginDate, endDate);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long total() {
		return businessDao.total();
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		businessDao.delete(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				businessDao.delete(id);
			}
		}
	}


	@Override
	public void addMemberBalance(Member member,BigDecimal refundableAmount) {
		Business business =new Business();
		business.setBalance(member.getBalance().add(refundableAmount));
		business.setVersion(member.getVersion());
		business.setId(member.getId());
		business.setLastModifiedDate(new Date());
		businessDao.addMemberBalance(business);
	}

	/**
	 * 转换分页信息
	 * 
	 */
	protected QueryWrapper<Business> getPageable(Pageable pageable) {
		QueryWrapper<Business> queryWrapper = new QueryWrapper<Business>();
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
	private QueryWrapper<Business> convertFilter(List<Filter> filters) {
		QueryWrapper<Business> queryWrapper = new QueryWrapper<Business>();
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
	private QueryWrapper<Business> convertOrders(List<Order> orders) {
		QueryWrapper<Business> orderList = new QueryWrapper<Business>();
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
	
}
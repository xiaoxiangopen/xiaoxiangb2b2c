package net.xiaoxiangshop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
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
import net.xiaoxiangshop.dao.AdminDao;
import net.xiaoxiangshop.entity.Admin;
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.entity.Role;
import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.service.AdminService;

/**
 * Service - 管理员
 * 
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements AdminService {
	
	/**
	 * 更新忽略属性
	 */
	private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATED_DATE_PROPERTY_NAME, BaseEntity.LAST_MODIFIED_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };
	
	@Inject
	private AdminDao adminDao;

	@Override
	public Admin find(Long id) {
		return adminDao.find(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Admin> findList(Long... ids) {
		List<Admin> result = new ArrayList<>();
		if (ids != null) {
			for (Long id : ids) {
				Admin admin = this.find(id);
				if (admin != null) {
					result.add(admin);
				}
			}
		}
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Admin getUser(Object principal) {
		Assert.notNull(principal, "[Assertion failed] - principal is required; it must not be null");
		Assert.isInstanceOf(String.class, principal);

		return findByUsername(String.valueOf(principal));
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user, "[Assertion failed] - user is required; it must not be null");
		Assert.isInstanceOf(Admin.class, user);

		Admin admin = adminDao.find(user.getId());
		Set<String> result = new HashSet<>();
		if (admin != null && admin.getRoles() != null) {
			for (Role role : admin.getRoles()) {
				if (role.getPermissions() != null) {
					result.addAll(role.getPermissions());
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean supports(Class<?> userClass) {
		return userClass != null && Admin.class.isAssignableFrom(userClass);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return adminDao.exists("username", StringUtils.lowerCase(username));
	}

	@Override
	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		return adminDao.findByAttribute("username", StringUtils.lowerCase(username));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return adminDao.exists("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean emailUnique(Long id, String email) {
		return !adminDao.unique(id, "email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public Admin findByEmail(String email) {
		return adminDao.findByAttribute("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean mobileExists(String mobile) {
		return adminDao.exists("mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public boolean mobileUnique(Long id, String mobile) {
		return !adminDao.unique(id, "mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public Admin findByMobile(String mobile) {
		return adminDao.findByAttribute("mobile", StringUtils.lowerCase(mobile));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Admin> findPage(Pageable pageable) {
		IPage<Admin> iPage = super.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<Admin>(pageable.getPageNumber(), pageable.getPageSize()), getPageable(pageable));
		Page<Admin> page = new Page<Admin>(iPage.getRecords(), iPage.getTotal(), pageable);
		return page;
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public boolean save(Admin admin) {
		Assert.notNull(admin, "[Assertion failed] - member is required; it must not be null");
		
		admin.setId(IdWorker.getId());
		admin.setVersion(0L);
		admin.setCreatedDate(new Date());
		return adminDao.save(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin) {
		Assert.notNull(admin, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(!admin.isNew(), "[Assertion failed] - entity must not be new");

		Admin persistant = this.find(admin.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(admin, persistant, UPDATE_IGNORE_PROPERTIES);
					
			persistant.setLastModifiedDate(new Date());
			adminDao.update(persistant);
		}
		return admin;
	}
	

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin, String... ignoreProperties) {
		Assert.notNull(admin, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(!admin.isNew(), "[Assertion failed] - entity must not be new");

		Admin persistant = this.find(admin.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(admin, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			persistant.setLastModifiedDate(new Date());
			adminDao.update(persistant);
		}
		return admin;
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		adminDao.delete(Arrays.asList(id));
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		adminDao.delete(Arrays.asList(ids));
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Admin admin) {
		adminDao.delete(Arrays.asList(admin.getId()));
	}

	/**
	 * 转换分页信息
	 * 
	 */
	protected QueryWrapper<Admin> getPageable(Pageable pageable) {
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
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
	private QueryWrapper<Admin> convertFilter(List<Filter> filters) {
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
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
	private QueryWrapper<Admin> convertOrders(List<Order> orders) {
		QueryWrapper<Admin> orderList = new QueryWrapper<Admin>();
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
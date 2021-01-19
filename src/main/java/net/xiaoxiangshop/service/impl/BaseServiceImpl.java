package net.xiaoxiangshop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.BaseDao;
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.entity.OrderedEntity;
import net.xiaoxiangshop.service.BaseService;

/**
 * Service - 基类
 * 
 */
@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity<T>> extends ServiceImpl<BaseMapper<T>, T> implements BaseService<T> {


	/**
	 * 更新忽略属性
	 */
	private static final String[] UPDATE_IGNORE_PROPERTIES = new String[] { BaseEntity.CREATED_DATE_PROPERTY_NAME, BaseEntity.LAST_MODIFIED_DATE_PROPERTY_NAME, BaseEntity.VERSION_PROPERTY_NAME };

	/**
	 * 实体类类型
	 */
	private Class<T> entityClass;

	/**
	 * BaseDao
	 */
	private BaseDao<T> baseDao;

	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		entityClass = (Class<T>) resolvableType.as(BaseServiceImpl.class).getGeneric().resolve();
	}

	@Inject
	protected void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	@Transactional(readOnly = true)
	public T find(Long id) {
		return baseDao.find(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return baseDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(Long... ids) {
		List<T> result = new ArrayList<>();
		if (ids != null) {
			for (Long id : ids) {
				T entity = find(id);
				if (entity != null) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(Integer count, List<Filter> filters, List<Order> orders) {
		return findList(null, count, filters, orders);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<T> queryWrapper = createQueryWrapper(first, count, filters, orders);
		return super.list(queryWrapper);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> findPage(Pageable pageable) {
		if (pageable.getOrderDirection() == null && pageable.getOrderProperty() == null) {
			pageable.setOrderDirection(net.xiaoxiangshop.Order.Direction.DESC);
			pageable.setOrderProperty("createdDate");
		}
		IPage<T> iPage = super.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>(pageable.getPageNumber(), pageable.getPageSize()), getPageable(pageable));
		Page<T> page = new Page<T>(iPage.getRecords(), iPage.getTotal(), pageable);
		return page;
	}

	@Transactional(readOnly = true)
	public Page<T> findPage(IPage<T> iPage, Pageable pageable) {
		Page<T> page = new Page<T>(iPage.getRecords(), iPage.getTotal(), pageable);
		return page;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean exists(Long id) {
		return baseDao.find(id) != null;
	}

	@Override
	@Transactional
	public boolean save(T entity) {
		Assert.notNull(entity, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(entity.isNew(), "[Assertion failed] - entity must be new");

		entity.setId(IdWorker.getId());
		entity.setCreatedDate(new Date());
		entity.setVersion(0L);
		return baseDao.save(entity);
	}

	@Override
	@Transactional
	public T update(T entity) {
		Assert.notNull(entity, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(!entity.isNew(), "[Assertion failed] - entity must not be new");

		T persistant = this.find(entity.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(entity, persistant, UPDATE_IGNORE_PROPERTIES);
					
			persistant.setLastModifiedDate(new Date());
			baseDao.update(persistant);
		}
		return entity;
	}

	@Override
	@Transactional
	public T update(T entity, String... ignoreProperties) {
		Assert.notNull(entity, "[Assertion failed] - entity is required; it must not be null");
		Assert.isTrue(!entity.isNew(), "[Assertion failed] - entity must not be new");

		T persistant = this.find(entity.getId());
		if (persistant != null) {
			//copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			BeanUtils.copyProperties(entity, persistant, (String[]) ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			persistant.setLastModifiedDate(new Date());
			baseDao.update(persistant);
		}
		return entity;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		super.removeById(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		if (ids != null) {
			super.removeByIds(Arrays.asList(ids));
		}
	}

	@Override
	@Transactional
	public void delete(T entity) {
		if (entity != null) {
			super.removeById(entity.getId());
		}
	}

	/**
	 * 分页插件
	 * 
	 */
	protected com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> getPluginsPage(Pageable pageable) {
		com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> pluginsPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>(pageable.getPageNumber(), pageable.getPageSize());
		return pluginsPage;
	}
	
	/**
	 * 条件构造器
	 * @param first
	 * @param count
	 * @param filters
	 * @param orders
	 * @return queryWrapper
	 */
	protected QueryWrapper<T> createQueryWrapper(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		if (CollectionUtils.isNotEmpty(orders)) {
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
		}
		
		// 排序
		if (CollectionUtils.isNotEmpty(orders)) {
			for (Order order : orders) {
				if (order == null) {
					continue;
				}
				String property = net.xiaoxiangshop.util.StringUtils.camel2Underline(order.getProperty());
				Order.Direction direction = order.getDirection();
				String[] columns = new String[] {property};
				switch (direction) {
				case ASC:
					queryWrapper.orderByAsc(columns);
					break;
				case DESC:
					queryWrapper.orderByDesc(columns);
					break;
				}
			}
		} else {
			if (OrderedEntity.class.isAssignableFrom(entityClass)) {
				queryWrapper.orderByAsc(OrderedEntity.ORDER_PROPERTY_NAME);
			} else {
				queryWrapper.orderByAsc(net.xiaoxiangshop.util.StringUtils.camel2Underline(OrderedEntity.CREATED_DATE_PROPERTY_NAME));
			}
		}
		
		// 限制行数
		if (first != null && count != null) {
			queryWrapper.last(" LIMIT " + first + ", " + count);
		}
		if (first == null && count != null) {
			queryWrapper.last(" LIMIT 0, " + count);
		}
		return queryWrapper;
	}

	/**
	 * 转换分页信息
	 * 
	 */
	protected QueryWrapper<T> getPageable(Pageable pageable) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
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
	private QueryWrapper<T> convertFilter(List<Filter> filters) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
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
	private QueryWrapper<T> convertOrders(List<Order> orders) {
		QueryWrapper<T> orderList = new QueryWrapper<T>();
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

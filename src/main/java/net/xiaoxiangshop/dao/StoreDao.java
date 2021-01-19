package net.xiaoxiangshop.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.CategoryApplication;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 店铺
 * 
 */
public interface StoreDao extends BaseDao<Store> {

	/**
	 * 查找店铺
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param isEnabled
	 *            是否启用
	 * @param hasExpired
	 *            是否过期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 店铺
	 */
	List<Store> findList(@Param("type")Store.Type type, @Param("status")Store.Status status, @Param("isEnabled")Boolean isEnabled, @Param("hasExpired")Boolean hasExpired, @Param("first")Integer first, @Param("count")Integer count);

	/**
	 * 查找经营分类
	 * 
	 * @param store
	 *            店铺
	 * @param status
	 *            状态
	 * @return 经营分类
	 */
	List<ProductCategory> findProductCategoryList(@Param("store")Store store, @Param("status")CategoryApplication.Status status);

	/**
	 * 查找店铺分页
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param isEnabled
	 *            是否启用
	 * @param hasExpired
	 *            是否过期
	 * @param pageable
	 *            分页信息
	 * @return 店铺分页
	 */
	List<Store> findPage(IPage<Store> iPage, @Param("ew")QueryWrapper<Store> queryWrapper, @Param("type")Store.Type type, @Param("status")Store.Status status, @Param("isEnabled")Boolean isEnabled, @Param("hasExpired")Boolean hasExpired);

	/**
	 * 查询已付保证金总额
	 * 
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 已付保证金总额
	 */
	BigDecimal bailPaidTotalAmountDate(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 查询已付保证金总额
	 * 
	 * @return 已付保证金总额
	 */
	BigDecimal bailPaidTotalAmount();

	/**
	 * 查找店铺数量
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param isEnabled
	 *            是否启用
	 * @param hasExpired
	 *            是否过期
	 * @return 店铺数量
	 */
	Long count(@Param("type")Store.Type type, @Param("status")Store.Status status,  @Param("isEnabled")Boolean isEnabled, @Param("hasExpired")Boolean hasExpired);

}
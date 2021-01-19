package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Area;
import net.xiaoxiangshop.entity.DefaultFreightConfig;
import net.xiaoxiangshop.entity.ShippingMethod;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 默认运费配置
 * 
 */
public interface DefaultFreightConfigDao extends BaseDao<DefaultFreightConfig> {

	/**
	 * 判断运费配置是否存在
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param area
	 *            地区
	 * @return 运费配置是否存在
	 */
	boolean exists(@Param("shippingMethod")ShippingMethod shippingMethod, @Param("area")Area area);

	/**
	 * 查找默认运费配置分页
	 * 
	 * @param store
	 *            店铺
	 * @return 运费配置分页
	 */
	List<DefaultFreightConfig> findPage(IPage<DefaultFreightConfig> iPage, @Param("ew")QueryWrapper<DefaultFreightConfig> queryWrapper, @Param("store")Store store);

	/**
	 * 查找默认运费配置
	 * 
	 * @param shippingMethod
	 *            配送方式
	 * @param store
	 *            店铺
	 * @return 默认运费配置
	 */
	DefaultFreightConfig findDefault(@Param("shippingMethod")ShippingMethod shippingMethod, @Param("store")Store store);

}
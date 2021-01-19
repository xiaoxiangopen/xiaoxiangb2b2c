package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.DeliveryTemplate;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 快递单模板
 * 
 */
public interface DeliveryTemplateDao extends BaseDao<DeliveryTemplate> {

	/**
	 * 查找默认快递单模板
	 * 
	 * @param store
	 *            店铺
	 * @return 默认快递单模板，若不存在则返回null
	 */
	DeliveryTemplate findDefault(@Param("store")Store store);

	/**
	 * 查找快递单模板
	 * 
	 * @param store
	 *            店铺
	 * @return 快递单模板
	 */
	List<DeliveryTemplate> findList(@Param("store")Store store);

	/**
	 * 查找快递单模板分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 快递单模板分页
	 */
	List<DeliveryTemplate> findPage(IPage<DeliveryTemplate> iPage, @Param("ew")QueryWrapper<DeliveryTemplate> queryWrapper, @Param("store")Store store);

	/**
	 * 清除默认
	 * 
	 * @param store
	 *            店铺
	 */
	void clearDefault(@Param("store")Store store);

	/**
	 * 清除默认
	 * 
	 * @param exclude
	 *            排除快递单模板
	 */
	void clearDefaultExclude(@Param("exclude")DeliveryTemplate exclude);

}
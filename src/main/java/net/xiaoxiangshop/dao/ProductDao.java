package net.xiaoxiangshop.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Attribute;
import net.xiaoxiangshop.entity.Brand;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.ProductCategory;
import net.xiaoxiangshop.entity.ProductTag;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreProductCategory;
import net.xiaoxiangshop.entity.StoreProductTag;

/**
 * Dao - 商品
 * 
 */
public interface ProductDao extends BaseDao<Product> {

	/**
	 * 查找商品
	 * 
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            商品分类
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param brand
	 *            品牌
	 * @param productTag
	 *            商品标签
	 * @param storeProductTag
	 *            店铺商品标签
	 * @param attributeValueMap
	 *            属性值Map
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isActive
	 *            是否有效
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param orderType
	 *            排序类型
	 * @return 商品
	 */
	List<Product> findByWrapperList(@Param("ew")QueryWrapper<Product> wrapper, @Param("type")Product.Type type, @Param("store")Store store, @Param("productCategory")ProductCategory productCategory, @Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("brand")Brand brand,@Param("productTag")ProductTag productTag, @Param("storeProductTag")StoreProductTag storeProductTag, @Param("attributeValueMap")Map<Attribute, String> attributeValueMap, @Param("startPrice")BigDecimal startPrice,
			@Param("endPrice")BigDecimal endPrice, @Param("isMarketable")Boolean isMarketable, @Param("isList")Boolean isList, @Param("isTop")Boolean isTop, @Param("isActive")Boolean isActive, @Param("isOutOfStock")Boolean isOutOfStock, @Param("isStockAlert")Boolean isStockAlert,  @Param("orderType")Product.OrderType orderType);

	/**
	 * 查找商品
	 * 
	 * @param productCategory
	 *            商品分类
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param isMarketable
	 *            是否上架
	 * @param isActive
	 *            是否有效
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 商品
	 */
	List<Product> findList(@Param("productCategory")ProductCategory productCategory, @Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("isMarketable")Boolean isMarketable, @Param("isActive")Boolean isActive, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("first")Integer first, @Param("count")Integer count);

	/**
	 * 查找商品
	 * 
	 * @param rankingType
	 *            排名类型
	 * @param store
	 *            店铺
	 * @param count
	 *            数量
	 * @return 商品
	 */
	List<Product> findByRankingTypeList(@Param("rankingType")Product.RankingType rankingType, @Param("store")Store store, @Param("count")Integer count);

	/**
	 * 查找商品分页
	 * 
	 * @param type
	 *            类型
	 * @param storeType
	 *            店铺类型
	 * @param store
	 *            店铺
	 * @param productCategory
	 *            商品分类
	 * @param storeProductCategory
	 *            店铺商品分类
	 * @param brand
	 *            品牌
	 * @param productTag
	 *            商品标签
	 * @param storeProductTag
	 *            店铺商品标签
	 * @param attributeValueMap
	 *            属性值Map
	 * @param startPrice
	 *            最低价格
	 * @param endPrice
	 *            最高价格
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isActive
	 *            是否有效
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @param orderType
	 *            排序类型
	 * @return 商品分页
	 */
	List<Product> findPage(IPage<Product> iPage, @Param("ew")QueryWrapper<Product> queryWrapper, @Param("type")Product.Type type, @Param("storeType")Store.Type storeType, @Param("store")Store store, @Param("productCategory")ProductCategory productCategory, @Param("storeProductCategory")StoreProductCategory storeProductCategory, @Param("brand")Brand brand,@Param("productTag")ProductTag productTag, @Param("storeProductTag")StoreProductTag storeProductTag, @Param("attributeValueMap")Map<Attribute, String> attributeValueMap,
			@Param("startPrice")BigDecimal startPrice, @Param("endPrice")BigDecimal endPrice, @Param("isMarketable")Boolean isMarketable, @Param("isList")Boolean isList, @Param("isTop")Boolean isTop, @Param("isActive")Boolean isActive, @Param("isOutOfStock")Boolean isOutOfStock, @Param("isStockAlert")Boolean isStockAlert,  @Param("orderType")Product.OrderType orderType,@Param("noteId")String noteId,@Param("erpFlag")String erpFlag);

	/**
	 * 查询商品数量
	 * 
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isActive
	 *            是否有效
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @return 商品数量
	 */
	Long count(@Param("type")Product.Type type, @Param("store")Store store, @Param("isMarketable")Boolean isMarketable, @Param("isList")Boolean isList, @Param("isTop")Boolean isTop, @Param("isActive")Boolean isActive, @Param("isOutOfStock")Boolean isOutOfStock, @Param("isStockAlert")Boolean isStockAlert);

	/**
	 * 清除商品属性值
	 * 
	 * @param attribute
	 *            属性
	 */
	void clearAttributeValue(Attribute attribute);

	/**
	 * 刷新过期店铺商品有效状态
	 */
	void refreshExpiredStoreProductActive();

	/**
	 * 刷新商品有效状态
	 * 
	 * @param store
	 *            店铺
	 */
	void refreshActive(@Param("store")Store store);

	/**
	 * 上架商品
	 * 
	 * @param ids
	 *            ID
	 */
	void shelves(@Param("ids")Long[] ids);

	/**
	 * 下架商品
	 * 
	 * @param ids
	 *            ID
	 */
	void shelf(@Param("ids")Long[] ids);


	/**
	 * 商品促销配置
	 * @param product
	 */
	void addProductPromotion(@Param("entity")Product product);
	/**
	 * 查询商品数量
	 *
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @param isMarketable
	 *            是否上架
	 * @param isList
	 *            是否列出
	 * @param isTop
	 *            是否置顶
	 * @param isActive
	 *            是否有效
	 * @param isOutOfStock
	 *            是否缺货
	 * @param isStockAlert
	 *            是否库存警告
	 * @return 商品数量
	 */
	Long selectCount(@Param("type")Product.Type type, @Param("store")Store store, @Param("isMarketable")Boolean isMarketable, @Param("isList")Boolean isList, @Param("isTop")Boolean isTop, @Param("isActive")Boolean isActive, @Param("isOutOfStock")Boolean isOutOfStock, @Param("isStockAlert")Boolean isStockAlert);

    void updateBusiness(@Param("entity")Product entity);
}
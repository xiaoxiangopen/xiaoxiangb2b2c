package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.entity.StoreFavorite;

/**
 * Dao - 店铺收藏
 * 
 */
public interface StoreFavoriteDao extends BaseDao<StoreFavorite> {

	/**
	 * 判断店铺收藏是否存在
	 * 
	 * @param member
	 *            会员
	 * @param store
	 *            店铺
	 * @return 店铺收藏是否存在
	 */
	boolean exists(@Param("member")Member member, @Param("store")Store store);

	/**
	 * 查找店铺收藏
	 * 
	 * @param member
	 *            会员
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 店铺收藏
	 */
	List<StoreFavorite> findList(@Param("ew")QueryWrapper<StoreFavorite> queryWrapper, @Param("member")Member member);

	/**
	 * 查找店铺收藏分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 店铺收藏分页
	 */
	List<StoreFavorite> findPage(IPage<StoreFavorite> iPage, @Param("ew")QueryWrapper<StoreFavorite> queryWrapper, @Param("member")Member member);

	/**
	 * 查找店铺收藏数量
	 * 
	 * @param member
	 *            会员
	 * @return 店铺收藏数量
	 */
	Long count(@Param("member")Member member);

}
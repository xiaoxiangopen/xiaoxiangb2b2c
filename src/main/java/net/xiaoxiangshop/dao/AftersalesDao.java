package net.xiaoxiangshop.dao;

import java.util.List;

import net.xiaoxiangshop.entity.*;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Dao - 售后
 * 
 */
public interface AftersalesDao extends BaseDao<Aftersales> {

	/**
	 * 查找售后列表
	 * 
	 * @param orderItems
	 *            订单项
	 * @return 售后列表
	 */
	List<Aftersales> findList(@Param("orderItems")List<OrderItem> orderItems);

	/**
	 * 查找售后分页
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param member
	 *            会员
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页信息
	 * @return 售后分页
	 */
	List<Aftersales> findPage(IPage<Aftersales> iPage, @Param("ew")QueryWrapper<Aftersales> queryWrapper, @Param("type")Aftersales.Type type, @Param("status")Aftersales.Status status, @Param("member")Member member, @Param("store")Store store);


	/**
	 * 查询售后申请数量
	 *
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param store
	 *            店铺ID
	 * @param member

	 * @return 售后申请数量
	 */
	Long count(@Param("type") Aftersales.Type type,@Param("status") Aftersales.Status status,@Param("store")Store store, @Param("member")Member member);
}
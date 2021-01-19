package net.xiaoxiangshop.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.Order.CommissionType;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Dao - 订单
 * 
 */
public interface OrderPlusDao extends BaseDao<OrderPlus> {

	/**
	 * 查找订单
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @return 订单
	 */
	List<OrderPlus> findList(@Param("ew")QueryWrapper<OrderPlus> queryWrapper, @Param("type")Order.Type type, @Param("status")Order.Status status, @Param("store")Store store, @Param("member")Member member, @Param("product")Product product, @Param("isPendingReceive")Boolean isPendingReceive, @Param("isPendingRefunds")Boolean isPendingRefunds, @Param("isExchangePoint")Boolean isExchangePoint, @Param("isAllocatedStock")Boolean isAllocatedStock, @Param("hasExpired")Boolean hasExpired);

	/**
	 * 查找订单分页
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 订单分页
	 */
	List<OrderPlus> findPage(IPage<OrderPlus> iPage, @Param("ew")QueryWrapper<OrderPlus> queryWrapper, @Param("type")Order.Type type, @Param("status")OrderPlus.Status status, @Param("store")Store store, @Param("member")Member member, @Param("product")Product product, @Param("isPendingReceive")Boolean isPendingReceive, @Param("isPendingRefunds")Boolean isPendingRefunds, @Param("isExchangePoint")Boolean isExchangePoint, @Param("isAllocatedStock")Boolean isAllocatedStock, @Param("hasExpired")Boolean hasExpired);

	/**
	 * 查询订单数量
	 * 
	 * @param type
	 *            类型
	 * @param status
	 *            状态
	 * @param store
	 *            店铺
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isPendingReceive
	 *            是否等待收款
	 * @param isPendingRefunds
	 *            是否等待退款
	 * @param isExchangePoint
	 *            是否已兑换积分
	 * @param isAllocatedStock
	 *            是否已分配库存
	 * @param hasExpired
	 *            是否已过期
	 * @return 订单数量
	 */
	Long count(@Param("type")Order.Type type, @Param("status")Order.Status status, @Param("store")Store store, @Param("member")Member member, @Param("product")Product product, @Param("isPendingReceive")Boolean isPendingReceive, @Param("isPendingRefunds")Boolean isPendingRefunds,  @Param("isExchangePoint")Boolean isExchangePoint, @Param("isAllocatedStock")Boolean isAllocatedStock, @Param("hasExpired")Boolean hasExpired);

	/**
	 * 查询订单创建数
	 * 
	 * @param store
	 *            店铺
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单创建数
	 */
	Long createOrderCount(@Param("store")Store store, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 查询订单完成数
	 * 
	 * @param store
	 *            店铺
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单完成数
	 */
	Long completeOrderCount(@Param("store")Store store, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 查询订单创建金额
	 * 
	 * @param store
	 *            店铺
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单创建金额
	 */
	BigDecimal createOrderAmount(@Param("store")Store store, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 查询订单完成金额
	 * 
	 * @param store
	 *            店铺
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 订单完成金额
	 */
	BigDecimal completeOrderAmount(@Param("store")Store store, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

	/**
	 * 查询已发放佣金总额
	 * 
	 * @param store
	 *            店铺
	 * @param commissionType
	 *            佣金类型
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param statuses
	 *            订单状态
	 * @return 已发放佣金总额
	 */
	BigDecimal grantedCommissionTotalAmount(@Param("store")Store store, @Param("commissionType")CommissionType commissionType, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("statuses")Order.Status... statuses);

}
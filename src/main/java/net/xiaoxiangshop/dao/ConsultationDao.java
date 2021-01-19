package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Consultation;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Product;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 咨询
 * 
 */
public interface ConsultationDao extends BaseDao<Consultation> {

	/**
	 * 查找咨询
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isShow
	 *            是否显示
	 * @return 咨询，不包含咨询回复
	 */
	List<Consultation> findList(@Param("ew")QueryWrapper<Consultation> queryWrapper, @Param("member")Member member, @Param("product")Product product, @Param("isShow")Boolean isShow);

	/**
	 * 查找咨询分页
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param store
	 *            店铺
	 * @param isShow
	 *            是否显示
	 * @param pageable
	 *            分页信息
	 * @return 咨询分页，不包含咨询回复
	 */
	List<Consultation> findPage(IPage<Consultation> iPage, @Param("ew")QueryWrapper<Consultation> queryWrapper, @Param("member")Member member, @Param("product")Product product, @Param("store")Store store, @Param("isShow")Boolean isShow);

	/**
	 * 查找咨询数量
	 * 
	 * @param member
	 *            会员
	 * @param product
	 *            商品
	 * @param isShow
	 *            是否显示
	 * @return 咨询数量，不包含咨询回复
	 */
	Long count(@Param("member")Member member, @Param("product")Product product, @Param("isShow")Boolean isShow);

}
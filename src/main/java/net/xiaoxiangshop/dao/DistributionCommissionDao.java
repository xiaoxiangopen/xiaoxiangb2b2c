package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.DistributionCommission;
import net.xiaoxiangshop.entity.Distributor;

/**
 * Dao - 分销佣金
 * 
 */
public interface DistributionCommissionDao extends BaseDao<DistributionCommission> {

	/**
	 * 查找分销佣金记录分页
	 * 
	 * @param distributor
	 *            分销员
	 * @param pageable
	 *            分页信息
	 * @return 分销佣金记录分页
	 */
	List<DistributionCommission> findPage(IPage<DistributionCommission> iPage, @Param("ew")QueryWrapper<DistributionCommission> queryWrapper, @Param("distributor")Distributor distributor);

}
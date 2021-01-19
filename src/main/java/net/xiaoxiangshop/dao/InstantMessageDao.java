package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.InstantMessage;
import net.xiaoxiangshop.entity.InstantMessage.Type;
import net.xiaoxiangshop.entity.Store;

/**
 * Dao - 即时通讯
 * 
 */
public interface InstantMessageDao extends BaseDao<InstantMessage> {

	/**
	 * 查找即时通讯
	 *
	 * @param type
	 *            类型
	 * @param store
	 *            店铺
	 * @return 即时通讯
	 */
	List<InstantMessage> findList(@Param("ew")QueryWrapper<InstantMessage> queryWrapper, @Param("type")Type type, @Param("store")Store store);

	/**
	 * 查找即时通讯分页
	 * 
	 * @param store
	 *            店铺
	 * @param pageable
	 *            分页
	 * @return 即时通讯分页
	 */
	List<InstantMessage> findPage(IPage<InstantMessage> iPage, @Param("ew")QueryWrapper<InstantMessage> queryWrapper, @Param("store")Store store);

}
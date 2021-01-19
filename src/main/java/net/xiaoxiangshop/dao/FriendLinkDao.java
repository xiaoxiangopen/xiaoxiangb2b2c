package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.FriendLink;

/**
 * Dao - 友情链接
 * 
 */
public interface FriendLinkDao extends BaseDao<FriendLink> {

	/**
	 * 查找友情链接
	 * 
	 * @param type
	 *            类型
	 * @return 友情链接
	 */
	List<FriendLink> findList(@Param("type")FriendLink.Type type);

}
package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.Message;
import net.xiaoxiangshop.entity.MessageGroup;
import net.xiaoxiangshop.entity.User;

/**
 * Dao - 消息
 * 
 */
public interface MessageDao extends BaseDao<Message> {

	/**
	 * 查找
	 * 
	 * @param messageGroup
	 *            消息组
	 * @param user
	 *            用户
	 * @return 消息
	 */
	List<Message> findList(@Param("messageGroup")MessageGroup messageGroup, @Param("user")User user);

	/**
	 * 未读消息数量
	 * 
	 * @param messageGroup
	 *            消息组
	 * @param user
	 *            用户
	 * @return 未读消息数量
	 */
	Long unreadMessageCount(@Param("messageGroup")MessageGroup messageGroup, @Param("user")User user);

}
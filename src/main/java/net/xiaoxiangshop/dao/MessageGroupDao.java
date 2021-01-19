package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.MessageGroup;
import net.xiaoxiangshop.entity.User;

/**
 * Dao - 消息组
 * 
 */
public interface MessageGroupDao extends BaseDao<MessageGroup> {

	/**
	 * 查找消息分页
	 * 
	 * @param user
	 *            用户
	 * @param pageable
	 *            分页信息
	 * @return 消息组分页
	 */
	List<MessageGroup> findPage(IPage<MessageGroup> iPage, @Param("ew")QueryWrapper<MessageGroup> queryWrapper, @Param("user")User user);

	/**
	 * 查找消息组
	 * 
	 * @param user1
	 *            用户1
	 * @param user2
	 *            用户2
	 * @return 消息组
	 */
	MessageGroup findByUser(@Param("user1")User user1, @Param("user2")User user2);

}
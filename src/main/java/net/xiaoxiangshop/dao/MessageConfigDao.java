package net.xiaoxiangshop.dao;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.MessageConfig;

/**
 * Dao - 消息配置
 * 
 */
public interface MessageConfigDao extends BaseDao<MessageConfig> {

	/**
	 * 查找消息配置
	 * 
	 * @param type
	 *            类型
	 * @return 消息配置
	 */
	MessageConfig findByType(@Param("type")MessageConfig.Type type);

}
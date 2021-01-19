package net.xiaoxiangshop.dao;

import net.xiaoxiangshop.entity.AuditLog;

/**
 * Dao - 审计日志
 * 
 */
public interface AuditLogDao extends BaseDao<AuditLog> {

	/**
	 * 删除所有
	 */
	void removeAll();

}
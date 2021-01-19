package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.AuditLog;

/**
 * Service - 审计日志
 * 
 */
public interface AuditLogService extends BaseService<AuditLog> {

	/**
	 * 创建审计日志(异步)
	 * 
	 * @param auditLog
	 *            审计日志
	 */
	void create(AuditLog auditLog);

	/**
	 * 清空审计日志
	 */
	void clear();

}
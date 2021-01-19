package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.xiaoxiangshop.dao.AuditLogDao;
import net.xiaoxiangshop.entity.AuditLog;
import net.xiaoxiangshop.service.AuditLogService;

/**
 * Service - 审计日志
 * 
 */
@Service
public class AuditLogServiceImpl extends BaseServiceImpl<AuditLog> implements AuditLogService {

	@Inject
	private AuditLogDao auditLogDao;

	@Override
	@Async
	public void create(AuditLog auditLog) {
		super.save(auditLog);
	}

	@Override
	public void clear() {
		auditLogDao.removeAll();
	}

}
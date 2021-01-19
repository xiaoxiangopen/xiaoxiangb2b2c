package net.xiaoxiangshop.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.dao.PointLogDao;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.PointLog;
import net.xiaoxiangshop.service.PointLogService;

/**
 * Service - 积分记录
 * 
 */
@Service
public class PointLogServiceImpl extends BaseServiceImpl<PointLog> implements PointLogService {

	@Inject
	private PointLogDao pointLogDao;

	@Override
	@Transactional(readOnly = true)
	public Page<PointLog> findPage(Member member, Pageable pageable) {
		IPage<PointLog> iPage = getPluginsPage(pageable);
		iPage.setRecords(pointLogDao.findPage(iPage, getPageable(pageable), member));
		return super.findPage(iPage, pageable);
	}

}
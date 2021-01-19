package net.xiaoxiangshop.service;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.PointLog;

/**
 * Service - 积分记录
 * 
 */
public interface PointLogService extends BaseService<PointLog> {

	/**
	 * 查找积分记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 积分记录分页
	 */
	Page<PointLog> findPage(Member member, Pageable pageable);

}
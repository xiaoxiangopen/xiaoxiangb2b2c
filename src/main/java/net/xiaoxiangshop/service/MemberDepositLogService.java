package net.xiaoxiangshop.service;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberDepositLog;

import java.util.List;
import java.util.Set;

/**
 * Service - 会员预存款记录
 * 
 */
public interface MemberDepositLogService extends BaseService<MemberDepositLog> {

	/**
	 * 查找会员预存款记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 会员预存款记录分页
	 */
	Page<MemberDepositLog> findPage(Member member, Pageable pageable);

	void insert(MemberDepositLog memberDepositLog);

	Set<MemberDepositLog> findListByUserId(Member member);
}
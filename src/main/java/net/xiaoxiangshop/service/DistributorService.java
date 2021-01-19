package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.Distributor;
import net.xiaoxiangshop.entity.Member;

/**
 * Service - 分销员
 * 
 */
public interface DistributorService extends BaseService<Distributor> {

	/**
	 * 创建
	 * 
	 * @param member
	 *            会员
	 */
	void create(Member member);

	/**
	 * 创建
	 * 
	 * @param member
	 *            会员
	 * @param spreadMember
	 *            推广会员
	 */
	void create(Member member, Member spreadMember);

	/**
	 * 修改
	 * 
	 * @param distributor
	 *            分销员
	 * @param spreadMember
	 *            推广会员
	 */
	void modify(Distributor distributor, Member spreadMember);

}
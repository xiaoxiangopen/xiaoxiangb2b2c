package net.xiaoxiangshop.service;

import java.util.List;

import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.Receiver;

/**
 * Service - 收货地址
 * 
 */
public interface ReceiverService extends BaseService<Receiver> {

	/**
	 * 查找默认收货地址
	 * 
	 * @param member
	 *            会员
	 * @return 默认收货地址，若不存在则返回最新收货地址
	 */
	Receiver findDefault(Member member);

	/**
	 * 查找收货地址列表
	 * 
	 * @param member
	 *            会员
	 * @return 收货地址
	 */
	List<Receiver> findList(Member member);

	/**
	 * 查找收货地址分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 收货地址分页
	 */
	Page<Receiver> findPage(Member member, Pageable pageable);

}
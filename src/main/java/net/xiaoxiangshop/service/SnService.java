package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.Sn;

/**
 * Service - 序列号
 * 
 */
public interface SnService extends BaseService<Sn>{

	/**
	 * 生成序列号
	 * 
	 * @param type
	 *            类型
	 * @return 序列号
	 */
	String generate(Sn.Type type);

}
package net.xiaoxiangshop.audit;

/**
 * Audit - 审计者Provider
 * 
 */
public interface AuditorProvider<T> {

	/**
	 * 获取当前审计者
	 * 
	 * @return 当前审计者
	 */
	T getCurrentAuditor();

}
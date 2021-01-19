package net.xiaoxiangshop.service;

/**
 * Service - 缓存
 * 
 */
public interface CacheService {

	/**
	 * 获取缓存存储路径
	 * 
	 * @return 缓存存储路径
	 */
	String getDiskStorePath();

	/**
	 * 获取缓存数
	 * 
	 * @return 缓存数
	 */
	int getCacheSize();

	/**
	 * 清除缓存
	 */
	void clear();

}
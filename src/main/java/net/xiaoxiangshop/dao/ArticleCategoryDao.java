package net.xiaoxiangshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.xiaoxiangshop.entity.ArticleCategory;

/**
 * Dao - 文章分类
 * 
 */
public interface ArticleCategoryDao extends BaseDao<ArticleCategory> {

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<ArticleCategory> findRoots(@Param("count")Integer count);

	/**
	 * 查找上级文章分类
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 上级文章分类
	 */
	List<ArticleCategory> findParents(@Param("articleCategory")ArticleCategory articleCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);


	/**
	 * 查找下级文章分类
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @return 下级文章分类
	 */
	List<ArticleCategory> findChildren(@Param("articleCategory")ArticleCategory articleCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);

}
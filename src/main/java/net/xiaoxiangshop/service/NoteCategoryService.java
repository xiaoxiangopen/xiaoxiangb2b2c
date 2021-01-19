package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.ArticleCategory;
import net.xiaoxiangshop.entity.Note;
import net.xiaoxiangshop.entity.NoteCategory;

import java.util.List;

/**
 * Service - 文章分类
 * 
 */
public interface NoteCategoryService extends BaseService<NoteCategory> {

	/**
	 * 查找顶级文章分类
	 * 
	 * @return 顶级文章分类
	 */
	List<NoteCategory> findRoots();

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<NoteCategory> findRoots(Integer count);

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 顶级文章分类
	 */
	List<NoteCategory> findRoots(Integer count, boolean useCache);

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
	List<NoteCategory> findParents(NoteCategory articleCategory, boolean recursive, Integer count);

	/**
	 * 查找上级文章分类
	 * 
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 上级文章分类
	 */
	List<NoteCategory> findParents(Long articleCategoryId, boolean recursive, Integer count, boolean useCache);

	/**
	 * 查找文章分类树
	 * 
	 * @return 文章分类树
	 */
	List<NoteCategory> findTree();

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
	List<NoteCategory> findChildren(NoteCategory articleCategory, boolean recursive, Integer count);

	/**
	 * 查找下级文章分类
	 * 
	 * @param articleCategoryId
	 *            文章分类ID
	 * @param recursive
	 *            是否递归
	 * @param count
	 *            数量
	 * @param useCache
	 *            是否使用缓存
	 * @return 下级文章分类
	 */
	List<NoteCategory> findChildren(Long articleCategoryId, boolean recursive, Integer count, boolean useCache);

}
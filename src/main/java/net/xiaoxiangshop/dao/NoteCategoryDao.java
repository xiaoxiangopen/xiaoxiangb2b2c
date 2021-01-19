package net.xiaoxiangshop.dao;

import net.xiaoxiangshop.entity.NoteCategory;
import net.xiaoxiangshop.entity.NoteCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dao - 文章分类
 * 
 */
public interface NoteCategoryDao extends BaseDao<NoteCategory> {

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<NoteCategory> findRoots(@Param("count")Integer count);

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
	List<NoteCategory> findParents(@Param("articleCategory")NoteCategory articleCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);


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
	List<NoteCategory> findChildren(@Param("articleCategory")NoteCategory articleCategory, @Param("recursive")boolean recursive, @Param("count")Integer count);

}
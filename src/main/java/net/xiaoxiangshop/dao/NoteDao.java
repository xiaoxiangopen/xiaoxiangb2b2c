package net.xiaoxiangshop.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.xiaoxiangshop.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Dao - 文章
 * 
 */
public interface NoteDao extends BaseDao<Note> {

	/**
	 * 查找文章
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param articleTag
	 *            文章标签
	 * @param isPublication
	 *            是否发布
	 * @return 文章
	 */
	List<Note> findByWrapperList(@Param("ew")QueryWrapper<Note> wrapper, @Param("articleCategory") NoteCategory articleCategory, @Param("articleTag")NoteTag articleTag, @Param("isPublication")Boolean isPublication);

	/**
	 * 查找文章
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param isPublication
	 *            是否发布
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @return 文章
	 */
	List<Note> findList(@Param("articleCategory")NoteCategory articleCategory, @Param("isPublication")Boolean isPublication, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("first")Integer first, @Param("count")Integer count);

	/**
	 * 查找文章分页
	 * 
	 * @param articleCategory
	 *            文章分类
	 * @param articleTag
	 *            文章标签
	 * @param isPublication
	 *            是否发布
	 * @param pageable
	 *            分页信息
	 * @return 文章分页
	 */
	List<Note> findPage(IPage<Note> iPage, @Param("ew")QueryWrapper<Note> queryWrapper, @Param("articleCategory")NoteCategory articleCategory, @Param("articleTag")NoteTag articleTag, @Param("isPublication")Boolean isPublication);

}
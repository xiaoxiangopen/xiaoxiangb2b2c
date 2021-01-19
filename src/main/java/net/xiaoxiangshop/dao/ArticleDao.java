package net.xiaoxiangshop.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.xiaoxiangshop.entity.Article;
import net.xiaoxiangshop.entity.ArticleCategory;
import net.xiaoxiangshop.entity.ArticleTag;

/**
 * Dao - 文章
 * 
 */
public interface ArticleDao extends BaseDao<Article> {

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
	List<Article> findByWrapperList(@Param("ew")QueryWrapper<Article> wrapper, @Param("articleCategory")ArticleCategory articleCategory, @Param("articleTag")ArticleTag articleTag, @Param("isPublication")Boolean isPublication);

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
	List<Article> findList(@Param("articleCategory")ArticleCategory articleCategory, @Param("isPublication")Boolean isPublication, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("first")Integer first, @Param("count")Integer count);

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
	List<Article> findPage(IPage<Article> iPage, @Param("ew")QueryWrapper<Article> queryWrapper, @Param("articleCategory")ArticleCategory articleCategory, @Param("articleTag")ArticleTag articleTag, @Param("isPublication")Boolean isPublication);

}
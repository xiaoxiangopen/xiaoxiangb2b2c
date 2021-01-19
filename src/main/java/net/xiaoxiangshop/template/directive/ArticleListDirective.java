package net.xiaoxiangshop.template.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.Article;
import net.xiaoxiangshop.service.ArticleService;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 模板指令 - 文章列表
 * 
 */
public class ArticleListDirective extends BaseDirective {

	/**
	 * "文章分类ID"参数名称
	 */
	private static final String ARTICLE_CATEGORY_ID_PARAMETER_NAME = "articleCategoryId";

	/**
	 * "文章标签ID"参数名称
	 */
	private static final String ARTICLE_TAG_ID_PARAMETER_NAME = "articleTagId";

	/**
	 * 变量名称
	 */
	private static final String VARIABLE_NAME = "articles";

	@Inject
	private ArticleService articleService;

	/**
	 * 执行
	 * 
	 * @param env
	 *            环境变量
	 * @param params
	 *            参数
	 * @param loopVars
	 *            循环变量
	 * @param body
	 *            模板内容
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long articleCategoryId = FreeMarkerUtils.getParameter(ARTICLE_CATEGORY_ID_PARAMETER_NAME, Long.class, params);
		Long articleTagId = FreeMarkerUtils.getParameter(ARTICLE_TAG_ID_PARAMETER_NAME, Long.class, params);
		Integer count = getCount(params);
		List<Filter> filters = getFilters(params, Article.class);
		List<Order> orders = getOrders(params);
		boolean useCache = useCache(params);

		List<Article> articles = articleService.findList(articleCategoryId, articleTagId, true, count, filters, orders, useCache);
		setLocalVariable(VARIABLE_NAME, articles, env, body);
	}

}
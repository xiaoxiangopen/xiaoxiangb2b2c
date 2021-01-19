package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.data.elasticsearch.annotations.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 文章
 * 
 */
@Entity
@Document(indexName = "article")
public class Article extends BaseEntity<Article> {

	private static final long serialVersionUID = 1475773294701585482L;

	/**
	 * 点击数缓存名称
	 */
	public static final String HITS_CACHE_NAME = "articleHits";

	/**
	 * 路径
	 */
	private static final String PATH = "/article/detail/%d_%d";

	/**
	 * 内容分页长度
	 */
	private static final int PAGE_CONTENT_LENGTH = 2000;

	/**
	 * 内容分页标签
	 */
	private static final String PAGE_BREAK_TAG = "xiaoxiangshop_page_break_tag";

	/**
	 * 段落配比
	 */
	private static final Pattern PARAGRAPH_PATTERN = Pattern.compile("[^,;\\.!?，；。！？]*([,;\\.!?，；。！？]+|$)");

	/**
	 * 标题
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	private String title;

	/**
	 * 作者
	 */
	@JsonView(BaseView.class)
	private String author;

	/**
	 * 内容
	 */
	@JsonView(BaseView.class)
	@Lob
	private String content;

	/**
	 * 页面标题
	 */
	private String seoTitle;

	/**
	 * 页面关键词
	 */
	private String seoKeywords;

	/**
	 * 页面描述
	 */
	private String seoDescription;

	/**
	 * 是否发布
	 */
	@NotNull
	private Boolean isPublication;

	/**
	 * 是否置顶
	 */
	@NotNull
	private Boolean isTop;

	/**
	 * 点击数
	 */
	private Long hits;

	/**
	 * 文章分类
	 */
	@TableField(exist = false)
	private ArticleCategory articleCategory;

	/**
	 * 文章标签
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Set<ArticleTag> articleTags = new HashSet<>();

	/**
	 * 获取标题
	 * 
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取作者
	 * 
	 * @return 作者
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 设置作者
	 * 
	 * @param author
	 *            作者
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取页面标题
	 * 
	 * @return 页面标题
	 */
	public String getSeoTitle() {
		return seoTitle;
	}

	/**
	 * 设置页面标题
	 * 
	 * @param seoTitle
	 *            页面标题
	 */
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	/**
	 * 获取页面关键词
	 * 
	 * @return 页面关键词
	 */
	public String getSeoKeywords() {
		return seoKeywords;
	}

	/**
	 * 设置页面关键词
	 * 
	 * @param seoKeywords
	 *            页面关键词
	 */
	public void setSeoKeywords(String seoKeywords) {
		if (seoKeywords != null) {
			seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", StringUtils.EMPTY);
		}
		this.seoKeywords = seoKeywords;
	}

	/**
	 * 获取页面描述
	 * 
	 * @return 页面描述
	 */
	public String getSeoDescription() {
		return seoDescription;
	}

	/**
	 * 设置页面描述
	 * 
	 * @param seoDescription
	 *            页面描述
	 */
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	/**
	 * 获取是否发布
	 * 
	 * @return 是否发布
	 */
	public Boolean getIsPublication() {
		return isPublication;
	}

	/**
	 * 设置是否发布
	 * 
	 * @param isPublication
	 *            是否发布
	 */
	public void setIsPublication(Boolean isPublication) {
		this.isPublication = isPublication;
	}

	/**
	 * 获取是否置顶
	 * 
	 * @return 是否置顶
	 */
	public Boolean getIsTop() {
		return isTop;
	}

	/**
	 * 设置是否置顶
	 * 
	 * @param isTop
	 *            是否置顶
	 */
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	/**
	 * 获取点击数
	 * 
	 * @return 点击数
	 */
	public Long getHits() {
		return hits;
	}

	/**
	 * 设置点击数
	 * 
	 * @param hits
	 *            点击数
	 */
	public void setHits(Long hits) {
		this.hits = hits;
	}

	/**
	 * 获取文章分类
	 * 
	 * @return 文章分类
	 */
	public ArticleCategory getArticleCategory() {
		return articleCategory;
	}

	/**
	 * 设置文章分类
	 * 
	 * @param articleCategory
	 *            文章分类
	 */
	public void setArticleCategory(ArticleCategory articleCategory) {
		this.articleCategory = articleCategory;
	}

	/**
	 * 获取文章标签
	 * 
	 * @return 文章标签
	 */
	public Set<ArticleTag> getArticleTags() {
		return articleTags;
	}

	/**
	 * 设置文章标签
	 * 
	 * @param articleTags
	 *            文章标签
	 */
	public void setArticleTags(Set<ArticleTag> articleTags) {
		this.articleTags = articleTags;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@JsonView(BaseView.class)
	@Transient
	public String getPath() {
		return getPath(1);
	}

	/**
	 * 获取路径
	 * 
	 * @param pageNumber
	 *            页码
	 * @return 路径
	 */
	@Transient
	public String getPath(Integer pageNumber) {
		return String.format(Article.PATH, getId(), pageNumber);
	}

	/**
	 * 获取文本内容
	 * 
	 * @return 文本内容
	 */
	@JsonView(BaseView.class)
	@Transient
	public String getText() {
		if (StringUtils.isEmpty(getContent())) {
			return StringUtils.EMPTY;
		}
		return StringUtils.remove(Jsoup.parse(getContent()).text(), PAGE_BREAK_TAG);
	}

	/**
	 * 获取分页内容
	 * 
	 * @return 分页内容
	 */
	@Transient
	public String[] getPageContents() {
		if (StringUtils.isEmpty(getContent())) {
			return new String[] { StringUtils.EMPTY };
		}
		if (StringUtils.contains(getContent(), PAGE_BREAK_TAG)) {
			return StringUtils.splitByWholeSeparator(getContent(), PAGE_BREAK_TAG);
		}
		List<Node> childNodes = Jsoup.parse(getContent()).body().childNodes();
		if (CollectionUtils.isEmpty(childNodes)) {
			return new String[] { getContent() };
		}
		List<String> pageContents = new ArrayList<>();
		int textLength = 0;
		StringBuilder paragraph = new StringBuilder();
		for (Node node : childNodes) {
			if (node instanceof Element) {
				Element element = (Element) node;
				paragraph.append(element.outerHtml());
				textLength += element.text().length();
				if (textLength >= PAGE_CONTENT_LENGTH) {
					pageContents.add(String.valueOf(paragraph));
					textLength = 0;
					paragraph.setLength(0);
				}
			} else if (node instanceof TextNode) {
				TextNode textNode = (TextNode) node;
				Matcher matcher = PARAGRAPH_PATTERN.matcher(textNode.text());
				while (matcher.find()) {
					String content = matcher.group();
					paragraph.append(content);
					textLength += content.length();
					if (textLength >= PAGE_CONTENT_LENGTH) {
						pageContents.add(String.valueOf(paragraph));
						textLength = 0;
						paragraph.setLength(0);
					}
				}
			}
		}
		String pageContent = paragraph.toString();
		if (StringUtils.isNotEmpty(pageContent)) {
			pageContents.add(pageContent);
		}
		return pageContents.toArray(new String[pageContents.size()]);
	}

	/**
	 * 获取分页内容
	 * 
	 * @param pageNumber
	 *            页码
	 * @return 分页内容
	 */
	@Transient
	public String getPageContent(Integer pageNumber) {
		if (pageNumber == null || pageNumber < 1) {
			return null;
		}
		String[] pageContents = getPageContents();
		if (pageContents.length < pageNumber) {
			return null;
		}
		return pageContents[pageNumber - 1];
	}

	/**
	 * 获取总页数
	 * 
	 * @return 总页数
	 */
	@Transient
	public int getTotalPages() {
		return getPageContents().length;
	}

}
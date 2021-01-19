package net.xiaoxiangshop.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.PreRemove;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 文章标签
 * 
 */
@Entity
public class ArticleTag extends OrderedEntity<ArticleTag> {

	private static final long serialVersionUID = -2735037966597250149L;

	/**
	 * 名称
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	private String name;

	/**
	 * 备注
	 */
	@JsonView(BaseView.class)
	private String memo;

	/**
	 * 文章
	 */
	@TableField(exist = false)
	private Set<Article> articles = new HashSet<>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取文章
	 * 
	 * @return 文章
	 */
	public Set<Article> getArticles() {
		return articles;
	}

	/**
	 * 设置文章
	 * 
	 * @param articles
	 *            文章
	 */
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<Article> articles = getArticles();
		if (articles != null) {
			for (Article article : articles) {
				article.getArticleTags().remove(this);
			}
		}
	}

}
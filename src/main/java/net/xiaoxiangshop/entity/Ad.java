package net.xiaoxiangshop.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Entity - 广告
 * 
 */
@Entity
public class Ad extends OrderedEntity<Ad> {

	private static final long serialVersionUID = -1307743303786909390L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 文本
		 */
		TEXT(0),

		/**
		 * 图片
		 */
		IMAGE(1);
		
		private int value;

		Type(final int value) {
			this.value = value;
		}
		
		@Override
		public Integer getValue() {
			return this.value;
		}
	}

	/**
	 * 标题
	 */
	@NotEmpty
	private String title;

	/**
	 * 类型
	 */
	@NotNull
	private Ad.Type type;

	/**
	 * 内容
	 */
	@Lob
	private String content;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 起始日期
	 */
	private Date beginDate;

	/**
	 * 结束日期
	 */
	private Date endDate;

	/**
	 * 链接地址
	 */
	private String url;

	/**
	 * 广告位
	 */
	@JsonIgnore
	@TableField(exist = false)
	private AdPosition adPosition;

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
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@JsonValue
	public Ad.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Ad.Type type) {
		this.type = type;
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
	 * 获取路径
	 * 
	 * @return 路径
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 设置路径
	 * 
	 * @param path
	 *            路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取起始日期
	 * 
	 * @return 起始日期
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 设置起始日期
	 * 
	 * @param beginDate
	 *            起始日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 获取结束日期
	 * 
	 * @return 结束日期
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置结束日期
	 * 
	 * @param endDate
	 *            结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取链接地址
	 * 
	 * @return 链接地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置链接地址
	 * 
	 * @param url
	 *            链接地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取广告位
	 * 
	 * @return 广告位
	 */
	public AdPosition getAdPosition() {
		return adPosition;
	}

	/**
	 * 设置广告位
	 * 
	 * @param adPosition
	 *            广告位
	 */
	public void setAdPosition(AdPosition adPosition) {
		this.adPosition = adPosition;
	}

	/**
	 * 判断是否已开始
	 * 
	 * @return 是否已开始
	 */
	@Transient
	public boolean hasBegun() {
		return getBeginDate() == null || !getBeginDate().after(new Date());
	}

	/**
	 * 判断是否已结束
	 * 
	 * @return 是否已结束
	 */
	@Transient
	public boolean hasEnded() {
		return getEndDate() != null && !getEndDate().after(new Date());
	}

}
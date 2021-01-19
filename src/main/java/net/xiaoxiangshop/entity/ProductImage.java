package net.xiaoxiangshop.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Entity - 商品图片
 * 
 */
public class ProductImage implements Serializable, Comparable<ProductImage> {

	private static final long serialVersionUID = -673883300094536107L;

	/**
	 * 文件扩展名
	 */
	public static final String FILE_EXTENSION = "jpg";

	/**
	 * 文件类型
	 */
	public static final String FILE_CONTENT_TYPE = "image/jpeg";

	/**
	 * 原图片文件名
	 */
	public static final String SOURCE_FILE_NAME = "%s_source.%s";

	/**
	 * 大图片文件名
	 */
	public static final String LARGE_FILE_NAME = "%s_large.%s";

	/**
	 * 中图片文件名
	 */
	public static final String MEDIUM_FILE_NAME = "%s_medium.%s";

	/**
	 * 缩略图文件名
	 */
	public static final String THUMBNAIL_FILE_NAME = "%s_thumbnail.%s";

	/**
	 * 原图片
	 */
	@NotEmpty
	private String source;

	/**
	 * 大图片
	 */
	@NotEmpty
	private String large;

	/**
	 * 中图片
	 */
	@NotEmpty
	private String medium;

	/**
	 * 缩略图
	 */
	@NotEmpty
	private String thumbnail;

	/**
	 * 排序
	 */
	@Min(0)
	private Integer order;

	/**
	 * 获取原图片
	 * 
	 * @return 原图片
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 设置原图片
	 * 
	 * @param source
	 *            原图片
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 获取大图片
	 * 
	 * @return 大图片
	 */
	public String getLarge() {
		return large;
	}

	/**
	 * 设置大图片
	 * 
	 * @param large
	 *            大图片
	 */
	public void setLarge(String large) {
		this.large = large;
	}

	/**
	 * 获取中图片
	 * 
	 * @return 中图片
	 */
	public String getMedium() {
		return medium;
	}

	/**
	 * 设置中图片
	 * 
	 * @param medium
	 *            中图片
	 */
	public void setMedium(String medium) {
		this.medium = medium;
	}

	/**
	 * 获取缩略图
	 * 
	 * @return 缩略图
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * 设置缩略图
	 * 
	 * @param thumbnail
	 *            缩略图
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order
	 *            排序
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param productImage
	 *            商品图片
	 * @return 比较结果
	 */
	@Override
	public int compareTo(ProductImage productImage) {
		if (productImage == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), productImage.getOrder()).toComparison();
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
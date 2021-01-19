package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableField;


/**
 * Entity - 店铺广告图片
 * 
 */
@Entity
public class StoreAdImage extends OrderedEntity<StoreAdImage> {

	private static final long serialVersionUID = -2643112399584260120L;

	/**
	 * 最大店铺广告图片数
	 */
	public static final Integer MAX_COUNT = 5;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 图片
	 */
	@NotEmpty
	private String image;

	/**
	 * 链接地址
	 */
	private String url;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

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
	 * 获取图片
	 * 
	 * @return 图片
	 */
	public String getImage() {
		return image;
	}

	/**
	 * 设置图片
	 * 
	 * @param image
	 *            图片
	 */
	public void setImage(String image) {
		this.image = image;
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
	 * 获取店铺
	 * 
	 * @return 店铺
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * 设置店铺
	 * 
	 * @param store
	 *            店铺
	 */
	public void setStore(Store store) {
		this.store = store;
	}

}
package net.xiaoxiangshop.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * <p>
 *  产品分类与店铺关连表
 * </p>
 *
 * @author 江南红衣
 * @since 2019-01-02
 */
public class ProductCategoryStore implements Serializable {

	private static final long serialVersionUID = -1657083728227173956L;

	/**
	 * 店铺
	 */
	@TableId(value = "stores_id")
	private Long storesId;

	/**
	 * 商品分类
	 */
	@TableId(value = "product_categories_id")
	private Long productCategoriesId;

	public Long getStoresId() {
		return storesId;
	}

	public void setStoresId(Long storesId) {
		this.storesId = storesId;
	}

	public Long getProductCategoriesId() {
		return productCategoriesId;
	}

	public void setProductCategoriesId(Long productCategoriesId) {
		this.productCategoriesId = productCategoriesId;
	}

}

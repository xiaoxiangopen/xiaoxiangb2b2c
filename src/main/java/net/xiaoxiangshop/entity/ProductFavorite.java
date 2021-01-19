package net.xiaoxiangshop.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 商品收藏
 * 
 */
@Entity
public class ProductFavorite extends BaseEntity<ProductFavorite> {

	private static final long serialVersionUID = 2540556338075542780L;

	/**
	 * 最大商品收藏数量
	 */
	public static final Integer MAX_PRODUCT_FAVORITE_SIZE = 10;

	/**
	 * 会员
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Member member;

	/**
	 * 商品
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Product product;

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * 
	 * @param member
	 *            会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * 
	 * @param product
	 *            商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

}
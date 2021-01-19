package net.xiaoxiangshop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 评论
 * 
 */
@Entity
public class Review extends BaseEntity<Review> {

	private static final long serialVersionUID = 8795901519290584100L;

	/**
	 * 路径
	 */
	private static final String PATH = "/review/detail/%d";

	/**
	 * 类型
	 */
	public enum Type {

		/**
		 * 好评
		 */
		POSITIVE,

		/**
		 * 中评
		 */
		MODERATE,

		/**
		 * 差评
		 */
		NEGATIVE
	}

	/**
	 * 评分
	 */
	@JsonView(BaseView.class)
	@NotNull
	@Min(1)
	@Max(5)
	private Integer score;

	/**
	 * 内容
	 */
	@JsonView(BaseView.class)
	private String content;

	/**
	 * 是否显示
	 */
	private Boolean isShow;

	/**
	 * IP
	 */
	private String ip;

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
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

	/**
	 * 评论
	 */
	@TableField(exist = false)
	private Review forReview;
	
	/**
	 * 回复
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Set<Review> replyReviews = new HashSet<>();

	/**
	 * 规格
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private List<String> specifications = new ArrayList<>();

	/**
	 * 获取评分
	 * 
	 * @return 评分
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * 设置评分
	 * 
	 * @param score
	 *            评分
	 */
	public void setScore(Integer score) {
		this.score = score;
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
	 * 获取是否显示
	 * 
	 * @return 是否显示
	 */
	public Boolean getIsShow() {
		return isShow;
	}

	/**
	 * 设置是否显示
	 * 
	 * @param isShow
	 *            是否显示
	 */
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	/**
	 * 获取IP
	 * 
	 * @return IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP
	 * 
	 * @param ip
	 *            IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

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

	/**
	 * 获取回复
	 * 
	 * @return 回复
	 */
	public Set<Review> getReplyReviews() {
		return replyReviews;
	}

	/**
	 * 设置回复
	 * 
	 * @param replyReviews
	 *            回复
	 */
	public void setReplyReviews(Set<Review> replyReviews) {
		this.replyReviews = replyReviews;
	}

	/**
	 * 获取评论
	 * 
	 * @return 评论
	 */
	public Review getForReview() {
		return forReview;
	}

	/**
	 * 设置评论
	 * 
	 * @param forReview
	 *            评论
	 */
	public void setForReview(Review forReview) {
		this.forReview = forReview;
	}

	/**
	 * 获取规格
	 * 
	 * @return 规格
	 */
	public List<String> getSpecifications() {
		return specifications;
	}

	/**
	 * 设置规格
	 * 
	 * @param specifications
	 *            规格
	 */
	public void setSpecifications(List<String> specifications) {
		this.specifications = specifications;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@JsonView(BaseView.class)
	@Transient
	public String getPath() {
		if(getProduct()!=null){
			return String.format(Review.PATH, getProduct().getId());
		}else{
			return "";
		}

	}


	/**
	 * Entity - 条目
	 * 
	 */
	public static class Entry implements Serializable {

		private static final long serialVersionUID = 4603334970331213066L;

		/**
		 * 订单项
		 */
		@NotNull
		private OrderItem orderItem;

		/**
		 * 评论
		 */
		@NotNull
		private Review review;

		/**
		 * 获取订单项
		 * 
		 * @return 订单项
		 */
		public OrderItem getOrderItem() {
			return orderItem;
		}

		/**
		 * 设置订单项
		 * 
		 * @param orderItem
		 *            订单项
		 */
		public void setOrderItem(OrderItem orderItem) {
			this.orderItem = orderItem;
		}

		/**
		 * 获取评论
		 * 
		 * @return 评论
		 */
		public Review getReview() {
			return review;
		}

		/**
		 * 设置评论
		 * 
		 * @param review
		 *            评论
		 */
		public void setReview(Review review) {
			this.review = review;
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

}
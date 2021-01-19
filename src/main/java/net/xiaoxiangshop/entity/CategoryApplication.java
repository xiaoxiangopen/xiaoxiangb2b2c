package net.xiaoxiangshop.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 经营分类申请
 * 
 */
@Entity
public class CategoryApplication extends BaseEntity<CategoryApplication> {

	private static final long serialVersionUID = 2586352101555595765L;

	/**
	 * 状态
	 */
	public enum Status implements IEnum<Integer> {

		/**
		 * 等待审核
		 */
		PENDING(0),

		/**
		 * 审核通过
		 */
		APPROVED(1),

		/**
		 * 审核失败
		 */
		FAILED(2);
		
		private int value;

		Status(final int value) {
			this.value = value;
		}
		
		@Override
		public Integer getValue() {
			return this.value;
		}
	}

	/**
	 * 状态
	 */
	@EnumValue
	private CategoryApplication.Status status;

	/**
	 * 分佣比例
	 */
	private Double rate;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

	/**
	 * 经营分类
	 */
	@TableField(exist = false)
	private ProductCategory productCategory;

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取分佣比例
	 * 
	 * @return 分佣比例
	 */
	public Double getRate() {
		return rate;
	}

	/**
	 * 设置分佣比例
	 * 
	 * @param rate
	 *            分佣比例
	 */
	public void setRate(Double rate) {
		this.rate = rate;
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
	 * 获取经营分类
	 * 
	 * @return 经营分类
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * 设置经营分类
	 * 
	 * @param productCategory
	 *            经营分类
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

}
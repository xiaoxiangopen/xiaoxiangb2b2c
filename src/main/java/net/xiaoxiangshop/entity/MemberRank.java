package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.PreRemove;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;


/**
 * Entity - 会员等级
 * 
 */
@Entity
public class MemberRank extends BaseEntity<MemberRank> {

	private static final long serialVersionUID = 3599029355500655209L;

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * ERP会员等级
	 */
//	@NotEmpty
	private String erpRank;

	/**
	 * ERP_正常会员价字段名
	 */
//	@NotEmpty
	private String erpNormalPriceColumn;

	/**
	 * ERP_促销价字段名
	 */
//	@NotEmpty
	private String erpPromotionPriceColumn;

	/**
	 * SKU_正常会员价字段名
	 */
//	@NotEmpty
	private String skuNormalPriceColumn;

	/**
	 * SKU_促销会员价字段名
	 */
//	@NotEmpty
	private String skuPromotionPriceColumn;

	/**
	 * 优惠比例
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	private Double scale;

	/**
	 * 消费金额
	 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal amount;

	/**
	 * 是否默认
	 */
//	@NotNull
	private Boolean isDefault;

	/**
	 * 是否特殊
	 */
//	@NotNull
	private Boolean isSpecial;

	//折扣率
	private String  discountRate;

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * 会员
	 */
	@TableField(exist = false)
	private Set<Member> members = new HashSet<>();


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
	 * 获取优惠比例
	 * 
	 * @return 优惠比例
	 */
	public Double getScale() {
		return scale;
	}

	/**
	 * 设置优惠比例
	 * 
	 * @param scale
	 *            优惠比例
	 */
	public void setScale(Double scale) {
		this.scale = scale;
	}

	/**
	 * 获取消费金额
	 * 
	 * @return 消费金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置消费金额
	 * 
	 * @param amount
	 *            消费金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取是否默认
	 * 
	 * @return 是否默认
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 设置是否默认
	 * 
	 * @param isDefault
	 *            是否默认
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取是否特殊
	 * 
	 * @return 是否特殊
	 */
	public Boolean getIsSpecial() {
		return isSpecial;
	}

	/**
	 * 设置是否特殊
	 * 
	 * @param isSpecial
	 *            是否特殊
	 */
	public void setIsSpecial(Boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	/**
	 * 获取会员
	 * 
	 * @return 会员
	 */
	public Set<Member> getMembers() {
		return members;
	}

	/**
	 * 设置会员
	 * 
	 * @param members
	 *            会员
	 */
	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	public String getErpRank() {
		return erpRank;
	}

	public void setErpRank(String erpRank) {
		this.erpRank = erpRank;
	}

	public String getErpNormalPriceColumn() {
		return erpNormalPriceColumn;
	}

	public void setErpNormalPriceColumn(String erpNormalPriceColumn) {
		this.erpNormalPriceColumn = erpNormalPriceColumn;
	}

	public String getErpPromotionPriceColumn() {
		return erpPromotionPriceColumn;
	}

	public void setErpPromotionPriceColumn(String erpPromotionPriceColumn) {
		this.erpPromotionPriceColumn = erpPromotionPriceColumn;
	}

	public String getSkuNormalPriceColumn() {
		return skuNormalPriceColumn;
	}

	public void setSkuNormalPriceColumn(String skuNormalPriceColumn) {
		this.skuNormalPriceColumn = skuNormalPriceColumn;
	}

	public String getSkuPromotionPriceColumn() {
		return skuPromotionPriceColumn;
	}

	public void setSkuPromotionPriceColumn(String skuPromotionPriceColumn) {
		this.skuPromotionPriceColumn = skuPromotionPriceColumn;
	}



}
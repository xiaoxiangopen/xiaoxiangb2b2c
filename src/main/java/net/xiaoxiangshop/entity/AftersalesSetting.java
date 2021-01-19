package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Entity - 售后设置
 * 
 */
@Entity
public class AftersalesSetting extends BaseEntity<AftersalesSetting> {

	private static final long serialVersionUID = 328799574853181019L;

	/**
	 * 维修提示
	 */
	@Lob
	private String repairTips;

	/**
	 * 换货提示
	 */
	@Lob
	private String replacementTips;

	/**
	 * 退货提示
	 */
	@Lob
	private String returnsTips;

	/**
	 * 店铺
	 */
	private Store store;

	/**
	 * 获取维修提示
	 * 
	 * @return 维修提示
	 */
	public String getRepairTips() {
		return repairTips;
	}

	/**
	 * 设置维修提示
	 * 
	 * @param repairTips
	 *            维修提示
	 */
	public void setRepairTips(String repairTips) {
		this.repairTips = repairTips;
	}

	/**
	 * 获取换货提示
	 * 
	 * @return 换货提示
	 */
	public String getReplacementTips() {
		return replacementTips;
	}

	/**
	 * 设置换货提示
	 * 
	 * @param replacementTips
	 *            换货提示
	 */
	public void setReplacementTips(String replacementTips) {
		this.replacementTips = replacementTips;
	}

	/**
	 * 获取退货提示
	 * 
	 * @return 退货提示
	 */
	public String getReturnsTips() {
		return returnsTips;
	}

	/**
	 * 设置退货提示
	 * 
	 * @param returnsTips
	 *            退货提示
	 */
	public void setReturnsTips(String returnsTips) {
		this.returnsTips = returnsTips;
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
package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

/**
 * Entity - 换货
 * 
 */
@Entity
public class AftersalesReplacement extends Aftersales {

	private static final long serialVersionUID = 4148094121995812521L;

	/**
	 * 收货人
	 */
	@NotEmpty
	private String consignee;

	/**
	 * 地区
	 */
	@NotEmpty
	private String area;

	/**
	 * 地址
	 */
	@NotEmpty
	private String address;

	/**
	 * 电话
	 */
	@NotEmpty
	private String phone;

	/**
	 * 获取收货人
	 * 
	 * @return 收货人
	 */
	public String getConsignee() {
		return consignee;
	}

	/**
	 * 设置收货人
	 * 
	 * @param consignee
	 *            收货人
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 获取地区
	 * 
	 * @return 地区
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 *            地区
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 获取地址
	 * 
	 * @return 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * 
	 * @param address
	 *            地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取电话
	 * 
	 * @return 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * 
	 * @param phone
	 *            电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 *            地区
	 */
	@Transient
	public void setArea(Area area) {
		setArea(area != null ? area.getFullName() : null);
	}

}
package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 收货地址
 * 
 */
@Entity
public class Receiver extends BaseEntity<Receiver> {

	private static final long serialVersionUID = 2673602067029665976L;

	/**
	 * 收货地址最大保存数
	 */
	public static final Integer MAX_RECEIVER_COUNT = 10;

	/**
	 * 收货人
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	private String consignee;

	/**
	 * 地区名称
	 */
	@JsonView(BaseView.class)
	private String areaName;

	/**
	 * 地址
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	private String address;

	/**
	 * 邮编
	 */
	@JsonView(BaseView.class)
	private String zipCode;

	/**
	 * 电话
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	private String phone;

	/**
	 * 是否默认
	 */
	@JsonView(BaseView.class)
	@NotNull
	private Boolean isDefault;

	/**
	 * 地区
	 */
	@TableField(exist = false)
	private Area area;

	/**
	 * 会员
	 */
	@TableField(exist = false)
	private Member member;

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
	 * 获取地区名称
	 * 
	 * @return 地区名称
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置地区名称
	 * 
	 * @param areaName
	 *            地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	 * 获取邮编
	 * 
	 * @return 邮编
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * 
	 * @param zipCode
	 *            邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
	 * 获取地区
	 * 
	 * @return 地区
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 *            地区
	 */
	public void setArea(Area area) {
		this.area = area;
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

}
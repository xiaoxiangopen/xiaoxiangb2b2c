package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 售后
 * 
 */
@Entity
public class Aftersales extends BaseEntity<Aftersales> {

	private static final long serialVersionUID = 3822083093484759909L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 维修
		 */
		AFTERSALES_REPAIR(0),

		/**
		 * 换货
		 */
		AFTERSALES_REPLACEMENT(1),

		/**
		 * 退货
		 */
		AFTERSALES_RETURNS(2);

		private int value;

		Type(final int value) {
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
		FAILED(2),

		/**
		 * 已完成
		 */
		COMPLETED(3),

		/**
		 * 已取消
		 */
		CANCELED(4);

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
	 * 类型
	 */
	@JsonView(BaseView.class)
	private Aftersales.Type type;
	
	/**
	 * 状态
	 */
	@JsonView(BaseView.class)
	@NotNull
	private Aftersales.Status status;

	/**
	 * 原因
	 */
	@NotEmpty
	@Lob
	private String reason;

	/**
	 * 物流公司
	 */
	private String deliveryCorp;

	/**
	 * 运单号
	 */
	private String trackingNo;

	/**
	 * 物流公司代码
	 */
	private String deliveryCorpCode;


	private String consignee;
	private String area;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	private String address;
	private String phone;

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 退款方式
	 */
	@NotNull
	private AftersalesReturns.Method method;

	public AftersalesReturns.Method getMethod() {
		return method;
	}

	public void setMethod(AftersalesReturns.Method method) {
		this.method = method;
	}

	private String bank;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	private String account;

	/**
	 * 售后项
	 */
	@JsonIgnore
	//@JsonView(BaseView.class)
	@TableField(exist = false)
	private List<AftersalesItem> aftersalesItems = new ArrayList<>();

	/**
	 * 会员
	 */
	@JsonIgnore
	@TableField(exist = false)
	private Member member;

	/**
	 * 店铺
	 */
	@JsonIgnore
	//@JsonView(BaseView.class)
	@TableField(exist = false)
	private Store store;
	
	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	@JsonView(BaseView.class)
	public Aftersales.Type getType() {
		return type;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setType(Aftersales.Type type) {
		this.type = type;
	}
	
	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public Aftersales.Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(Aftersales.Status status) {
		this.status = status;
	}

	/**
	 * 获取原因
	 * 
	 * @return 原因
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * 设置原因
	 * 
	 * @param reason
	 *            原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 获取物流公司
	 * 
	 * @return 物流公司
	 */
	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	/**
	 * 设置物流公司
	 * 
	 * @param deliveryCorp
	 *            物流公司
	 */
	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	/**
	 * 获取运单号
	 * 
	 * @return 运单号
	 */
	public String getTrackingNo() {
		return trackingNo;
	}

	/**
	 * 设置运单号
	 * 
	 * @param trackingNo
	 *            运单号
	 */
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	/**
	 * 获取物流公司代码
	 * 
	 * @return 物流公司代码
	 */
	public String getDeliveryCorpCode() {
		return deliveryCorpCode;
	}

	/**
	 * 设置物流公司代码
	 * 
	 * @param deliveryCorpCode
	 *            物流公司代码
	 */
	public void setDeliveryCorpCode(String deliveryCorpCode) {
		this.deliveryCorpCode = deliveryCorpCode;
	}

	/**
	 * 获取售后项
	 * 
	 * @return 售后项
	 */
	public List<AftersalesItem> getAftersalesItems() {
		return aftersalesItems;
	}

	/**
	 * 设置售后项
	 * 
	 * @param aftersalesItems
	 *            售后项
	 */
	public void setAftersalesItems(List<AftersalesItem> aftersalesItems) {
		this.aftersalesItems = aftersalesItems;
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
	 * 设置物流公司
	 * 
	 * @param deliveryCorp
	 *            物流公司
	 */
	@Transient
	public void setDeliveryCorp(DeliveryCorp deliveryCorp) {
		setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
	}

	/**
	 * 设置物流公司代码
	 * 
	 * @param deliveryCorp
	 *            物流公司
	 */
	@Transient
	public void setDeliveryCorpCode(DeliveryCorp deliveryCorp) {
		setDeliveryCorpCode(deliveryCorp != null ? deliveryCorp.getCode() : null);
	}

	/**
	 * 获取订单项
	 * 
	 * @return 订单项
	 */
	@JsonView(BaseView.class)
	@SuppressWarnings("unchecked")
	@Transient
	public List<OrderItem> getOrderItems() {
		return (List<OrderItem>) CollectionUtils.collect(getAftersalesItems(), new Transformer() {

			@Override
			public Object transform(Object object) {
				AftersalesItem aftersalesItem = (AftersalesItem) object;
				return aftersalesItem.getOrderItem();
			}

		});
	}


}
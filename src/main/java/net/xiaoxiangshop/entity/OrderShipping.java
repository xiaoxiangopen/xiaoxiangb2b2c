package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 订单发货
 * 
 */
@Entity
public class OrderShipping extends BaseEntity<OrderShipping> {

	private static final long serialVersionUID = -261737051893669935L;

	/**
	 * 配送验证组
	 */
	public interface Delivery extends Default {

	}

	/**
	 * 编号
	 */
	@JsonView(BaseView.class)
	private String sn;

	/**
	 * 配送方式
	 */
	@JsonView(BaseView.class)
	private String shippingMethod;

	/**
	 * 物流公司
	 */
	@JsonView(BaseView.class)
	@NotEmpty(groups = Delivery.class)
	private String deliveryCorp;

	/**
	 * 物流公司网址
	 */
	@JsonView(BaseView.class)
	private String deliveryCorpUrl;

	/**
	 * 物流公司代码
	 */
	@JsonView(BaseView.class)
	private String deliveryCorpCode;

	/**
	 * 运单号
	 */
	@JsonView(BaseView.class)
	private String trackingNo;

	/**
	 * 物流费用
	 */
	@JsonView(BaseView.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal freight;

	/**
	 * 收货人
	 */
	@JsonView(BaseView.class)
	@NotEmpty(groups = Delivery.class)
	private String consignee;

	/**
	 * 地区
	 */
	@JsonView(BaseView.class)
	@NotEmpty(groups = Delivery.class)
	private String area;

	/**
	 * 地址
	 */
	@JsonView(BaseView.class)
	@NotEmpty(groups = Delivery.class)
	private String address;

	/**
	 * 邮编
	 */
	@JsonView(BaseView.class)
	@NotEmpty(groups = Delivery.class)
	private String zipCode;

	/**
	 * 电话
	 */
	@JsonView(BaseView.class)
	@NotEmpty(groups = Delivery.class)
	private String phone;

	/**
	 * 备注
	 */
	@JsonView(BaseView.class)
	private String memo;

	/**
	 * 订单
	 */
	@TableField(exist = false)
	private Order order;

	/**
	 * 订单发货项
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private List<OrderShippingItem> orderShippingItems = new ArrayList<>();

	/**
	 * 获取编号
	 * 
	 * @return 编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * 
	 * @param sn
	 *            编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取配送方式
	 * 
	 * @return 配送方式
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * 设置配送方式
	 * 
	 * @param shippingMethod
	 *            配送方式
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
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
	 * 获取物流公司网址
	 * 
	 * @return 物流公司网址
	 */
	public String getDeliveryCorpUrl() {
		return deliveryCorpUrl;
	}

	/**
	 * 设置物流公司网址
	 * 
	 * @param deliveryCorpUrl
	 *            物流公司网址
	 */
	public void setDeliveryCorpUrl(String deliveryCorpUrl) {
		this.deliveryCorpUrl = deliveryCorpUrl;
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
	 * 获取物流费用
	 * 
	 * @return 物流费用
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * 设置物流费用
	 * 
	 * @param freight
	 *            物流费用
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

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
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * 
	 * @param memo
	 *            备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * 
	 * @param order
	 *            订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 获取订单发货项
	 * 
	 * @return 订单发货项
	 */
	public List<OrderShippingItem> getOrderShippingItems() {
		return orderShippingItems;
	}

	/**
	 * 设置订单发货项
	 * 
	 * @param orderShippingItems
	 *            订单发货项
	 */
	public void setOrderShippingItems(List<OrderShippingItem> orderShippingItems) {
		this.orderShippingItems = orderShippingItems;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	@Transient
	public int getQuantity() {
		int quantity = 0;
		if (getOrderShippingItems() != null) {
			for (OrderShippingItem orderShippingItem : getOrderShippingItems()) {
				if (orderShippingItem != null && orderShippingItem.getQuantity() != null) {
					quantity += orderShippingItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	/**
	 * 获取是否需要物流
	 * 
	 * @return 是否需要物流
	 */
	@Transient
	public boolean getIsDelivery() {
		return CollectionUtils.exists(getOrderShippingItems(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				OrderShippingItem orderShippingItem = (OrderShippingItem) object;
				return orderShippingItem != null && BooleanUtils.isTrue(orderShippingItem.getIsDelivery());
			}
		});
	}

	/**
	 * 设置配送方式
	 * 
	 * @param shippingMethod
	 *            配送方式
	 */
	@Transient
	public void setShippingMethod(ShippingMethod shippingMethod) {
		setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
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
		setDeliveryCorpUrl(deliveryCorp != null ? deliveryCorp.getUrl() : null);
		setDeliveryCorpCode(deliveryCorp != null ? deliveryCorp.getCode() : null);
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

	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		setSn(StringUtils.lowerCase(getSn()));
	}

}
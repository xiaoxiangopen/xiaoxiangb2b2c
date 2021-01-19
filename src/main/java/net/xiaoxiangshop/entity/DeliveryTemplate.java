package net.xiaoxiangshop.entity;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 快递单模板
 * 
 */
@Entity
public class DeliveryTemplate extends BaseEntity<DeliveryTemplate> {

	private static final long serialVersionUID = -3711024981692804054L;

	/**
	 * 属性标签名称
	 */
	private static final String ATTRIBUTE_TAG_NMAE = "{%s}";

	/**
	 * 店铺属性
	 */
	public enum StoreAttribute {

		/**
		 * 店铺名称
		 */
		STORE_NAME("name"),

		/**
		 * 店铺E-mail
		 */
		STORE_EMAIL("email"),

		/**
		 * 店铺手机
		 */
		STORE_MOBILE("mobile"),

		/**
		 * 店铺电话
		 */
		STORE_PHONE("phone"),

		/**
		 * 店铺地址
		 */
		STORE_ADDRESS("address"),

		/**
		 * 店铺邮编
		 */
		STORE_ZIP_CODE("zipCode");

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 构造方法
		 * 
		 * @param name
		 *            名称
		 */
		StoreAttribute(String name) {
			this.name = name;
		}

		/**
		 * 获取标签名称
		 * 
		 * @return 标签名称
		 */
		public String getTagName() {
			return String.format(DeliveryTemplate.ATTRIBUTE_TAG_NMAE, toString());
		}

		/**
		 * 获取值
		 * 
		 * @param store
		 *            店铺
		 * @return 值
		 */
		public String getValue(Store store) {
			if (store == null) {
				return null;
			}

			try {
				Object value = PropertyUtils.getProperty(store, name);
				return value != null ? String.valueOf(value) : StringUtils.EMPTY;
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 发货点属性
	 */
	public enum DeliveryCenterAttribute {

		/**
		 * 发货点名称
		 */
		DELIVERY_CENTER_NAME("name"),

		/**
		 * 发货点联系人
		 */
		DELIVERY_CENTER_CONTACT("contact"),

		/**
		 * 发货点地区
		 */
		DELIVERY_CENTER_AREA_NAME("areaName"),

		/**
		 * 发货点地址
		 */
		DELIVERY_CENTER_ADDRESS("address"),

		/**
		 * 发货点邮编
		 */
		DELIVERY_CENTER_ZIP_CODE("zipCode"),

		/**
		 * 发货点电话
		 */
		DELIVERY_CENTER_PHONE("phone"),

		/**
		 * 发货点手机
		 */
		DELIVERY_CENTER_MOBILE("mobile");

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 构造方法
		 * 
		 * @param name
		 *            名称
		 */
		DeliveryCenterAttribute(String name) {
			this.name = name;
		}

		/**
		 * 获取标签名称
		 * 
		 * @return 标签名称
		 */
		public String getTagName() {
			return String.format(DeliveryTemplate.ATTRIBUTE_TAG_NMAE, toString());
		}

		/**
		 * 获取值
		 * 
		 * @param deliveryCenter
		 *            发货点
		 * @return 值
		 */
		public String getValue(DeliveryCenter deliveryCenter) {
			if (deliveryCenter == null) {
				return null;
			}

			try {
				Object value = PropertyUtils.getProperty(deliveryCenter, name);
				return value != null ? String.valueOf(value) : StringUtils.EMPTY;
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 订单属性
	 */
	public enum OrderAttribute {

		/**
		 * 订单编号
		 */
		ORDER_SN("sn"),

		/**
		 * 订单收货人
		 */
		ORDER_CONSIGNEE("consignee"),

		/**
		 * 订单收货地区
		 */
		ORDER_AREA_NAME("areaName"),

		/**
		 * 订单收货地址
		 */
		ORDER_ADDRESS("address"),

		/**
		 * 订单收货邮编
		 */
		ORDER_ZIP_CODE("zipCode"),

		/**
		 * 订单收货电话
		 */
		ORDER_PHONE("phone"),

		/**
		 * 订单附言
		 */
		ORDER_MEMO("memo");

		/**
		 * 名称
		 */
		private String name;

		/**
		 * 构造方法
		 * 
		 * @param name
		 *            名称
		 */
		OrderAttribute(String name) {
			this.name = name;
		}

		/**
		 * 获取标签名称
		 * 
		 * @return 标签名称
		 */
		public String getTagName() {
			return String.format(DeliveryTemplate.ATTRIBUTE_TAG_NMAE, toString());
		}

		/**
		 * 获取值
		 * 
		 * @param order
		 *            订单
		 * @return 值
		 */
		public String getValue(Order order) {
			if (order == null) {
				return null;
			}

			try {
				Object value = PropertyUtils.getProperty(order, name);
				return value != null ? String.valueOf(value) : StringUtils.EMPTY;
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 内容
	 */
	@NotEmpty
	@Lob
	private String content;

	/**
	 * 宽度
	 */
	@NotNull
	@Min(1)
	private Integer width;

	/**
	 * 高度
	 */
	@NotNull
	@Min(1)
	private Integer height;

	/**
	 * 偏移量X
	 */
	@NotNull
	private Integer offsetX;

	/**
	 * 偏移量Y
	 */
	@NotNull
	private Integer offsetY;

	/**
	 * 背景图
	 */
	private String background;

	/**
	 * 是否默认
	 */
	@NotNull
	private Boolean isDefault;

	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

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
	 * 获取宽度
	 * 
	 * @return 宽度
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * 设置宽度
	 * 
	 * @param width
	 *            宽度
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * 获取高度
	 * 
	 * @return 高度
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * 设置高度
	 * 
	 * @param height
	 *            高度
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * 获取偏移量X
	 * 
	 * @return 偏移量X
	 */
	public Integer getOffsetX() {
		return offsetX;
	}

	/**
	 * 设置偏移量X
	 * 
	 * @param offsetX
	 *            偏移量X
	 */
	public void setOffsetX(Integer offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * 获取偏移量Y
	 * 
	 * @return 偏移量Y
	 */
	public Integer getOffsetY() {
		return offsetY;
	}

	/**
	 * 设置偏移量Y
	 * 
	 * @param offsetY
	 *            偏移量Y
	 */
	public void setOffsetY(Integer offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * 获取背景图
	 * 
	 * @return 背景图
	 */
	public String getBackground() {
		return background;
	}

	/**
	 * 设置背景图
	 * 
	 * @param background
	 *            背景图
	 */
	public void setBackground(String background) {
		this.background = background;
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
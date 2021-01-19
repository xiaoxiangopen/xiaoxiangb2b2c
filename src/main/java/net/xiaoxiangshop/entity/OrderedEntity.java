package net.xiaoxiangshop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang.builder.CompareToBuilder;

import javax.persistence.MappedSuperclass;


/**
 * Entity - 排序基类
 * 
 */
@MappedSuperclass
public abstract class OrderedEntity<T extends BaseEntity<T>> extends BaseEntity<T> implements Comparable<OrderedEntity<T>> {

	private static final long serialVersionUID = 5995013015967525827L;

	/**
	 * "排序"属性名称
	 */
	public static final String ORDER_PROPERTY_NAME = "orders";

	/**
	 * 排序
	 */
	@TableField(value = "orders")
	private Integer order;

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order
	 *            排序
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param orderEntity
	 *            排序对象
	 * @return 比较结果
	 */
	@Override
	public int compareTo(OrderedEntity<T> orderEntity) {
		if (orderEntity == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), orderEntity.getOrder()).append(getId(), orderEntity.getId()).toComparison();
	}
}
package net.xiaoxiangshop.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 店铺插件状态
 * 
 */
@Entity
public class StorePluginStatus extends BaseEntity<StorePluginStatus> {

	private static final long serialVersionUID = -2635529980974992707L;

	/**
	 * 到期日期
	 */
	private Date endDate;

	/**
	 * 插件ID
	 */
	private String pluginId;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

	/**
	 * 获取到期日期
	 * 
	 * @return 到期日期
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置到期日期
	 * 
	 * @param endDate
	 *            到期日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取插件ID
	 * 
	 * @return 插件ID
	 */
	public String getPluginId() {
		return pluginId;
	}

	/**
	 * 设置插件ID
	 * 
	 * @param pluginId
	 *            插件ID
	 */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
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
	 * 判断是否已过期
	 * 
	 * @return 是否已过期
	 */
	@Transient
	public boolean hasExpired() {
		return !getEndDate().after(new Date());
	}

}
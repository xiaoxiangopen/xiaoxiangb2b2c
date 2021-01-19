package net.xiaoxiangshop.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * Entity - 即时通讯
 * 
 */
@Entity
public class InstantMessage extends OrderedEntity<InstantMessage> {

	private static final long serialVersionUID = 163292786603104144L;

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * QQ
		 */
		QQ(0),

		/**
		 * 阿里旺旺
		 */
		ALI_TALK(1);
		
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
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 类型
	 */
	@NotNull
	private InstantMessage.Type type;

	/**
	 * 账号
	 */
	@NotNull
	private String account;

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
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public InstantMessage.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(InstantMessage.Type type) {
		this.type = type;
	}

	/**
	 * 获取账号
	 * 
	 * @return 账号
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * 设置账号
	 * 
	 * @param account
	 *            账号
	 */
	public void setAccount(String account) {
		this.account = account;
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
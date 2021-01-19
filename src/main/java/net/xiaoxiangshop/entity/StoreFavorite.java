package net.xiaoxiangshop.entity;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 店铺收藏
 * 
 */
@Entity
public class StoreFavorite extends BaseEntity<StoreFavorite> {

	private static final long serialVersionUID = -2817086898251865976L;

	/**
	 * 最大店铺收藏数
	 */
	public static final Integer MAX_STORE_FAVORITE_SIZE = 10;

	/**
	 * 会员
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Member member;

	/**
	 * 店铺
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Store store;

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

}
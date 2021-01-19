package net.xiaoxiangshop.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 咨询
 * 
 */
@Entity
public class Consultation extends BaseEntity<Consultation> {

	private static final long serialVersionUID = -3950317769006303385L;

	/**
	 * 路径
	 */
	private static final String PATH = "/consultation/detail/%d";

	/**
	 * 内容
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	@Lob
	private String content;

	/**
	 * 是否显示
	 */
	private Boolean isShow;

	/**
	 * IP
	 */
	private String ip;

	/**
	 * 会员
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Member member;

	/**
	 * 商品
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Product product;

	/**
	 * 咨询
	 */
	@TableField(exist = false)
	private Consultation forConsultation;

	/**
	 * 回复
	 */
	@JsonView(BaseView.class)
	@TableField(exist = false)
	private Set<Consultation> replyConsultations = new HashSet<>();

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

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
	 * 获取是否显示
	 * 
	 * @return 是否显示
	 */
	public Boolean getIsShow() {
		return isShow;
	}

	/**
	 * 设置是否显示
	 * 
	 * @param isShow
	 *            是否显示
	 */
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	/**
	 * 获取IP
	 * 
	 * @return IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP
	 * 
	 * @param ip
	 *            IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
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
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * 
	 * @param product
	 *            商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * 获取咨询
	 * 
	 * @return 咨询
	 */
	public Consultation getForConsultation() {
		return forConsultation;
	}

	/**
	 * 设置咨询
	 * 
	 * @param forConsultation
	 *            咨询
	 */
	public void setForConsultation(Consultation forConsultation) {
		this.forConsultation = forConsultation;
	}

	/**
	 * 获取回复
	 * 
	 * @return 回复
	 */
	public Set<Consultation> getReplyConsultations() {
		return replyConsultations;
	}

	/**
	 * 设置回复
	 * 
	 * @param replyConsultations
	 *            回复
	 */
	public void setReplyConsultations(Set<Consultation> replyConsultations) {
		this.replyConsultations = replyConsultations;
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
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@JsonView(BaseView.class)
	@Transient
	public String getPath() {
		if(getProduct()!=null){
			return String.format(Consultation.PATH, getProduct().getId());
		}else{
			return  "";
		}

	}

}
package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.elasticsearch.annotations.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 店铺
 * 
 */
@Entity
@Document(indexName = "store")
public class Store extends BaseEntity<Store> {

	private static final long serialVersionUID = -6822023942602776765L;

	/**
	 * 路径
	 */
	private static final String PATH = "/store/%d";

	/**
	 * 类型
	 */
	public enum Type implements IEnum<Integer> {

		/**
		 * 普通
		 */
		GENERAL(0),

		/**
		 * 自营
		 */
		SELF(1);
		
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
		 * 审核失败
		 */
		FAILED(1),

		/**
		 * 审核通过
		 */
		APPROVED(2),

		/**
		 * 开店成功
		 */
		SUCCESS(3);
		
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
	 * 名称
	 */
	@JsonView(BaseView.class)
	@NotEmpty
	private String name;

	/**
	 * 类型
	 */
	@JsonView(BaseView.class)
	@NotNull
	private Store.Type type;

	/**
	 * 状态
	 */
	private Store.Status status;

	/**
	 * logo
	 */
	@JsonView(BaseView.class)
	private String logo;

	/**
	 * 门店编码
	 */
	@JsonView(BaseView.class)
	private String storeNo;


	/**
	 * E-mail
	 */
	@NotEmpty
	@Email
	private String email;

	private BigDecimal businessId;

	/**
	 * 手机
	 */
	@NotEmpty
	private String mobile;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 邮编
	 */
	private String zipCode;

	/**
	 * 简介
	 */
	@Lob
	private String introduction;

	/**
	 * 管理子账号
	 */
	private String storeUsers;



	/**
	 * 搜索关键词
	 */
	private String keyword;

	/**
	 * 到期日期
	 */
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endDate;

	/**
	 * 是否启用
	 */
	@JsonView(BaseView.class)
	@NotNull
	private Boolean isEnabled;

	/**
	 * 已付保证金
	 */
	private BigDecimal bailPaid;

	/**
	 * 商家
	 */
	@TableField(exist = false)
	private Business business;

	/**
	 * 店铺等级
	 */
	@TableField(exist = false)
	private StoreRank storeRank;

	/**
	 * 店铺分类
	 */
	@TableField(exist = false)
	private StoreCategory storeCategory;

	/**
	 * 售后设置
	 */
	@TableField(exist = false)
	private AftersalesSetting aftersalesSetting;

	/**
	 * 售后
	 */
	@TableField(exist = false)
	private Set<Aftersales> aftersales;


	//地区ID，后台配置逗号隔开
	private String areaTids;

	//运费起步价
	private String maxPrice;

	//额外运费
	private BigDecimal extraFreight;

	public BigDecimal getExtraFreight() {
		return extraFreight;
	}

	public void setExtraFreight(BigDecimal extraFreight) {
		this.extraFreight = extraFreight;
	}

	public String getAreaTids() {
		return areaTids;
	}

	public void setAreaTids(String areaTids) {
		this.areaTids = areaTids;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	/**
	 * 店铺广告图片
	 */
	@TableField(exist = false)
	private Set<StoreAdImage> storeAdImages = new HashSet<>();


	public BigDecimal getBusinessId() {
		return businessId;
	}

	public void setBusinessId(BigDecimal businessId) {
		this.businessId = businessId;
	}

	/**
	 * 即时通讯
	 */
	@TableField(exist = false)
	private Set<InstantMessage> instantMessages = new HashSet<>();

	/**
	 * 店铺商品分类
	 */
	@TableField(exist = false)
	private Set<StoreProductCategory> storeProductCategories = new HashSet<>();

	public String getStoreUsers() {
		return storeUsers;
	}

	public void setStoreUsers(String storeUsers) {
		this.storeUsers = storeUsers;
	}

	/**
	 * 经营分类
	 */
	@TableField(exist = false)
	private Set<ProductCategory> productCategories = new HashSet<>();

	/**
	 * 经营分类申请
	 */
	@TableField(exist = false)
	private Set<CategoryApplication> categoryApplications = new HashSet<>();

	/**
	 * 店铺商品标签
	 */
	@TableField(exist = false)
	private Set<StoreProductTag> storeProductTags = new HashSet<>();

	/**
	 * 商品
	 */
	@TableField(exist = false)
	private Set<Product> products = new HashSet<>();


	/**
	 * 店铺插件状态
	 */
	@TableField(exist = false)
	private Set<StorePluginStatus> storePluginStatus = new HashSet<>();

	/**
	 * 订单
	 */
	@TableField(exist = false)
	private Set<Order> orders = new HashSet<>();

	/**
	 * 店铺收藏
	 */
	@TableField(exist = false)
	private Set<StoreFavorite> storeFavorites = new HashSet<>();

	/**
	 * 快递单模板
	 */
	@TableField(exist = false)
	private Set<DeliveryTemplate> deliveryTemplates = new HashSet<>();

	/**
	 * 发货点
	 */
	@TableField(exist = false)
	private Set<DeliveryCenter> deliveryCenters = new HashSet<>();

	/**
	 * 默认运费配置
	 */
	@TableField(exist = false)
	private Set<DefaultFreightConfig> defaultFreightConfigs = new HashSet<>();

	/**
	 * 地区运费配置
	 */
	@TableField(exist = false)
	private Set<AreaFreightConfig> areaFreightConfigs = new HashSet<>();

	/**
	 * 服务
	 */
	@TableField(exist = false)
	private Set<Svc> svcs = new HashSet<>();

	/**
	 * 支付事务
	 */
	@TableField(exist = false)
	private Set<PaymentTransaction> paymentTransactions = new HashSet<>();

	/**
	 * 咨询
	 */
	@TableField(exist = false)
	private Set<Consultation> consultations = new HashSet<>();

	/**
	 * 评论
	 */
	@TableField(exist = false)
	private Set<Review> reviews = new HashSet<>();

	/**
	 * 统计
	 */
	@TableField(exist = false)
	private Set<Statistic> statistics = new HashSet<>();

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
	public Store.Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Store.Type type) {
		this.type = type;
	}

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public Store.Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status
	 *            状态
	 */
	public void setStatus(Store.Status status) {
		this.status = status;
	}

	/**
	 * 获取logo
	 * 
	 * @return logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置logo
	 * 
	 * @param logo
	 *            logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * 获取E-mail
	 * 
	 * @return E-mail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置E-mail
	 * 
	 * @param email
	 *            E-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取手机
	 * 
	 * @return 手机
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置手机
	 * 
	 * @param mobile
	 *            手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	 * 获取简介
	 * 
	 * @return 简介
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * 设置简介
	 * 
	 * @param introduction
	 *            简介
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * 获取搜索关键词
	 * 
	 * @return 搜索关键词
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * 设置搜索关键词
	 * 
	 * @param keyword
	 *            搜索关键词
	 */
	public void setKeyword(String keyword) {
		if (keyword != null) {
			keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", StringUtils.EMPTY);
		}
		this.keyword = keyword;
	}

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
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取已付保证金
	 * 
	 * @return 已付保证金
	 */
	public BigDecimal getBailPaid() {
		return bailPaid;
	}

	/**
	 * 设置已付保证金
	 * 
	 * @param bailPaid
	 *            已付保证金
	 */
	public void setBailPaid(BigDecimal bailPaid) {
		this.bailPaid = bailPaid;
	}

	/**
	 * 获取商家
	 * 
	 * @return 商家
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * 设置商家
	 * 
	 * @param business
	 *            商家
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

	/**
	 * 获取店铺等级
	 * 
	 * @return 店铺等级
	 */
	public StoreRank getStoreRank() {
		return storeRank;
	}

	/**
	 * 设置店铺等级
	 * 
	 * @param storeRank
	 *            店铺等级
	 */
	public void setStoreRank(StoreRank storeRank) {
		this.storeRank = storeRank;
	}

	/**
	 * 获取店铺分类
	 * 
	 * @return 店铺分类
	 */
	public StoreCategory getStoreCategory() {
		return storeCategory;
	}

	/**
	 * 设置店铺分类
	 * 
	 * @param storeCategory
	 *            店铺分类
	 */
	public void setStoreCategory(StoreCategory storeCategory) {
		this.storeCategory = storeCategory;
	}

	/**
	 * 获取售后设置
	 * 
	 * @return 售后设置
	 */
	public AftersalesSetting getAftersalesSetting() {
		return aftersalesSetting;
	}

	/**
	 * 设置售后设置
	 * 
	 * @param aftersalesSetting
	 *            售后设置
	 */
	public void setAftersalesSetting(AftersalesSetting aftersalesSetting) {
		this.aftersalesSetting = aftersalesSetting;
	}

	/**
	 * 获取售后
	 * 
	 * @return 售后
	 */
	public Set<Aftersales> getAftersales() {
		return aftersales;
	}

	/**
	 * 设置售后
	 * 
	 * @param aftersales
	 *            售后
	 */
	public void setAftersales(Set<Aftersales> aftersales) {
		this.aftersales = aftersales;
	}

	/**
	 * 获取店铺广告图片
	 * 
	 * @return 店铺广告图片
	 */
	public Set<StoreAdImage> getStoreAdImages() {
		return storeAdImages;
	}

	/**
	 * 设置店铺广告图片
	 * 
	 * @param storeAdImages
	 *            店铺广告图片
	 */
	public void setStoreAdImages(Set<StoreAdImage> storeAdImages) {
		this.storeAdImages = storeAdImages;
	}

	/**
	 * 获取即时通讯
	 * 
	 * @return 即时通讯
	 */
	public Set<InstantMessage> getInstantMessages() {
		return instantMessages;
	}

	/**
	 * 设置即时通讯
	 * 
	 * @param instantMessages
	 *            即时通讯
	 */
	public void setInstantMessages(Set<InstantMessage> instantMessages) {
		this.instantMessages = instantMessages;
	}

	/**
	 * 获取店铺商品分类
	 * 
	 * @return 店铺商品分类
	 */
	public Set<StoreProductCategory> getStoreProductCategories() {
		return storeProductCategories;
	}

	/**
	 * 设置店铺商品分类
	 * 
	 * @param storeProductCategories
	 *            店铺商品分类
	 */
	public void setStoreProductCategories(Set<StoreProductCategory> storeProductCategories) {
		this.storeProductCategories = storeProductCategories;
	}

	/**
	 * 获取经营分类
	 * 
	 * @return 经营分类
	 */
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	/**
	 * 设置经营分类
	 * 
	 * @param productCategories
	 *            经营分类
	 */
	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	/**
	 * 获取经营分类申请
	 * 
	 * @return 经营分类申请
	 */
	public Set<CategoryApplication> getCategoryApplications() {
		return categoryApplications;
	}

	/**
	 * 设置经营分类申请
	 * 
	 * @param categoryApplications
	 *            经营分类申请
	 */
	public void setCategoryApplications(Set<CategoryApplication> categoryApplications) {
		this.categoryApplications = categoryApplications;
	}

	/**
	 * 获取店铺商品标签
	 * 
	 * @return 店铺商品标签
	 */
	public Set<StoreProductTag> getStoreProductTags() {
		return storeProductTags;
	}

	/**
	 * 设置店铺商品标签
	 * 
	 * @param storeProductTags
	 *            店铺商品标签
	 */
	public void setStoreProductTags(Set<StoreProductTag> storeProductTags) {
		this.storeProductTags = storeProductTags;
	}

	/**
	 * 获取商品
	 * 
	 * @return 商品
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 设置商品
	 * 
	 * @param products
	 *            商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}


	/**
	 * 获取店铺插件状态
	 * 
	 * @return 店铺插件状态
	 */
	public Set<StorePluginStatus> getStorePluginStatus() {
		return storePluginStatus;
	}

	/**
	 * 设置店铺插件状态
	 * 
	 * @param storePluginStatus
	 *            店铺插件状态
	 */
	public void setStorePluginStatus(Set<StorePluginStatus> storePluginStatus) {
		this.storePluginStatus = storePluginStatus;
	}

	/**
	 * 获取订单
	 * 
	 * @return 订单
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * 
	 * @param orders
	 *            订单
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取店铺收藏
	 * 
	 * @return 店铺收藏
	 */
	public Set<StoreFavorite> getStoreFavorites() {
		return storeFavorites;
	}

	/**
	 * 设置店铺收藏
	 * 
	 * @param storeFavorites
	 *            店铺收藏
	 */
	public void setStoreFavorites(Set<StoreFavorite> storeFavorites) {
		this.storeFavorites = storeFavorites;
	}

	/**
	 * 获取快递单模板
	 * 
	 * @return 快递单模板
	 */
	public Set<DeliveryTemplate> getDeliveryTemplates() {
		return deliveryTemplates;
	}

	/**
	 * 设置快递单模板
	 * 
	 * @param deliveryTemplates
	 *            快递单模板
	 */
	public void setDeliveryTemplates(Set<DeliveryTemplate> deliveryTemplates) {
		this.deliveryTemplates = deliveryTemplates;
	}

	/**
	 * 获取发货点
	 * 
	 * @return 发货点
	 */
	public Set<DeliveryCenter> getDeliveryCenters() {
		return deliveryCenters;
	}

	/**
	 * 设置发货点
	 * 
	 * @param deliveryCenters
	 *            发货点
	 */
	public void setDeliveryCenters(Set<DeliveryCenter> deliveryCenters) {
		this.deliveryCenters = deliveryCenters;
	}

	/**
	 * 获取默认运费配置
	 * 
	 * @return 默认运费配置
	 */
	public Set<DefaultFreightConfig> getDefaultFreightConfigs() {
		return defaultFreightConfigs;
	}

	/**
	 * 设置默认运费配置
	 * 
	 * @param defaultFreightConfigs
	 *            默认运费配置
	 */
	public void setDefaultFreightConfigs(Set<DefaultFreightConfig> defaultFreightConfigs) {
		this.defaultFreightConfigs = defaultFreightConfigs;
	}

	/**
	 * 获取地区运费配置
	 * 
	 * @return 地区运费配置
	 */
	public Set<AreaFreightConfig> getAreaFreightConfigs() {
		return areaFreightConfigs;
	}

	/**
	 * 设置地区运费配置
	 * 
	 * @param areaFreightConfigs
	 *            地区运费配置
	 */
	public void setAreaFreightConfigs(Set<AreaFreightConfig> areaFreightConfigs) {
		this.areaFreightConfigs = areaFreightConfigs;
	}

	/**
	 * 获取服务
	 * 
	 * @return 服务
	 */
	public Set<Svc> getSvcs() {
		return svcs;
	}

	/**
	 * 设置服务
	 * 
	 * @param svcs
	 *            服务
	 */
	public void setSvcs(Set<Svc> svcs) {
		this.svcs = svcs;
	}

	/**
	 * 获取支付事务
	 * 
	 * @return 支付事务
	 */
	public Set<PaymentTransaction> getPaymentTransactions() {
		return paymentTransactions;
	}

	/**
	 * 设置支付事务
	 * 
	 * @param paymentTransactions
	 *            支付事务
	 */
	public void setPaymentTransactions(Set<PaymentTransaction> paymentTransactions) {
		this.paymentTransactions = paymentTransactions;
	}


	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	/**
	 * 获取咨询
	 * 
	 * @return 咨询
	 */
	public Set<Consultation> getConsultations() {
		return consultations;
	}

	/**
	 * 设置咨询
	 * 
	 * @param consultations
	 *            咨询
	 */
	public void setConsultations(Set<Consultation> consultations) {
		this.consultations = consultations;
	}

	/**
	 * 获取评论
	 * 
	 * @return 评论
	 */
	public Set<Review> getReviews() {
		return reviews;
	}

	/**
	 * 设置评论
	 * 
	 * @param reviews
	 *            评论
	 */
	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	/**
	 * 获取统计
	 * 
	 * @return 统计
	 */
	public Set<Statistic> getStatistics() {
		return statistics;
	}

	/**
	 * 设置统计
	 * 
	 * @param statistics
	 *            统计
	 */
	public void setStatistics(Set<Statistic> statistics) {
		this.statistics = statistics;
	}

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@JsonView(BaseView.class)
	@Transient
	public String getPath() {
		return String.format(Store.PATH, getId());
	}

	/**
	 * 获取应付保证金
	 * 
	 * @return 应付保证金
	 */
	@JsonIgnore
	@Transient
	public BigDecimal getBailPayable() {
		if (Store.Status.APPROVED.equals(getStatus())) {
			BigDecimal bailPayable = getStoreCategory().getBail().subtract(getBailPaid());
			return bailPayable.compareTo(BigDecimal.ZERO) >= 0 ? bailPayable : BigDecimal.ZERO;
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 判断是否为自营店铺
	 * 
	 * @return 是否为自营店铺
	 */
	@Transient
	public boolean isSelf() {
		return Store.Type.SELF.equals(getType());
	}

	/**
	 * 判断店铺是否有效
	 * 
	 * @return 店铺是否有效
	 */
	@Transient
	public boolean isActive() {
		return BooleanUtils.isTrue(getIsEnabled()) && Status.SUCCESS.equals(getStatus()) && !hasExpired();
	}

	/**
	 * 判断店铺是否已过期
	 * 
	 * @return 店铺是否已过期
	 */
	@JsonView(BaseView.class)
	@Transient
	public boolean hasExpired() {
		return getEndDate() != null && !getEndDate().after(new Date());
	}


	/**
	 * 判断促销插件是否有效
	 * 
	 * @param promotionPluginId
	 *            促销插件ID
	 * @return 促销插件是否有效
	 */
	@Transient
	public boolean isPromotionPluginActive(final String promotionPluginId) {
		return isSelf() || CollectionUtils.exists(getStorePluginStatus(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				StorePluginStatus storePluginStatus = (StorePluginStatus) object;
				return storePluginStatus != null && StringUtils.equals(storePluginStatus.getPluginId(), promotionPluginId) && !storePluginStatus.hasExpired();
			}
		});
	}

	/**
	 * 获取促销插件到期日期
	 * 
	 * @param promotionPluginId
	 *            促销插件ID
	 * @return 促销插件到期日期
	 */
	@JsonIgnore
	@Transient
	public Date getPromotionPluginEndDate(final String promotionPluginId) {
		StorePluginStatus storePluginStatus = (StorePluginStatus) CollectionUtils.find(getStorePluginStatus(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				StorePluginStatus storePluginStatus = (StorePluginStatus) object;
				return storePluginStatus != null && StringUtils.equals(storePluginStatus.getPluginId(), promotionPluginId);
			}
		});
		return storePluginStatus != null ? storePluginStatus.getEndDate() : null;
	}

}
package net.xiaoxiangshop;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import freemarker.template.TemplateException;
import net.xiaoxiangshop.util.FreeMarkerUtils;

/**
 * 系统设置
 * 
 */
public class Setting implements Serializable {

	private static final long serialVersionUID = -1478999889661796840L;

	/**
	 * 缓存名称
	 */
	public static final String CACHE_NAME = "setting";

	/**
	 * 分隔符
	 */
	private static final String SEPARATOR = ",";

	/**
	 * 水印位置
	 */
	public enum WatermarkPosition {

		/**
		 * 无
		 */
		NO,

		/**
		 * 左上
		 */
		TOP_LEFT,

		/**
		 * 右上
		 */
		TOP_RIGHT,

		/**
		 * 居中
		 */
		CENTER,

		/**
		 * 左下
		 */
		BOTTOM_LEFT,

		/**
		 * 右下
		 */
		BOTTOM_RIGHT
	}

	/**
	 * 小数位精确方式
	 */
	public enum RoundType {

		/**
		 * 四舍五入
		 */
		ROUND_HALF_UP,

		/**
		 * 向上取整
		 */
		ROUND_UP,

		/**
		 * 向下取整
		 */
		ROUND_DOWN
	}

	/**
	 * 注册类型
	 */
	public enum RegisterType {

		/**
		 * 会员
		 */
		MEMBER,

		/**
		 * 商家
		 */
		BUSINESS
	}

	/**
	 * 验证码类型
	 */
	public enum CaptchaType {

		/**
		 * 会员登录
		 */
		MEMBER_LOGIN,

		/**
		 * 会员注册
		 */
		MEMBER_REGISTER,

		/**
		 * 商家注册
		 */
		BUSINESS_REGISTER,

		/**
		 * 商家登录
		 */
		BUSINESS_LOGIN,

		/**
		 * 后台登录
		 */
		ADMIN_LOGIN,

		/**
		 * 商品评论
		 */
		REVIEW,

		/**
		 * 商品咨询
		 */
		CONSULTATION,

		/**
		 * 忘记密码
		 */
		FORGOT_PASSWORD,

		/**
		 * 重置密码
		 */
		RESET_PASSWORD,

		/**
		 * 其它
		 */
		OTHER
	}

	/**
	 * 库存分配时间点
	 */
	public enum StockAllocationTime {

		/**
		 * 下订单
		 */
		ORDER,

		/**
		 * 订单支付
		 */
		PAYMENT,

		/**
		 * 订单发货
		 */
		SHIP
	}

	/**
	 * 区域设置
	 */
	public enum Locale {

		/**
		 * 中文(简体, 中国)
		 */
		zh_CN,

		/**
		 * 中文(繁体, 台湾)
		 */
		zh_TW,

		/**
		 * 英文(美国)
		 */
		en_US
	}

	/**
	 * 网站名称
	 */
	@NotEmpty
	private String siteName;

	/**
	 * 网站网址
	 */
	@NotEmpty
	private String siteUrl;

	/**
	 * logo
	 */
	@NotEmpty
	private String logo;
	/**
	 * h5 logo
	 */
	@NotEmpty
	private String mobilelogo;


	/**
	 * 热门搜索
	 */
	private String hotSearch;

	/**
	 * 联系地址
	 */
	private String address;

	/**
	 * 联系电话
	 */
	@Pattern(regexp = "^\\d{3,4}-?\\d{7,9}$")
	private String phone;

	/**
	 * 邮政编码
	 */
	@Pattern(regexp = "^\\d{6}$")
	private String zipCode;

	/**
	 * E-mail
	 */
	@Email
	private String email;

	/**
	 * 备案编号
	 */
	private String certtext;

	/**
	 * 商品图片(大)宽度
	 */
	@NotNull
	@Min(1)
	private Integer largeProductImageWidth;

	/**
	 * 商品图片(大)高度
	 */
	@NotNull
	@Min(1)
	private Integer largeProductImageHeight;

	/**
	 * 商品图片(中)宽度
	 */
	@NotNull
	@Min(1)
	private Integer mediumProductImageWidth;

	/**
	 * 商品图片(中)高度
	 */
	@NotNull
	@Min(1)
	private Integer mediumProductImageHeight;

	/**
	 * 商品缩略图宽度
	 */
	@NotNull
	@Min(1)
	private Integer thumbnailProductImageWidth;

	/**
	 * 商品缩略图高度
	 */
	@NotNull
	@Min(1)
	private Integer thumbnailProductImageHeight;

	/**
	 * 默认商品图片(大)
	 */
	@NotEmpty
	private String defaultLargeProductImage;

	/**
	 * 默认商品图片(小)
	 */
	@NotEmpty
	private String defaultMediumProductImage;

	/**
	 * 默认缩略图
	 */
	@NotEmpty
	private String defaultThumbnailProductImage;

	/**
	 * 默认店铺logo
	 */
	@NotEmpty
	private String defaultStoreLogo;

	/**
	 * 水印透明度
	 */
	@NotNull
	@Min(0)
	@Max(100)
	private Integer watermarkAlpha;

	/**
	 * 水印图片
	 */
	private String watermarkImage;

	/**
	 * 水印位置
	 */
	@NotNull
	private Setting.WatermarkPosition watermarkPosition;

	/**
	 * 价格精确位数
	 */
	@NotNull
	@Min(0)
	@Max(3)
	private Integer priceScale;

	/**
	 * 价格精确方式
	 */
	@NotNull
	private Setting.RoundType priceRoundType;

	/**
	 * 是否前台显示市场价
	 */
	@NotNull
	private Boolean isShowMarketPrice;

	/**
	 * 默认市场价换算比例
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	private Double defaultMarketPriceScale;

	/**
	 * 允许注册类型
	 */
	private Setting.RegisterType[] allowedRegisterTypes;

	/**
	 * 注册初始积分
	 */
	@NotNull
	@Min(0)
	private Long registerPoint;

	/**
	 * 验证码类型
	 */
	private Setting.CaptchaType[] captchaTypes;

	/**
	 * 最大登录失败尝试次数
	 */
	@Min(1)
	private Integer maxFailedLoginAttempts;

	/**
	 * 密码锁定时间
	 */
	@NotNull
	@Min(1)
	private Integer passwordLockTime;

	/**
	 * 安全密匙有效时间
	 */
	@NotNull
	@Min(0)
	private Integer safeKeyExpiryTime;

	/**
	 * 上传文件最大限制
	 */
	@NotNull
	@Min(0)
	private Integer uploadMaxSize;

	/**
	 * 允许上传图片扩展名
	 */
	private String uploadImageExtension;

	/**
	 * 允许上传媒体扩展名
	 */
	private String uploadMediaExtension;

	/**
	 * 允许上传文件扩展名
	 */
	private String uploadFileExtension;

	/**
	 * 图片上传路径
	 */
	@NotEmpty
	private String imageUploadPath;

	/**
	 * 媒体上传路径
	 */
	@NotEmpty
	private String mediaUploadPath;

	/**
	 * 文件上传路径
	 */
	@NotEmpty
	private String fileUploadPath;

	/**
	 * SMTP服务器地址
	 */
	@NotEmpty
	private String smtpHost;

	/**
	 * SMTP服务器端口
	 */
	@NotNull
	@Min(0)
	private Integer smtpPort;

	/**
	 * SMTP用户名
	 */
	@NotEmpty
	private String smtpUsername;

	/**
	 * SMTP密码
	 */
	private String smtpPassword;

	/**
	 * SMTP是否启用SSL
	 */
	@NotNull
	private Boolean smtpSSLEnabled;

	/**
	 * 发件人邮箱
	 */
	@NotEmpty
	@Email
	private String smtpFromMail;

	/**
	 * 货币符号
	 */
	@NotEmpty
	private String currencySign;

	/**
	 * 货币单位
	 */
	@NotEmpty
	private String currencyUnit;

	/**
	 * 库存警告数
	 */
	@NotNull
	@Min(0)
	private Integer stockAlertCount;

	/**
	 * 自动收货时间
	 */
	@NotNull
	@Min(0)
	private Integer automaticReceiveTime;

	/**
	 * 库存分配时间点
	 */
	@NotNull
	private Setting.StockAllocationTime stockAllocationTime;

	/**
	 * 默认积分换算比例
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	private Double defaultPointScale;

	/**
	 * 最大积分换算比例
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	private Double maxPointScale;

	/**
	 * 会员最小提现金额
	 */
	@NotNull
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal memberMinimumCashAmount;

	/**
	 * 商家最小提现金额
	 */
	@NotNull
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal businessMinimumCashAmount;

	/**
	 * 分销佣金比例
	 */
	private Double[] distributionCommissionRates;

	/**
	 * 是否开启开发模式
	 */
	@NotNull
	private Boolean isDevelopmentEnabled;

	/**
	 * 是否开启评论
	 */
	@NotNull
	private Boolean isReviewEnabled;

	/**
	 * 是否审核评论
	 */
	@NotNull
	private Boolean isReviewCheck;

	/**
	 * 是否开启咨询
	 */
	@NotNull
	private Boolean isConsultationEnabled;

	/**
	 * 是否审核咨询
	 */
	@NotNull
	private Boolean isConsultationCheck;

	/**
	 * 是否开启发票功能
	 */
	@NotNull
	private Boolean isInvoiceEnabled;

	/**
	 * 是否开启含税价
	 */
	@NotNull
	private Boolean isTaxPriceEnabled;

	/**
	 * 税率
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	private Double taxRate;

	/**
	 * Cookie路径
	 */
	@NotEmpty
	private String cookiePath;

	/**
	 * Cookie作用域
	 */
	private String cookieDomain;

	/**
	 * 快递100公司编号
	 */
	private String kuaidi100Customer;

	/**
	 * 快递100授权KEY
	 */
	private String kuaidi100Key;

	/**
	 * 是否开启CNZZ统计
	 */
	@Null
	private Boolean isCnzzEnabled;

	/**
	 * CNZZ统计站点ID
	 */
	@Null
	private String cnzzSiteId;

	/**
	 * CNZZ统计密码
	 */
	@Null
	private String cnzzPassword;

	/**
	 * 短信AppId
	 */
	private String smsAppId;

	/**
	 * 短信服务密钥
	 */
	private String smsSecretKey;
	/**
	 * 短信服务请求服务地址
	 */
	private String youyiSmsUrl;
	/**
	 * 短信服务-绑定会员短信模板
	 */
	private String youyiHySmsTemplate;
	/**
	 *短信服务-登陆短信验证码短信模板
	 */
	private String youyiLoginSmsTemplate;
	/**
	 *短信服务-修改密码短信验证码短信模板
	 */
	private String youyiEditPwdSmsTemplate;

	/**
	 * 短信服务-验证码有效时间（秒）
	 */
	private Integer youyiHySmsCodeTime;

	private String zgdmCScategoryCode;

	private String zgdmCScategorySubCode;

	private String zgdmCScategoryName;
	/**
	 * 区域设置
	 */
	@NotNull
	private Setting.Locale locale;

	/**
	 * 获取网站名称
	 * 
	 * @return 网站名称
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * 设置网站名称
	 * 
	 * @param siteName
	 *            网站名称
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * 获取网站网址
	 * 
	 * @return 网站网址
	 */
	public String getSiteUrl() {
		return siteUrl;
	}

	/**
	 * 设置网站网址
	 * 
	 * @param siteUrl
	 *            网站网址
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = StringUtils.removeEnd(siteUrl, "/");
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

	public String getMobilelogo() {
		return mobilelogo;
	}

	public void setMobilelogo(String mobilelogo) {
		this.mobilelogo = mobilelogo;
	}

	/**
	 * 获取热门搜索
	 * 
	 * @return 热门搜索
	 */
	public String getHotSearch() {
		return hotSearch;
	}

	/**
	 * 设置热门搜索
	 * 
	 * @param hotSearch
	 *            热门搜索
	 */
	public void setHotSearch(String hotSearch) {
		if (hotSearch != null) {
			hotSearch = hotSearch.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", StringUtils.EMPTY);
		}
		this.hotSearch = hotSearch;
	}

	/**
	 * 获取联系地址
	 * 
	 * @return 联系地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置联系地址
	 * 
	 * @param address
	 *            联系地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取联系电话
	 * 
	 * @return 联系电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置联系电话
	 * 
	 * @param phone
	 *            联系电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取邮政编码
	 * 
	 * @return 邮政编码
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮政编码
	 * 
	 * @param zipCode
	 *            邮政编码
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
	 * 获取备案编号
	 * 
	 * @return 备案编号
	 */
	public String getCerttext() {
		return certtext;
	}

	/**
	 * 设置备案编号
	 * 
	 * @param certtext
	 *            备案编号
	 */
	public void setCerttext(String certtext) {
		this.certtext = certtext;
	}

	/**
	 * 获取商品图片(大)宽度
	 * 
	 * @return 商品图片(大)宽度
	 */
	public Integer getLargeProductImageWidth() {
		return largeProductImageWidth;
	}

	public String getZgdmCScategorySubCode() {
		return zgdmCScategorySubCode;
	}

	public void setZgdmCScategorySubCode(String zgdmCScategorySubCode) {
		this.zgdmCScategorySubCode = zgdmCScategorySubCode;
	}

	/**
	 * 设置商品图片(大)宽度
	 * 
	 * @param largeProductImageWidth
	 *            商品图片(大)宽度
	 */
	public void setLargeProductImageWidth(Integer largeProductImageWidth) {
		this.largeProductImageWidth = largeProductImageWidth;
	}

	/**
	 * 获取商品图片(大)高度
	 * 
	 * @return 商品图片(大)高度
	 */
	public Integer getLargeProductImageHeight() {
		return largeProductImageHeight;
	}

	/**
	 * 设置商品图片(大)高度
	 * 
	 * @param largeProductImageHeight
	 *            商品图片(大)高度
	 */
	public void setLargeProductImageHeight(Integer largeProductImageHeight) {
		this.largeProductImageHeight = largeProductImageHeight;
	}

	/**
	 * 获取商品图片(中)宽度
	 * 
	 * @return 商品图片(中)宽度
	 */
	public Integer getMediumProductImageWidth() {
		return mediumProductImageWidth;
	}

	/**
	 * 设置商品图片(中)宽度
	 * 
	 * @param mediumProductImageWidth
	 *            商品图片(中)宽度
	 */
	public void setMediumProductImageWidth(Integer mediumProductImageWidth) {
		this.mediumProductImageWidth = mediumProductImageWidth;
	}

	/**
	 * 获取商品图片(中)高度
	 * 
	 * @return 商品图片(中)高度
	 */
	public Integer getMediumProductImageHeight() {
		return mediumProductImageHeight;
	}

	/**
	 * 设置商品图片(中)高度
	 * 
	 * @param mediumProductImageHeight
	 *            商品图片(中)高度
	 */
	public void setMediumProductImageHeight(Integer mediumProductImageHeight) {
		this.mediumProductImageHeight = mediumProductImageHeight;
	}

	public String getZgdmCScategoryCode() {
		return zgdmCScategoryCode;
	}

	public void setZgdmCScategoryCode(String zgdmCScategoryCode) {
		this.zgdmCScategoryCode = zgdmCScategoryCode;
	}

	public String getZgdmCScategoryName() {
		return zgdmCScategoryName;
	}

	public void setZgdmCScategoryName(String zgdmCScategoryName) {
		this.zgdmCScategoryName = zgdmCScategoryName;
	}

	/**
	 * 获取商品缩略图宽度
	 * 
	 * @return 商品缩略图宽度
	 */
	public Integer getThumbnailProductImageWidth() {
		return thumbnailProductImageWidth;
	}

	/**
	 * 设置商品缩略图宽度
	 * 
	 * @param thumbnailProductImageWidth
	 *            商品缩略图宽度
	 */
	public void setThumbnailProductImageWidth(Integer thumbnailProductImageWidth) {
		this.thumbnailProductImageWidth = thumbnailProductImageWidth;
	}

	/**
	 * 获取商品缩略图高度
	 * 
	 * @return 商品缩略图高度
	 */
	public Integer getThumbnailProductImageHeight() {
		return thumbnailProductImageHeight;
	}

	/**
	 * 设置商品缩略图高度
	 * 
	 * @param thumbnailProductImageHeight
	 *            商品缩略图高度
	 */
	public void setThumbnailProductImageHeight(Integer thumbnailProductImageHeight) {
		this.thumbnailProductImageHeight = thumbnailProductImageHeight;
	}

	/**
	 * 获取默认商品图片(大)
	 * 
	 * @return 默认商品图片(大)
	 */
	public String getDefaultLargeProductImage() {
		return defaultLargeProductImage;
	}

	/**
	 * 设置默认商品图片(大)
	 * 
	 * @param defaultLargeProductImage
	 *            默认商品图片(大)
	 */
	public void setDefaultLargeProductImage(String defaultLargeProductImage) {
		this.defaultLargeProductImage = defaultLargeProductImage;
	}

	/**
	 * 获取默认商品图片(小)
	 * 
	 * @return 默认商品图片(小)
	 */
	public String getDefaultMediumProductImage() {
		return defaultMediumProductImage;
	}

	/**
	 * 设置默认商品图片(小)
	 * 
	 * @param defaultMediumProductImage
	 *            默认商品图片(小)
	 */
	public void setDefaultMediumProductImage(String defaultMediumProductImage) {
		this.defaultMediumProductImage = defaultMediumProductImage;
	}

	/**
	 * 获取默认缩略图
	 * 
	 * @return 默认缩略图
	 */
	public String getDefaultThumbnailProductImage() {
		return defaultThumbnailProductImage;
	}

	/**
	 * 设置默认缩略图
	 * 
	 * @param defaultThumbnailProductImage
	 *            默认缩略图
	 */
	public void setDefaultThumbnailProductImage(String defaultThumbnailProductImage) {
		this.defaultThumbnailProductImage = defaultThumbnailProductImage;
	}

	/**
	 * 获取默认店铺logo
	 * 
	 * @return 默认店铺logo
	 */
	public String getDefaultStoreLogo() {
		return defaultStoreLogo;
	}

	/**
	 * 设置默认店铺logo
	 * 
	 * @param defaultStoreLogo
	 *            默认店铺logo
	 */
	public void setDefaultStoreLogo(String defaultStoreLogo) {
		this.defaultStoreLogo = defaultStoreLogo;
	}

	/**
	 * 获取水印透明度
	 * 
	 * @return 水印透明度
	 */
	public Integer getWatermarkAlpha() {
		return watermarkAlpha;
	}

	/**
	 * 设置水印透明度
	 * 
	 * @param watermarkAlpha
	 *            水印透明度
	 */
	public void setWatermarkAlpha(Integer watermarkAlpha) {
		this.watermarkAlpha = watermarkAlpha;
	}

	/**
	 * 获取水印图片
	 * 
	 * @return 水印图片
	 */
	public String getWatermarkImage() {
		return watermarkImage;
	}

	/**
	 * 设置水印图片
	 * 
	 * @param watermarkImage
	 *            水印图片
	 */
	public void setWatermarkImage(String watermarkImage) {
		this.watermarkImage = watermarkImage;
	}

	/**
	 * 获取水印位置
	 * 
	 * @return 水印位置
	 */
	public Setting.WatermarkPosition getWatermarkPosition() {
		return watermarkPosition;
	}

	/**
	 * 设置水印位置
	 * 
	 * @param watermarkPosition
	 *            水印位置
	 */
	public void setWatermarkPosition(Setting.WatermarkPosition watermarkPosition) {
		this.watermarkPosition = watermarkPosition;
	}

	/**
	 * 获取价格精确位数
	 * 
	 * @return 价格精确位数
	 */
	public Integer getPriceScale() {
		return priceScale;
	}

	/**
	 * 设置价格精确位数
	 * 
	 * @param priceScale
	 *            价格精确位数
	 */
	public void setPriceScale(Integer priceScale) {
		this.priceScale = priceScale;
	}

	/**
	 * 获取价格精确方式
	 * 
	 * @return 价格精确方式
	 */
	public Setting.RoundType getPriceRoundType() {
		return priceRoundType;
	}

	/**
	 * 设置价格精确方式
	 * 
	 * @param priceRoundType
	 *            价格精确方式
	 */
	public void setPriceRoundType(Setting.RoundType priceRoundType) {
		this.priceRoundType = priceRoundType;
	}

	/**
	 * 获取是否前台显示市场价
	 * 
	 * @return 是否前台显示市场价
	 */
	public Boolean getIsShowMarketPrice() {
		return isShowMarketPrice;
	}

	/**
	 * 设置是否前台显示市场价
	 * 
	 * @param isShowMarketPrice
	 *            是否前台显示市场价
	 */
	public void setIsShowMarketPrice(Boolean isShowMarketPrice) {
		this.isShowMarketPrice = isShowMarketPrice;
	}

	/**
	 * 获取默认市场价换算比例
	 * 
	 * @return 默认市场价换算比例
	 */
	public Double getDefaultMarketPriceScale() {
		return defaultMarketPriceScale;
	}

	/**
	 * 设置默认市场价换算比例
	 * 
	 * @param defaultMarketPriceScale
	 *            默认市场价换算比例
	 */
	public void setDefaultMarketPriceScale(Double defaultMarketPriceScale) {
		this.defaultMarketPriceScale = defaultMarketPriceScale;
	}

	/**
	 * 获取允许注册类型
	 * 
	 * @return 允许注册类型
	 */

	public Setting.RegisterType[] getAllowedRegisterTypes() {
		return allowedRegisterTypes;
	}

	/**
	 * 设置允许注册类型
	 * 
	 * @param allowedRegisterTypes
	 *            允许注册类型
	 */
	public void setAllowedRegisterTypes(Setting.RegisterType[] allowedRegisterTypes) {
		this.allowedRegisterTypes = allowedRegisterTypes;
	}

	/**
	 * 获取注册初始积分
	 * 
	 * @return 注册初始积分
	 */
	public Long getRegisterPoint() {
		return registerPoint;
	}

	/**
	 * 设置注册初始积分
	 * 
	 * @param registerPoint
	 *            注册初始积分
	 */
	public void setRegisterPoint(Long registerPoint) {
		this.registerPoint = registerPoint;
	}

	/**
	 * 获取验证码类型
	 * 
	 * @return 验证码类型
	 */
	public Setting.CaptchaType[] getCaptchaTypes() {
		return captchaTypes;
	}

	/**
	 * 设置验证码类型
	 * 
	 * @param captchaTypes
	 *            验证码类型
	 */
	public void setCaptchaTypes(Setting.CaptchaType[] captchaTypes) {
		this.captchaTypes = captchaTypes;
	}

	/**
	 * 获取最大登录失败尝试次数
	 * 
	 * @return 最大登录失败尝试次数
	 */
	public Integer getMaxFailedLoginAttempts() {
		return maxFailedLoginAttempts;
	}

	/**
	 * 设置最大登录失败尝试次数
	 * 
	 * @param maxFailedLoginAttempts
	 *            最大登录失败尝试次数
	 */
	public void setMaxFailedLoginAttempts(Integer maxFailedLoginAttempts) {
		this.maxFailedLoginAttempts = maxFailedLoginAttempts;
	}

	/**
	 * 获取密码锁定时间
	 * 
	 * @return 密码锁定时间
	 */
	public Integer getPasswordLockTime() {
		return passwordLockTime;
	}

	/**
	 * 设置密码锁定时间
	 * 
	 * @param passwordLockTime
	 *            密码锁定时间
	 */
	public void setPasswordLockTime(Integer passwordLockTime) {
		this.passwordLockTime = passwordLockTime;
	}

	/**
	 * 获取安全密匙有效时间
	 * 
	 * @return 安全密匙有效时间
	 */
	public Integer getSafeKeyExpiryTime() {
		return safeKeyExpiryTime;
	}

	/**
	 * 设置安全密匙有效时间
	 * 
	 * @param safeKeyExpiryTime
	 *            安全密匙有效时间
	 */
	public void setSafeKeyExpiryTime(Integer safeKeyExpiryTime) {
		this.safeKeyExpiryTime = safeKeyExpiryTime;
	}

	/**
	 * 获取上传文件最大限制
	 * 
	 * @return 上传文件最大限制
	 */
	public Integer getUploadMaxSize() {
		return uploadMaxSize;
	}

	/**
	 * 设置上传文件最大限制
	 * 
	 * @param uploadMaxSize
	 *            上传文件最大限制
	 */
	public void setUploadMaxSize(Integer uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	/**
	 * 获取允许上传图片扩展名
	 * 
	 * @return 允许上传图片扩展名
	 */
	public String getUploadImageExtension() {
		return uploadImageExtension;
	}

	/**
	 * 设置允许上传图片扩展名
	 * 
	 * @param uploadImageExtension
	 *            允许上传图片扩展名
	 */
	public void setUploadImageExtension(String uploadImageExtension) {
		if (uploadImageExtension != null) {
			uploadImageExtension = uploadImageExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", StringUtils.EMPTY).toLowerCase();
		}
		this.uploadImageExtension = uploadImageExtension;
	}

	/**
	 * 获取允许上传媒体扩展名
	 * 
	 * @return 允许上传媒体扩展名
	 */
	public String getUploadMediaExtension() {
		return uploadMediaExtension;
	}

	/**
	 * 设置允许上传媒体扩展名
	 * 
	 * @param uploadMediaExtension
	 *            允许上传媒体扩展名
	 */
	public void setUploadMediaExtension(String uploadMediaExtension) {
		if (uploadMediaExtension != null) {
			uploadMediaExtension = uploadMediaExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", StringUtils.EMPTY).toLowerCase();
		}
		this.uploadMediaExtension = uploadMediaExtension;
	}

	/**
	 * 获取允许上传文件扩展名
	 * 
	 * @return 允许上传文件扩展名
	 */
	public String getUploadFileExtension() {
		return uploadFileExtension;
	}

	/**
	 * 设置允许上传文件扩展名
	 * 
	 * @param uploadFileExtension
	 *            允许上传文件扩展名
	 */
	public void setUploadFileExtension(String uploadFileExtension) {
		if (uploadFileExtension != null) {
			uploadFileExtension = uploadFileExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", StringUtils.EMPTY).toLowerCase();
		}
		this.uploadFileExtension = uploadFileExtension;
	}

	/**
	 * 获取图片上传路径
	 * 
	 * @return 图片上传路径
	 */
	public String getImageUploadPath() {
		return imageUploadPath;
	}

	/**
	 * 设置图片上传路径
	 * 
	 * @param imageUploadPath
	 *            图片上传路径
	 */
	public void setImageUploadPath(String imageUploadPath) {
		if (imageUploadPath != null) {
			if (!imageUploadPath.startsWith("/")) {
				imageUploadPath = "/" + imageUploadPath;
			}
			if (!imageUploadPath.endsWith("/")) {
				imageUploadPath += "/";
			}
		}
		this.imageUploadPath = imageUploadPath;
	}

	/**
	 * 获取媒体上传路径
	 * 
	 * @return 媒体上传路径
	 */
	public String getMediaUploadPath() {
		return mediaUploadPath;
	}

	/**
	 * 设置媒体上传路径
	 * 
	 * @param mediaUploadPath
	 *            媒体上传路径
	 */
	public void setMediaUploadPath(String mediaUploadPath) {
		if (mediaUploadPath != null) {
			if (!mediaUploadPath.startsWith("/")) {
				mediaUploadPath = "/" + mediaUploadPath;
			}
			if (!mediaUploadPath.endsWith("/")) {
				mediaUploadPath += "/";
			}
		}
		this.mediaUploadPath = mediaUploadPath;
	}

	/**
	 * 获取文件上传路径
	 * 
	 * @return 文件上传路径
	 */
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	/**
	 * 设置文件上传路径
	 * 
	 * @param fileUploadPath
	 *            文件上传路径
	 */
	public void setFileUploadPath(String fileUploadPath) {
		if (fileUploadPath != null) {
			if (!fileUploadPath.startsWith("/")) {
				fileUploadPath = "/" + fileUploadPath;
			}
			if (!fileUploadPath.endsWith("/")) {
				fileUploadPath += "/";
			}
		}
		this.fileUploadPath = fileUploadPath;
	}

	/**
	 * 获取SMTP服务器地址
	 * 
	 * @return SMTP服务器地址
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * 设置SMTP服务器地址
	 * 
	 * @param smtpHost
	 *            SMTP服务器地址
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * 获取SMTP服务器端口
	 * 
	 * @return SMTP服务器端口
	 */
	public Integer getSmtpPort() {
		return smtpPort;
	}

	/**
	 * 设置SMTP服务器端口
	 * 
	 * @param smtpPort
	 *            SMTP服务器端口
	 */
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	/**
	 * 获取SMTP用户名
	 * 
	 * @return SMTP用户名
	 */
	public String getSmtpUsername() {
		return smtpUsername;
	}

	/**
	 * 设置SMTP用户名
	 * 
	 * @param smtpUsername
	 *            SMTP用户名
	 */
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	/**
	 * 获取SMTP密码
	 * 
	 * @return SMTP密码
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}

	/**
	 * 设置SMTP密码
	 * 
	 * @param smtpPassword
	 *            SMTP密码
	 */
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	/**
	 * 获取SMTP是否启用SSL
	 * 
	 * @return SMTP是否启用SSL
	 */
	public Boolean getSmtpSSLEnabled() {
		return smtpSSLEnabled;
	}

	/**
	 * 设置SMTP是否启用SSL
	 * 
	 * @param smtpSSLEnabled
	 *            SMTP是否启用SSL
	 */
	public void setSmtpSSLEnabled(Boolean smtpSSLEnabled) {
		this.smtpSSLEnabled = smtpSSLEnabled;
	}

	/**
	 * 获取发件人邮箱
	 * 
	 * @return 发件人邮箱
	 */
	public String getSmtpFromMail() {
		return smtpFromMail;
	}

	/**
	 * 设置发件人邮箱
	 * 
	 * @param smtpFromMail
	 *            发件人邮箱
	 */
	public void setSmtpFromMail(String smtpFromMail) {
		this.smtpFromMail = smtpFromMail;
	}

	/**
	 * 获取货币符号
	 * 
	 * @return 货币符号
	 */
	public String getCurrencySign() {
		return currencySign;
	}

	/**
	 * 设置货币符号
	 * 
	 * @param currencySign
	 *            货币符号
	 */
	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}

	/**
	 * 获取货币单位
	 * 
	 * @return 货币单位
	 */
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	/**
	 * 设置货币单位
	 * 
	 * @param currencyUnit
	 *            货币单位
	 */
	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}

	/**
	 * 获取库存警告数
	 * 
	 * @return 库存警告数
	 */
	public Integer getStockAlertCount() {
		return stockAlertCount;
	}

	/**
	 * 设置库存警告数
	 * 
	 * @param stockAlertCount
	 *            库存警告数
	 */
	public void setStockAlertCount(Integer stockAlertCount) {
		this.stockAlertCount = stockAlertCount;
	}

	/**
	 * 获取自动收货时间
	 * 
	 * @return 自动收货时间
	 */
	public Integer getAutomaticReceiveTime() {
		return automaticReceiveTime;
	}

	/**
	 * 设置自动收货时间
	 * 
	 * @param automaticReceiveTime
	 *            自动收货时间
	 */
	public void setAutomaticReceiveTime(Integer automaticReceiveTime) {
		this.automaticReceiveTime = automaticReceiveTime;
	}

	/**
	 * 获取库存分配时间点
	 * 
	 * @return 库存分配时间点
	 */
	public Setting.StockAllocationTime getStockAllocationTime() {
		return stockAllocationTime;
	}

	/**
	 * 设置库存分配时间点
	 * 
	 * @param stockAllocationTime
	 *            库存分配时间点
	 */
	public void setStockAllocationTime(Setting.StockAllocationTime stockAllocationTime) {
		this.stockAllocationTime = stockAllocationTime;
	}

	/**
	 * 获取默认积分换算比例
	 * 
	 * @return 默认积分换算比例
	 */
	public Double getDefaultPointScale() {
		return defaultPointScale;
	}

	/**
	 * 设置默认积分换算比例
	 * 
	 * @param defaultPointScale
	 *            默认积分换算比例
	 */
	public void setDefaultPointScale(Double defaultPointScale) {
		this.defaultPointScale = defaultPointScale;
	}

	/**
	 * 获取最大积分换算比例
	 * 
	 * @return 最大积分换算比例
	 */
	public Double getMaxPointScale() {
		return maxPointScale;
	}

	/**
	 * 设置最大积分换算比例
	 * 
	 * @param maxPointScale
	 *            最大积分换算比例
	 */
	public void setMaxPointScale(Double maxPointScale) {
		this.maxPointScale = maxPointScale;
	}

	/**
	 * 获取会员最小提现金额
	 * 
	 * @return 会员最小提现金额
	 */
	public BigDecimal getMemberMinimumCashAmount() {
		return memberMinimumCashAmount;
	}

	/**
	 * 设置会员最小提现金额
	 * 
	 * @param memberMinimumCashAmount
	 *            会员最小提现金额
	 */
	public void setMemberMinimumCashAmount(BigDecimal memberMinimumCashAmount) {
		this.memberMinimumCashAmount = memberMinimumCashAmount;
	}

	/**
	 * 获取商家最小提现金额
	 * 
	 * @return 商家最小提现金额
	 */
	public BigDecimal getBusinessMinimumCashAmount() {
		return businessMinimumCashAmount;
	}

	/**
	 * 设置商家最小提现金额
	 * 
	 * @param businessMinimumCashAmount
	 *            商家最小提现金额
	 */
	public void setBusinessMinimumCashAmount(BigDecimal businessMinimumCashAmount) {
		this.businessMinimumCashAmount = businessMinimumCashAmount;
	}

	/**
	 * 获取分销佣金比例
	 * 
	 * @return 分销佣金比例
	 */
	public Double[] getDistributionCommissionRates() {
		return distributionCommissionRates;
	}

	/**
	 * 设置分销佣金比例
	 * 
	 * @param distributionCommissionRates
	 *            分销佣金比例
	 */
	public void setDistributionCommissionRates(Double[] distributionCommissionRates) {
		this.distributionCommissionRates = distributionCommissionRates;
	}

	/**
	 * 获取是否开启开发模式
	 * 
	 * @return 是否开启开发模式
	 */
	public Boolean getIsDevelopmentEnabled() {
		return isDevelopmentEnabled;
	}

	/**
	 * 设置是否开启开发模式
	 * 
	 * @param isDevelopmentEnabled
	 *            是否开启开发模式
	 */
	public void setIsDevelopmentEnabled(Boolean isDevelopmentEnabled) {
		this.isDevelopmentEnabled = isDevelopmentEnabled;
	}

	/**
	 * 获取是否开启评论
	 * 
	 * @return 是否开启评论
	 */
	public Boolean getIsReviewEnabled() {
		return isReviewEnabled;
	}

	/**
	 * 设置是否开启评论
	 * 
	 * @param isReviewEnabled
	 *            是否开启评论
	 */
	public void setIsReviewEnabled(Boolean isReviewEnabled) {
		this.isReviewEnabled = isReviewEnabled;
	}

	/**
	 * 获取是否审核评论
	 * 
	 * @return 是否审核评论
	 */
	public Boolean getIsReviewCheck() {
		return isReviewCheck;
	}

	/**
	 * 设置是否审核评论
	 * 
	 * @param isReviewCheck
	 *            是否审核评论
	 */
	public void setIsReviewCheck(Boolean isReviewCheck) {
		this.isReviewCheck = isReviewCheck;
	}

	/**
	 * 获取是否开启咨询
	 * 
	 * @return 是否开启咨询
	 */
	public Boolean getIsConsultationEnabled() {
		return isConsultationEnabled;
	}

	/**
	 * 设置是否开启咨询
	 * 
	 * @param isConsultationEnabled
	 *            是否开启咨询
	 */
	public void setIsConsultationEnabled(Boolean isConsultationEnabled) {
		this.isConsultationEnabled = isConsultationEnabled;
	}

	/**
	 * 获取是否审核咨询
	 * 
	 * @return 是否审核咨询
	 */
	public Boolean getIsConsultationCheck() {
		return isConsultationCheck;
	}

	/**
	 * 设置是否审核咨询
	 * 
	 * @param isConsultationCheck
	 *            是否审核咨询
	 */
	public void setIsConsultationCheck(Boolean isConsultationCheck) {
		this.isConsultationCheck = isConsultationCheck;
	}

	/**
	 * 获取是否开启发票功能
	 * 
	 * @return 是否开启发票功能
	 */
	public Boolean getIsInvoiceEnabled() {
		return isInvoiceEnabled;
	}

	/**
	 * 设置是否开启发票功能
	 * 
	 * @param isInvoiceEnabled
	 *            是否开启发票功能
	 */
	public void setIsInvoiceEnabled(Boolean isInvoiceEnabled) {
		this.isInvoiceEnabled = isInvoiceEnabled;
	}

	/**
	 * 获取是否开启含税价
	 * 
	 * @return 是否开启含税价
	 */
	public Boolean getIsTaxPriceEnabled() {
		return isTaxPriceEnabled;
	}

	/**
	 * 设置是否开启含税价
	 * 
	 * @param isTaxPriceEnabled
	 *            是否开启含税价
	 */
	public void setIsTaxPriceEnabled(Boolean isTaxPriceEnabled) {
		this.isTaxPriceEnabled = isTaxPriceEnabled;
	}

	/**
	 * 获取税率
	 * 
	 * @return 税率
	 */
	public Double getTaxRate() {
		return taxRate;
	}

	/**
	 * 设置税率
	 * 
	 * @param taxRate
	 *            税率
	 */
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * 获取Cookie路径
	 * 
	 * @return Cookie路径
	 */
	public String getCookiePath() {
		return cookiePath;
	}

	/**
	 * 设置Cookie路径
	 * 
	 * @param cookiePath
	 *            Cookie路径
	 */
	public void setCookiePath(String cookiePath) {
		if (cookiePath != null && !cookiePath.endsWith("/")) {
			cookiePath += "/";
		}
		this.cookiePath = cookiePath;
	}

	/**
	 * 获取Cookie作用域
	 * 
	 * @return Cookie作用域
	 */
	public String getCookieDomain() {
		return cookieDomain;
	}

	/**
	 * 设置Cookie作用域
	 * 
	 * @param cookieDomain
	 *            Cookie作用域
	 */
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	/**
	 * 获取快递100公司编号
	 * 
	 * @return 快递100公司编号
	 */
	public String getKuaidi100Customer() {
		return kuaidi100Customer;
	}

	/**
	 * 设置快递100公司编号
	 * 
	 * @param kuaidi100Customer
	 *            快递100公司编号
	 */
	public void setKuaidi100Customer(String kuaidi100Customer) {
		this.kuaidi100Customer = kuaidi100Customer;
	}

	/**
	 * 获取快递100授权KEY
	 * 
	 * @return 快递100授权KEY
	 */
	public String getKuaidi100Key() {
		return kuaidi100Key;
	}

	/**
	 * 设置快递100授权KEY
	 * 
	 * @param kuaidi100Key
	 *            快递100授权KEY
	 */
	public void setKuaidi100Key(String kuaidi100Key) {
		this.kuaidi100Key = kuaidi100Key;
	}

	/**
	 * 获取是否开启CNZZ统计
	 * 
	 * @return 是否开启CNZZ统计
	 */
	public Boolean getIsCnzzEnabled() {
		return isCnzzEnabled;
	}

	/**
	 * 设置是否开启CNZZ统计
	 * 
	 * @param isCnzzEnabled
	 *            是否开启CNZZ统计
	 */
	public void setIsCnzzEnabled(Boolean isCnzzEnabled) {
		this.isCnzzEnabled = isCnzzEnabled;
	}

	/**
	 * 获取CNZZ统计站点ID
	 * 
	 * @return CNZZ统计站点ID
	 */
	public String getCnzzSiteId() {
		return cnzzSiteId;
	}

	/**
	 * 设置CNZZ统计站点ID
	 * 
	 * @param cnzzSiteId
	 *            CNZZ统计站点ID
	 */
	public void setCnzzSiteId(String cnzzSiteId) {
		this.cnzzSiteId = cnzzSiteId;
	}

	/**
	 * 获取CNZZ统计密码
	 * 
	 * @return CNZZ统计密码
	 */
	public String getCnzzPassword() {
		return cnzzPassword;
	}

	/**
	 * 设置CNZZ统计密码
	 * 
	 * @param cnzzPassword
	 *            CNZZ统计密码
	 */
	public void setCnzzPassword(String cnzzPassword) {
		this.cnzzPassword = cnzzPassword;
	}

	/**
	 * 获取短信AppId
	 * 
	 * @return 短信AppId
	 */
	public String getSmsAppId() {
		return smsAppId;
	}

	/**
	 * 设置短信AppId
	 * 
	 * @param smsAppId
	 *            短信AppId
	 */
	public void setSmsAppId(String smsAppId) {
		this.smsAppId = smsAppId;
	}

	/**
	 * 获取短信服务密钥
	 * 
	 * @return 短信服务密钥
	 */
	public String getSmsSecretKey() {
		return smsSecretKey;
	}

	/**
	 * 设置短信服务密钥
	 * 
	 * @param smsSecretKey
	 *            短信服务密钥
	 */
	public void setSmsSecretKey(String smsSecretKey) {
		this.smsSecretKey = smsSecretKey;
	}

	public String getYouyiSmsUrl() {
		return youyiSmsUrl;
	}

	public void setYouyiSmsUrl(String youyiSmsUrl) {
		this.youyiSmsUrl = youyiSmsUrl;
	}

	public String getYouyiHySmsTemplate() {
		return youyiHySmsTemplate;
	}

	public void setYouyiHySmsTemplate(String youyiHySmsTemplate) {
		this.youyiHySmsTemplate = youyiHySmsTemplate;
	}

	public String getYouyiLoginSmsTemplate() {
		return youyiLoginSmsTemplate;
	}

	public void setYouyiLoginSmsTemplate(String youyiLoginSmsTemplate) {
		this.youyiLoginSmsTemplate = youyiLoginSmsTemplate;
	}

	public String getYouyiEditPwdSmsTemplate() {
		return youyiEditPwdSmsTemplate;
	}

	public void setYouyiEditPwdSmsTemplate(String youyiEditPwdSmsTemplate) {
		this.youyiEditPwdSmsTemplate = youyiEditPwdSmsTemplate;
	}

	public Integer getYouyiHySmsCodeTime() {
		return youyiHySmsCodeTime;
	}

	public void setYouyiHySmsCodeTime(Integer youyiHySmsCodeTime) {
		this.youyiHySmsCodeTime = youyiHySmsCodeTime;
	}

	/**
	 * 获取区域设置
	 * 
	 * @return 区域设置
	 */
	public Setting.Locale getLocale() {
		return locale;
	}

	/**
	 * 设置区域设置
	 * 
	 * @param locale
	 *            区域设置
	 */
	public void setLocale(Setting.Locale locale) {
		this.locale = locale;
	}

	/**
	 * 获取热门搜索关键词
	 * 
	 * @return 热门搜索关键词
	 */
	public String[] getHotSearches() {
		return StringUtils.split(hotSearch, SEPARATOR);
	}

	/**
	 * 获取允许上传图片扩展名
	 * 
	 * @return 允许上传图片扩展名
	 */
	public String[] getUploadImageExtensions() {
		return StringUtils.split(uploadImageExtension, SEPARATOR);
	}

	/**
	 * 获取允许上传媒体扩展名
	 * 
	 * @return 允许上传媒体扩展名
	 */
	public String[] getUploadMediaExtensions() {
		return StringUtils.split(uploadMediaExtension, SEPARATOR);
	}

	/**
	 * 获取允许上传文件扩展名
	 * 
	 * @return 允许上传文件扩展名
	 */
	public String[] getUploadFileExtensions() {
		return StringUtils.split(uploadFileExtension, SEPARATOR);
	}

	/**
	 * 设置精度
	 * 
	 * @param amount
	 *            数值
	 * @return 数值
	 */
	public BigDecimal setScale(BigDecimal amount) {
		if (amount != null && getPriceScale() != null && getPriceRoundType() != null) {
			switch (getPriceRoundType()) {
			case ROUND_UP:
				return amount.setScale(getPriceScale(), BigDecimal.ROUND_UP);
			case ROUND_DOWN:
				return amount.setScale(getPriceScale(), BigDecimal.ROUND_DOWN);
			case ROUND_HALF_UP:
				return amount.setScale(getPriceScale(), BigDecimal.ROUND_HALF_UP);
			}
		}
		return amount;
	}

	/**
	 * 解析图片上传路径
	 * 
	 * @return 图片上传路径
	 */
	public String resolveImageUploadPath() {
		return resolveImageUploadPath(null);
	}

	/**
	 * 解析图片上传路径
	 * 
	 * @param model
	 *            数据
	 * @return 图片上传路径
	 */
	public String resolveImageUploadPath(Map<String, Object> model) {
		try {
			return FreeMarkerUtils.process(getImageUploadPath(), model);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 解析媒体上传路径
	 * 
	 * @return 媒体上传路径
	 */
	public String resolveMediaUploadPath() {
		return resolveMediaUploadPath(null);
	}

	/**
	 * 解析媒体上传路径
	 * 
	 * @param model
	 *            数据
	 * @return 媒体上传路径
	 */
	public String resolveMediaUploadPath(Map<String, Object> model) {
		try {
			return FreeMarkerUtils.process(getMediaUploadPath(), model);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 解析文件上传路径
	 * 
	 * @return 文件上传路径
	 */
	public String resolveFileUploadPath() {
		return resolveFileUploadPath(null);
	}

	/**
	 * 解析文件上传路径
	 * 
	 * @param model
	 *            数据
	 * @return 文件上传路径
	 */
	public String resolveFileUploadPath(Map<String, Object> model) {
		try {
			return FreeMarkerUtils.process(getFileUploadPath(), model);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}

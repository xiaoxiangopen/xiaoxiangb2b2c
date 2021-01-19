package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Entity - SKU
 */
@Entity
public class Sku extends BaseEntity<Sku> {

    private static final long serialVersionUID = 2167830430439593293L;

    /**
     * 普通商品验证组
     */
    public interface General extends Default {

    }

    /**
     * 兑换商品验证组
     */
    public interface Exchange extends Default {

    }

    /**
     * 赠品验证组
     */
    public interface Gift extends Default {

    }

    /**
     * 编号
     */
    @JsonView(BaseView.class)
    private String sn;

    /**
     * 专柜编号
     */
    @JsonView(BaseView.class)
    private String counterNo;

    /**
     * 专柜名称
     */
    @JsonView(BaseView.class)
    private String counterName;

    /**
     * 销售价
     */
    @JsonView(BaseView.class)
    @NotNull(groups = General.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal price;

    /**
     * 成本价
     */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal cost;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal marketPrice;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuPromotionPrice3;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuPromotionPrice2;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuPromotionPrice1;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuPromotionPrice0;

    /**
     * 促销开始日期
     */
    @JsonView(BaseView.class)
//    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date promStartTime;

    /**
     * 促销结束日期
     */
    @JsonView(BaseView.class)
//    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date promEndTime;

    private Date syncTime;




    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuNormalPrice0;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuNormalPrice1;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuNormalPrice2;

    /**
     * 市场价
     */
    @JsonView(BaseView.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal skuNormalPrice3;

    /**
     * 最大佣金
     */
//    @NotNull(groups = General.class)
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    private BigDecimal maxCommission;

    /**
     * 赠送积分
     */
    @JsonView(BaseView.class)
    @Min(0)
    private Long rewardPoint;

    /**
     * 兑换积分
     */
    @JsonView(BaseView.class)
    @NotNull(groups = Exchange.class)
    @Min(0)
    private Long exchangePoint;

    /**
     * 库存
     */
    @NotNull(groups = Save.class)
    @Min(0)
    private Integer stock;

    /**
     * 已分配库存
     */
    private Integer allocatedStock;

    /**
     * 是否默认
     */
    @NotNull
    private Boolean isDefault;

    /**
     * 是否促销
     */
    //	@NotNull
    private Boolean isPro;

    private Boolean isZsz;

    private Boolean syncFlag;


    @TableField(exist = false)
    private Boolean isOutOfStock;


    /**
     * 商品条码
     */
//	@NotNull
    private String internalNumber;

    private String skuName;

    private String unit;

    private String specificationVal;

    /**
     * 商品
     */
    @JsonIgnore
    @TableField(exist = false)
    private Product product;

    /**
     * 规格值
     */
    @JsonView(BaseView.class)
    @TableField(exist = false)
    private List<SpecificationValue> specificationValues = new ArrayList<>();

    /**
     * 购物车项
     */
    @JsonIgnore
    @TableField(exist = false)
    private Set<CartItem> cartItems = new HashSet<>();

    /**
     * 订单项
     */
    @JsonIgnore
    @TableField(exist = false)
    private Set<OrderItem> orderItems = new HashSet<>();

    /**
     * 订单发货项
     */
    @JsonIgnore
    @TableField(exist = false)
    private Set<OrderShippingItem> orderShippingItems = new HashSet<>();


    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public Date getPromStartTime() {
        return promStartTime;
    }

    public void setPromStartTime(Date promStartTime) {
        this.promStartTime = promStartTime;
    }

    public Date getPromEndTime() {
        return promEndTime;
    }

    public void setPromEndTime(Date promEndTime) {
        this.promEndTime = promEndTime;
    }


    public Boolean getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    /**
     * 到货通知
     */
    @JsonIgnore
    @TableField(exist = false)
    private Set<ProductNotify> productNotifies = new HashSet<>();

    public BigDecimal getSkuPromotionPrice3() {
        return skuPromotionPrice3;
    }

    public void setSkuPromotionPrice3(BigDecimal skuPromotionPrice3) {
        this.skuPromotionPrice3 = skuPromotionPrice3;
    }

    /**
     * 库存记录
     */
    @JsonIgnore
    @TableField(exist = false)
    private Set<StockLog> stockLogs = new HashSet<>();

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSpecificationVal() {
        return specificationVal;
    }

    public void setSpecificationVal(String specificationVal) {
        this.specificationVal = specificationVal;
    }

    /**
     * 获取编号
     *
     * @return 编号
     */
    public String getSn() {
        return sn;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 设置编号
     *
     * @param sn 编号
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    public Boolean getIsPro() {
        return isPro;
    }

    public void setIsPro(Boolean isPro) {
        this.isPro = isPro;
    }

    public String getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(String counterNo) {
        this.counterNo = counterNo;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }


    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    /**
     * 获取销售价
     *
     * @return 销售价
     */
    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSkuPromotionPrice2() {
        return skuPromotionPrice2;
    }

    public void setSkuPromotionPrice2(BigDecimal skuPromotionPrice2) {
        this.skuPromotionPrice2 = skuPromotionPrice2;
    }

    public BigDecimal getSkuPromotionPrice1() {
        return skuPromotionPrice1;
    }

    public void setSkuPromotionPrice1(BigDecimal skuPromotionPrice1) {
        this.skuPromotionPrice1 = skuPromotionPrice1;
    }

    public BigDecimal getSkuPromotionPrice0() {
        return skuPromotionPrice0;
    }

    public void setSkuPromotionPrice0(BigDecimal skuPromotionPrice0) {
        this.skuPromotionPrice0 = skuPromotionPrice0;
    }

    public BigDecimal getSkuNormalPrice0() {
        return skuNormalPrice0;
    }

    public void setSkuNormalPrice0(BigDecimal skuNormalPrice0) {
        this.skuNormalPrice0 = skuNormalPrice0;
    }

    public BigDecimal getSkuNormalPrice1() {
        return skuNormalPrice1;
    }

    public void setSkuNormalPrice1(BigDecimal skuNormalPrice1) {
        this.skuNormalPrice1 = skuNormalPrice1;
    }

    public BigDecimal getSkuNormalPrice2() {
        return skuNormalPrice2;
    }

    public void setSkuNormalPrice2(BigDecimal skuNormalPrice2) {
        this.skuNormalPrice2 = skuNormalPrice2;
    }

    public BigDecimal getSkuNormalPrice3() {
        return skuNormalPrice3;
    }

    public void setSkuNormalPrice3(BigDecimal skuNormalPrice3) {
        this.skuNormalPrice3 = skuNormalPrice3;
    }

    public Boolean getIsZsz() {
        return isZsz;
    }

    public void setIsZsz(Boolean isZsz) {
        this.isZsz = isZsz;
    }

    /**
     * 设置销售价
     *
     * @param price 销售价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取成本价
     *
     * @return 成本价
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本价
     *
     * @param cost 成本价
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取市场价
     *
     * @return 市场价
     */
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    /**
     * 设置市场价
     *
     * @param marketPrice 市场价
     */
    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    /**
     * 获取最大佣金
     *
     * @return 最大佣金
     */
    public BigDecimal getMaxCommission() {
        return maxCommission;
    }

    /**
     * 设置最大佣金
     *
     * @param maxCommission 最大佣金
     */
    public void setMaxCommission(BigDecimal maxCommission) {
        this.maxCommission = maxCommission;
    }

    /**
     * 获取赠送积分
     *
     * @return 赠送积分
     */
    public Long getRewardPoint() {
        return rewardPoint;
    }

    /**
     * 设置赠送积分
     *
     * @param rewardPoint 赠送积分
     */
    public void setRewardPoint(Long rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    /**
     * 获取兑换积分
     *
     * @return 兑换积分
     */
    public Long getExchangePoint() {
        return exchangePoint;
    }

    /**
     * 设置兑换积分
     *
     * @param exchangePoint 兑换积分
     */
    public void setExchangePoint(Long exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    /**
     * 获取库存
     *
     * @return 库存
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * 设置库存
     *
     * @param stock 库存
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 获取已分配库存
     *
     * @return 已分配库存
     */
    public Integer getAllocatedStock() {
        return allocatedStock;
    }

    /**
     * 设置已分配库存
     *
     * @param allocatedStock 已分配库存
     */
    public void setAllocatedStock(Integer allocatedStock) {
        this.allocatedStock = allocatedStock;
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
     * @param isDefault 是否默认
     */
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
     * @param product 商品
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * 获取规格值
     *
     * @return 规格值
     */
    public List<SpecificationValue> getSpecificationValues() {
        return specificationValues;
    }


    public Boolean getOutOfStock() {
        return isOutOfStock;
    }

    public void setOutOfStock() {
        this.isOutOfStock = getIsOutOfStock();;
    }

    /**
     * 设置规格值
     *
     * @param specificationValues 规格值
     */
    public void setSpecificationValues(List<SpecificationValue> specificationValues) {
        this.specificationValues = specificationValues;
    }

    /**
     * 获取购物车项
     *
     * @return 购物车项
     */
    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    /**
     * 设置购物车项
     *
     * @param cartItems 购物车项
     */
    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    /**
     * 获取订单项
     *
     * @return 订单项
     */
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    /**
     * 设置订单项
     *
     * @param orderItems 订单项
     */
    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    /**
     * 获取订单发货项
     *
     * @return 订单发货项
     */
    public Set<OrderShippingItem> getOrderShippingItems() {
        return orderShippingItems;
    }

    /**
     * 设置订单发货项
     *
     * @param orderShippingItems 订单发货项
     */
    public void setOrderShippingItems(Set<OrderShippingItem> orderShippingItems) {
        this.orderShippingItems = orderShippingItems;
    }

    /**
     * 获取到货通知
     *
     * @return 到货通知
     */
    public Set<ProductNotify> getProductNotifies() {
        return productNotifies;
    }

    /**
     * 设置到货通知
     *
     * @param productNotifies 到货通知
     */
    public void setProductNotifies(Set<ProductNotify> productNotifies) {
        this.productNotifies = productNotifies;
    }

    /**
     * 获取库存记录
     *
     * @return 库存记录
     */
    public Set<StockLog> getStockLogs() {
        return stockLogs;
    }

    /**
     * 设置库存记录
     *
     * @param stockLogs 库存记录
     */
    public void setStockLogs(Set<StockLog> stockLogs) {
        this.stockLogs = stockLogs;
    }


    /**
     * 获取名称
     *
     * @return 名称
     */
    @JsonView(BaseView.class)
    @Transient
    public String getName() {
        return getProduct() != null ? getProduct().getName() : null;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @JsonView(BaseView.class)
    @Transient
    public Product.Type getType() {
        return getProduct() != null ? getProduct().getType() : null;
    }

    /**
     * 获取单位
     *
     * @return 单位
     */
    @Transient
    public String getUnit() {
        return getProduct() != null ? getProduct().getUnit() : null;
    }

    /**
     * 获取重量
     *
     * @return 重量
     */
    @Transient
    public Integer getWeight() {
        return getProduct() != null ? getProduct().getWeight() : null;
    }

    /**
     * 获取是否有效
     *
     * @return 是否有效
     */
    @Transient
    public boolean getIsActive() {
        return getProduct() != null && BooleanUtils.isTrue(getProduct().getIsActive());
    }

    /**
     * 获取是否上架
     *
     * @return 是否上架
     */
    @Transient
    public boolean getIsMarketable() {
        return getProduct() != null && BooleanUtils.isTrue(getProduct().getIsMarketable());
    }

    /**
     * 获取是否列出
     *
     * @return 是否列出
     */
    @Transient
    public boolean getIsList() {
        return getProduct() != null && BooleanUtils.isTrue(getProduct().getIsList());
    }

    /**
     * 获取是否置顶
     *
     * @return 是否置顶
     */
    @Transient
    public boolean getIsTop() {
        return getProduct() != null && BooleanUtils.isTrue(getProduct().getIsTop());
    }

    /**
     * 获取是否需要物流
     *
     * @return 是否需要物流
     */
    @Transient
    public boolean getIsDelivery() {
        return getProduct() != null && BooleanUtils.isTrue(getProduct().getIsDelivery());
    }

    /**
     * 获取路径
     *
     * @return 路径
     */
    @JsonView(BaseView.class)
    @Transient
    public String getPath() {
        return getProduct() != null ? getProduct().getPath() : null;
    }

    /**
     * 获取缩略图
     *
     * @return 缩略图
     */
    @JsonView(BaseView.class)
    @Transient
    public String getThumbnail() {
        return getProduct() != null ? getProduct().getThumbnail() : null;
    }

    /**
     * 获取店铺
     *
     * @return 店铺
     */
    @JsonIgnore
    @Transient
    public net.xiaoxiangshop.entity.Store getStore() {
        return getProduct() != null ? getProduct().getStore() : null;
    }

    /**
     * 获取可用库存
     *
     * @return 可用库存
     */
    @Transient
    public int getAvailableStock() {
        int availableStock = getStock() - getAllocatedStock();
        return availableStock >= 0 ? availableStock : 0;
    }

    /**
     * 获取是否库存警告
     *
     * @return 是否库存警告
     */
    @Transient
    public boolean getIsStockAlert() {
        Setting setting = SystemUtils.getSetting();
        return setting.getStockAlertCount() != null && getAvailableStock() <= setting.getStockAlertCount();
    }

    //是否促销
    @JsonView(BaseView.class)
    public  boolean   getIsPromotion(){
        Date  start=getPromStartTime();
        Date  end=getPromEndTime();
        Date  n=new Date();
//        System.out.println(n.before(end));
//        System.out.println(n.after(start));
        if(start!=null&&end!=null&&n.before(end)&&n.after(start)&&getIsPro()){
            return true;
        }else{
            return  false;
        }

    }

    /**
     * 获取是否缺货
     *
     * @return 是否缺货
     */
    @Transient
    public boolean getIsOutOfStock() {
        return getAvailableStock() <= 0;

    }

    /**
     * 获取规格值ID
     *
     * @return 规格值ID
     */
    @Transient
    public List<Integer> getSpecificationValueIds() {
        List<Integer> specificationValueIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(getSpecificationValues())) {
            for (SpecificationValue specificationValue : getSpecificationValues()) {
                specificationValueIds.add(specificationValue.getId());
            }
        }
        return specificationValueIds;
    }

    /**
     * 获取规格
     *
     * @return 规格
     */
    @Transient
    public List<String> getSpecifications() {
        List<String> specifications = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(getSpecificationValues())) {
            for (SpecificationValue specificationValue : getSpecificationValues()) {
                specifications.add(specificationValue.getValue());
            }
        }
        return specifications;
    }

    /**
     * 是否存在规格
     *
     * @return 是否存在规格
     */
    @Transient
    public boolean hasSpecification() {
        return CollectionUtils.isNotEmpty(getSpecificationValues());
    }


    /**
     * 持久化前处理
     */
    @PrePersist
    public void prePersist() {
        setSn(StringUtils.lowerCase(getSn()));
    }

    /**
     * 获取平台佣金
     *
     * @param type 类型
     * @return 平台佣金
     */
    @Transient
    public BigDecimal getPlatformCommission(net.xiaoxiangshop.entity.Store.Type type) {
        BigDecimal commission = BigDecimal.ZERO;
        if (type != null && getProduct() != null && getProduct().getProductCategory() != null) {
            ProductCategory productCategory = getProduct().getProductCategory();
            if (net.xiaoxiangshop.entity.Store.Type.GENERAL.equals(type) && productCategory.getGeneralRate() > 0) {
                commission = getPrice().multiply(new BigDecimal(String.valueOf(productCategory.getGeneralRate())).movePointLeft(2));
            } else if (net.xiaoxiangshop.entity.Store.Type.SELF.equals(type) && productCategory.getSelfRate() > 0) {
                commission = getPrice().multiply(new BigDecimal(String.valueOf(productCategory.getSelfRate())).movePointLeft(2));
            }
        }
        return commission;
    }

    /**
     * 删除前处理
     */
    @PreRemove
    public void preRemove() {
        Set<OrderItem> orderItems = getOrderItems();
        if (orderItems != null) {
            for (OrderItem orderItem : orderItems) {
                orderItem.setSku(null);
            }
        }
        Set<OrderShippingItem> orderShippingItems = getOrderShippingItems();
        if (orderShippingItems != null) {
            for (OrderShippingItem orderShippingItem : getOrderShippingItems()) {
                orderShippingItem.setSku(null);
            }
        }
    }

}
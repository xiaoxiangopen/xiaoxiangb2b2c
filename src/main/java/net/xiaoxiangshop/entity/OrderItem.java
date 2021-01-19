package net.xiaoxiangshop.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Entity - 订单项
 */
@Entity
public class OrderItem extends BaseEntity<OrderItem> {

    private static final long serialVersionUID = -4999926022604479334L;

    /**
     * 编号
     */
    private String sn;

    /**
     * 名称
     */
    @JsonView(BaseView.class)
    private String name;

    /**
     * 类型
     */
    @JsonView(BaseView.class)
    private Product.Type type;

    /**
     * 价格
     */
    @JsonView(BaseView.class)
    private BigDecimal price;

    //单品折扣金额
    @JsonView(BaseView.class)
    private BigDecimal counterdiscount;
    //单品折扣总金额
    @JsonView(BaseView.class)
    private BigDecimal totaldiscount;

    public BigDecimal getCounterdiscount() {
        return counterdiscount;
    }

    public void setCounterdiscount(BigDecimal counterdiscount) {
        this.counterdiscount = counterdiscount;
    }

    public BigDecimal getTotaldiscount() {
        return totaldiscount;
    }

    public void setTotaldiscount(BigDecimal totaldiscount) {
        this.totaldiscount = totaldiscount;
    }

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 是否需要物流
     */
    private Boolean isDelivery;

    /**
     * 缩略图
     */
    @JsonView(BaseView.class)
    private String thumbnail;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 已发货数量
     */
    private Integer shippedQuantity;

    /**
     * 已退货数量
     */
    private Integer returnedQuantity;

    /**
     * 平台佣金总计
     */
    private BigDecimal platformCommissionTotals;

    /**
     * 分销佣金小计
     */
    private BigDecimal distributionCommissionTotals;

    /**
     * SKU
     */
    @JsonIgnore
    @TableField(exist = false)
    private Sku sku;

    /**
     * 订单
     */
    @JsonIgnore
    @TableField(exist = false)
    private Order order;


    //分摊比例
    private BigDecimal proportion;

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    /**
     * 售后项
     */
    @JsonIgnore
    @TableField(exist = false)
    private Set<AftersalesItem> aftersalesItems = new HashSet<>();

    /**
     * 规格
     */
    @JsonView(BaseView.class)
    @TableField(exist = false)
    private List<String> specifications = new ArrayList<>();

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
     * @param sn 编号
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

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
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    public Product.Type getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(Product.Type type) {
        this.type = type;
    }

    /**
     * 获取价格
     *
     * @return 价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取重量
     *
     * @return 重量
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 设置重量
     *
     * @param weight 重量
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * 获取是否需要物流
     *
     * @return 是否需要物流
     */
    public Boolean getIsDelivery() {
        return isDelivery;
    }

    /**
     * 设置是否需要物流
     *
     * @param isDelivery 是否需要物流
     */
    public void setIsDelivery(Boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    /**
     * 获取缩略图
     *
     * @return 缩略图
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * 设置缩略图
     *
     * @param thumbnail 缩略图
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * 获取数量
     *
     * @return 数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取已发货数量
     *
     * @return 已发货数量
     */
    public Integer getShippedQuantity() {
        return shippedQuantity;
    }

    /**
     * 设置已发货数量
     *
     * @param shippedQuantity 已发货数量
     */
    public void setShippedQuantity(Integer shippedQuantity) {
        this.shippedQuantity = shippedQuantity;
    }

    /**
     * 获取已退货数量
     *
     * @return 已退货数量
     */
    public Integer getReturnedQuantity() {
        return returnedQuantity;
    }

    /**
     * 设置已退货数量
     *
     * @param returnedQuantity 已退货数量
     */
    public void setReturnedQuantity(Integer returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    /**
     * 获取平台佣金总计
     *
     * @return 平台佣金总计
     */
    public BigDecimal getPlatformCommissionTotals() {
        return platformCommissionTotals;
    }

    /**
     * 设置平台佣金总计
     *
     * @param platformCommissionTotals 平台佣金总计
     */
    public void setPlatformCommissionTotals(BigDecimal platformCommissionTotals) {
        this.platformCommissionTotals = platformCommissionTotals;
    }

    /**
     * 获取分销佣金小计
     *
     * @return 分销佣金小计
     */
    public BigDecimal getDistributionCommissionTotals() {
        return distributionCommissionTotals;
    }

    /**
     * 设置分销佣金小计
     *
     * @param distributionCommissionTotals 分销佣金小计
     */
    public void setDistributionCommissionTotals(BigDecimal distributionCommissionTotals) {
        this.distributionCommissionTotals = distributionCommissionTotals;
    }

    /**
     * 获取SKU
     *
     * @return SKU
     */
    public Sku getSku() {
        return sku;
    }

    /**
     * 设置SKU
     *
     * @param sku SKU
     */
    public void setSku(Sku sku) {
        this.sku = sku;
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
     * @param order 订单
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 获取售后项
     *
     * @return 售后项
     */
    public Set<AftersalesItem> getAftersalesItems() {
        return aftersalesItems;
    }

    /**
     * 设置售后项
     *
     * @param aftersalesItems 售后项
     */
    public void setAftersalesItems(Set<AftersalesItem> aftersalesItems) {
        this.aftersalesItems = aftersalesItems;
    }

    /**
     * 获取规格
     *
     * @return 规格
     */
    public List<String> getSpecifications() {
        return specifications;
    }

    /**
     * 设置规格
     *
     * @param specifications 规格
     */
    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }

    /**
     * 获取总重量
     *
     * @return 总重量
     */
    @JsonIgnore
    @Transient
    public int getTotalWeight() {
        if (getWeight() != null && getQuantity() != null) {
            return getWeight() * getQuantity();
        } else {
            return 0;
        }
    }

    /**
     * 获取小计
     *
     * @return 小计
     */
    @JsonIgnore
    @Transient
    public BigDecimal getSubtotal() {
        if (getPrice() != null && getQuantity() != null) {
            return getPrice().multiply(new BigDecimal(getQuantity()));
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取可发货数
     *
     * @return 可发货数
     */
    @JsonIgnore
    @Transient
    public int getShippableQuantity() {
        int shippableQuantity = getQuantity() - getShippedQuantity();
        return shippableQuantity >= 0 ? shippableQuantity : 0;
    }

    /**
     * 获取可退货数
     *
     * @return 可退货数
     */
    @JsonIgnore
    @Transient
    public int getReturnableQuantity() {
        int returnableQuantity = getShippedQuantity() - getReturnedQuantity();
        return returnableQuantity >= 0 ? returnableQuantity : 0;
    }

    /**
     * 获取可申请售后数量
     *
     * @return 可申请售后数量
     */
    @JsonIgnore
    @Transient
    public int getAllowApplyAftersalesQuantity() {
        int allowApplyAftersalesQuantity = getQuantity();

        for (AftersalesItem aftersalesItem : getAftersalesItems()) {
            Aftersales aftersales = aftersalesItem.getAftersales();
            if (Aftersales.Status.PENDING.equals(aftersales.getStatus()) || Aftersales.Status.APPROVED.equals(aftersales.getStatus())) {
                allowApplyAftersalesQuantity -= aftersalesItem.getQuantity();
            } else if (Aftersales.Type.AFTERSALES_RETURNS.equals(aftersales.getType()) && Aftersales.Status.COMPLETED.equals(aftersales.getStatus())) {
                allowApplyAftersalesQuantity -= aftersalesItem.getQuantity();
            }
        }
        return allowApplyAftersalesQuantity;
    }


}
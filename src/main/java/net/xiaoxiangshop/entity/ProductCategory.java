package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.PreRemove;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 商品分类
 * 
 */
@Entity
public class ProductCategory extends OrderedEntity<ProductCategory> {

	private static final long serialVersionUID = 5095521437302782717L;

	/**
	 * 树路径分隔符
	 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/**
	 * 路径
	 */
	private static final String PATH = "/product/list/%d";

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 页面标题
	 */
	private String seoTitle;

	/**
	 * 页面关键词
	 */
	private String seoKeywords;

	/**
	 * 页面描述
	 */
	private String seoDescription;

	/**
	 * 普通店铺分佣比例
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	private Double generalRate;

	/**
	 * 自营店铺分佣比例
	 */
	@NotNull
	@Min(0)
	@Digits(integer = 3, fraction = 3)
	private Double selfRate;

	/**
	 * 树路径
	 */
	private String treePath;

	/**
	 * 层级
	 */
	private Integer grade;

	/**
	 * 下拉树是否可选标识
	 */
	@TableField(exist = false)
	private String disabledFlag;

	/**
	 * 上级分类
	 */
	@TableField(exist = false)
	private ProductCategory parent;

	/**
	 * 下级分类
	 */
	@TableField(exist = false)
	private Set<ProductCategory> children = new HashSet<>();

	/**
	 * 商品
	 */
	@TableField(exist = false)
	private Set<Product> products = new HashSet<>();

	/**
	 * 关联品牌
	 */
	@TableField(exist = false)
	private Set<Brand> brands = new HashSet<>();


	/**
	 * 参数
	 */
	@TableField(exist = false)
	private Set<Parameter> parameters = new HashSet<>();

	/**
	 * 属性
	 */
	@TableField(exist = false)
	private Set<Attribute> attributes = new HashSet<>();

	/**
	 * 规格
	 */
	@TableField(exist = false)
	private Set<Specification> specifications = new HashSet<>();

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Set<Store> stores = new HashSet<>();

	/**
	 * 经营分类申请
	 */
	@TableField(exist = false)
	private Set<CategoryApplication> categoryApplications = new HashSet<>();



	public Integer getDeductStock() {
		return deductStock;
	}

	public void setDeductStock(Integer deductStock) {
		this.deductStock = deductStock;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
	}

	//分类图片路径
	private String imgUrl;
	//A/B/C分类
	private Integer categoryType;
	//库存核减数
	private Integer deductStock;
	//是否有效
	@JsonView(BaseView.class)
	private Boolean effective;

	public Boolean getEffective() {
		return effective;
	}

	public void setEffective(Boolean effective) {
		this.effective = effective;
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
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取页面标题
	 * 
	 * @return 页面标题
	 */
	public String getSeoTitle() {
		return seoTitle;
	}

	/**
	 * 设置页面标题
	 * 
	 * @param seoTitle
	 *            页面标题
	 */
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	/**
	 * 获取页面关键词
	 * 
	 * @return 页面关键词
	 */
	public String getSeoKeywords() {
		return seoKeywords;
	}

	/**
	 * 设置页面关键词
	 * 
	 * @param seoKeywords
	 *            页面关键词
	 */
	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	/**
	 * 获取页面描述
	 * 
	 * @return 页面描述
	 */
	public String getSeoDescription() {
		return seoDescription;
	}

	/**
	 * 设置页面描述
	 * 
	 * @param seoDescription
	 *            页面描述
	 */
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	/**
	 * 获取普通店铺分佣比例
	 * 
	 * @return 普通店铺分佣比例
	 */
	public Double getGeneralRate() {
		return generalRate;
	}

	/**
	 * 设置普通店铺分佣比例
	 * 
	 * @param generalRate
	 *            普通店铺分佣比例
	 */
	public void setGeneralRate(Double generalRate) {
		this.generalRate = generalRate;
	}

	/**
	 * 获取自营店铺分佣比例
	 * 
	 * @return 自营店铺分佣比例
	 */
	public Double getSelfRate() {
		return selfRate;
	}

	/**
	 * 设置自营店铺分佣比例
	 * 
	 * @param selfRate
	 *            自营店铺分佣比例
	 */
	public void setSelfRate(Double selfRate) {
		this.selfRate = selfRate;
	}

	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 * 
	 * @param treePath
	 *            树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取层级
	 * 
	 * @return 层级
	 */
	public Integer getGrade() {
		return grade;
	}

	/**
	 * 设置层级
	 * 
	 * @param grade
	 *            层级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getDisabledFlag() {
		return disabledFlag;
	}

	public void setDisabledFlag(String disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

	/**
	 * 获取上级分类
	 * 
	 * @return 上级分类
	 */
	public ProductCategory getParent() {
		return parent;
	}

	/**
	 * 设置上级分类
	 * 
	 * @param parent
	 *            上级分类
	 */
	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级分类
	 * 
	 * @return 下级分类
	 */
	public Set<ProductCategory> getChildren() {
		return children;
	}

	/**
	 * 设置下级分类
	 * 
	 * @param children
	 *            下级分类
	 */
	public void setChildren(Set<ProductCategory> children) {
		this.children = children;
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
	 * 获取关联品牌
	 * 
	 * @return 关联品牌
	 */
	public Set<Brand> getBrands() {
		return brands;
	}

	/**
	 * 设置关联品牌
	 * 
	 * @param brands
	 *            关联品牌
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}


	/**
	 * 获取参数
	 * 
	 * @return 参数
	 */
	public Set<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * 设置参数
	 * 
	 * @param parameters
	 *            参数
	 */
	public void setParameters(Set<Parameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * 获取属性
	 * 
	 * @return 属性
	 */
	public Set<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * 设置属性
	 * 
	 * @param attributes
	 *            属性
	 */
	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * 获取规格
	 * 
	 * @return 规格
	 */
	public Set<Specification> getSpecifications() {
		return specifications;
	}

	/**
	 * 设置规格
	 * 
	 * @param specifications
	 *            规格
	 */
	public void setSpecifications(Set<Specification> specifications) {
		this.specifications = specifications;
	}

	/**
	 * 获取店铺
	 * 
	 * @return 店铺
	 */
	public Set<Store> getStores() {
		return stores;
	}

	/**
	 * 设置店铺
	 * 
	 * @param stores
	 *            店铺
	 */
	public void setStores(Set<Store> stores) {
		this.stores = stores;
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
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@Transient
	public String getPath() {
		return String.format(ProductCategory.PATH, getId());
	}

	/**
	 * 获取所有上级分类ID
	 * 
	 * @return 所有上级分类ID
	 */
	@Transient
	public Long[] getParentIds() {
		String[] parentIds = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		Long[] result = new Long[parentIds.length];
		for (int i = 0; i < parentIds.length; i++) {
			result[i] = Long.valueOf(parentIds[i]);
		}
		return result;
	}

	/**
	 * 获取所有上级分类
	 * 
	 * @return 所有上级分类
	 */
	@Transient
	public List<ProductCategory> getParents() {
		List<ProductCategory> parents = new ArrayList<>();
		recursiveParents(parents, this);
		return parents;
	}

	/**
	 * 递归上级分类
	 * 
	 * @param parents
	 *            上级分类
	 * @param productCategory
	 *            商品分类
	 */
	private void recursiveParents(List<ProductCategory> parents, ProductCategory productCategory) {
		if (productCategory == null) {
			return;
		}
		ProductCategory parent = productCategory.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
		Set<Store> stores = getStores();
		if (stores != null) {
			for (Store store : stores) {
				store.getProductCategories().remove(this);
			}
		}
	}

}
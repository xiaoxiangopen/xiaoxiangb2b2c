package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 店铺商品分类
 * 
 */
@Entity
public class StoreProductCategory extends OrderedEntity<StoreProductCategory> {

	private static final long serialVersionUID = -9132857395246176246L;

	/**
	 * 树路径分隔符
	 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/**
	 * 路径
	 */
	private static final String PATH = "/product/list?storeProductCategoryId=%d";

	/**
	 * 分类名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 层级
	 */
	private Integer grade;

	/**
	 * 树路径
	 */
	private String treePath;

	/**
	 * 店铺
	 */
	@TableField(exist = false)
	private Store store;

	/**
	 * 上级分类
	 */
	@TableField(exist = false)
	private StoreProductCategory parent;

	/**
	 * 下级分类
	 */
	@TableField(exist = false)
	private Set<StoreProductCategory> children = new HashSet<>();

	/**
	 * 商品
	 */
	@TableField(exist = false)
	private Set<Product> products = new HashSet<>();

	/**
	 * 获取分类名称
	 * 
	 * @return 分类名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置分类名称
	 * 
	 * @param name
	 *            分类名称
	 */
	public void setName(String name) {
		this.name = name;
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
	 * 获取上级分类
	 * 
	 * @return 上级分类
	 */
	public StoreProductCategory getParent() {
		return parent;
	}

	/**
	 * 设置上级分类
	 * 
	 * @param parent
	 *            上级分类
	 */
	public void setParent(StoreProductCategory parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级分类
	 * 
	 * @return 下级分类
	 */
	public Set<StoreProductCategory> getChildren() {
		return children;
	}

	/**
	 * 设置下级分类
	 * 
	 * @param children
	 *            下级分类
	 */
	public void setChildren(Set<StoreProductCategory> children) {
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
	 * 获取路径
	 * 
	 * @return 路径
	 */
	@Transient
	public String getPath() {
		return String.format(StoreProductCategory.PATH, getId());
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
	public List<StoreProductCategory> getParents() {
		List<StoreProductCategory> parents = new ArrayList<>();
		recursiveParents(parents, this);
		return parents;
	}

	/**
	 * 递归上级分类
	 * 
	 * @param parents
	 *            上级分类
	 * @param storeProductCategory
	 *            店铺商品分类
	 */
	private void recursiveParents(List<StoreProductCategory> parents, StoreProductCategory storeProductCategory) {
		if (storeProductCategory == null) {
			return;
		}
		StoreProductCategory parent = storeProductCategory.getParent();
		if (parent != null) {
			parents.add(0, parent);
			recursiveParents(parents, parent);
		}
	}

}
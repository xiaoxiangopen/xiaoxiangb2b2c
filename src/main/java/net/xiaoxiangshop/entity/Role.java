package net.xiaoxiangshop.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 角色
 * 
 */
@Entity
public class Role extends BaseEntity<Role> {

	private static final long serialVersionUID = -6614052029623997372L;

	/**
	 * 最大权限数量
	 */
	public static final int MAX_PERMISSION_SIZE = 100;

	/**
	 * 名称
	 */
	@NotEmpty
	private String name;

	/**
	 * 是否内置
	 */
	private Boolean isSystem;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 权限
	 */
	@Size(max = MAX_PERMISSION_SIZE)
	@TableField(exist = false)
	private List<String> permissions = new ArrayList<>();

	/**
	 * 管理员
	 */
	@TableField(exist = false)
	private Set<Admin> admins = new HashSet<>();

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
	 * 获取是否内置
	 * 
	 * @return 是否内置
	 */
	public Boolean getIsSystem() {
		return isSystem;
	}

	/**
	 * 设置是否内置
	 * 
	 * @param isSystem
	 *            是否内置
	 */
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 * 
	 * @param description
	 *            描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取权限
	 * 
	 * @return 权限
	 */
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * 设置权限
	 * 
	 * @param permissions
	 *            权限
	 */
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	/**
	 * 获取管理员
	 * 
	 * @return 管理员
	 */
	public Set<Admin> getAdmins() {
		return admins;
	}

	/**
	 * 设置管理员
	 * 
	 * @param admins
	 *            管理员
	 */
	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	/**
	 * 类型转换 - 权限
	 * 
	 */
//	@Converter
//	public static class PermissionConverter extends BaseAttributeConverter<List<String>> {
//	}

}
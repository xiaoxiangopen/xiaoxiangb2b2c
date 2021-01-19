package net.xiaoxiangshop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.groups.Default;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import net.xiaoxiangshop.audit.AuditingEntityListener;
import net.xiaoxiangshop.audit.CreatedDate;
import net.xiaoxiangshop.audit.LastModifiedDate;

/**
 * Entity - 基类
 * 
 */
@JsonIgnoreProperties({ "handler" })
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity<T extends Model<T>> extends Model<T> implements Serializable {

	private static final long serialVersionUID = -67188388306700736L;

	/**
	 * "ID"属性名称
	 */
	public static final String ID_PROPERTY_NAME = "id";

	/**
	 * "创建日期"属性名称
	 */
	public static final String CREATED_DATE_PROPERTY_NAME = "createdDate";

	/**
	 * "最后修改日期"属性名称
	 */
	public static final String LAST_MODIFIED_DATE_PROPERTY_NAME = "lastModifiedDate";

	/**
	 * "版本"属性名称
	 */
	public static final String VERSION_PROPERTY_NAME = "version";

	/**
	 * 保存验证组
	 */
	public interface Save extends Default {

	}

	/**
	 * 更新验证组
	 */
	public interface Update extends Default {

	}

	/**
	 * 基础视图
	 */
	public interface BaseView {

	}

	/**
	 * ID
	 */
	@JsonView(BaseView.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;

	/**
	 * 创建日期
	 */
	@JsonView(BaseView.class)
	@CreatedDate
	@TableField(value = "created_date", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdDate;

	/**
	 * 最后修改日期
	 */
	@JsonView(BaseView.class)
	@LastModifiedDate
	@TableField(value = "last_modified_date", fill = FieldFill.UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastModifiedDate;

	/**
	 * 版本
	 */
	@Version
	@TableField(value = "version", fill = FieldFill.INSERT)
	private Long version;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取创建日期
	 * 
	 * @return 创建日期
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * 设置创建日期
	 * 
	 * @param createdDate
	 *            创建日期
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * 获取最后修改日期
	 * 
	 * @return 最后修改日期
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * 设置最后修改日期
	 * 
	 * @param lastModifiedDate
	 *            最后修改日期
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * 获取版本
	 * 
	 * @return 版本
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * 设置版本
	 * 
	 * @param version
	 *            版本
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * 判断是否为新建对象
	 * 
	 * @return 是否为新建对象
	 */
	@Transient
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * 重写toString方法
	 * 
	 * @return 字符串
	 */
	@Override
	public String toString() {
		return String.format("Entity of type %s with id: %s", getClass().getName(), getId());
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
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		BaseEntity<?> other = (BaseEntity<?>) obj;
		return getId() != null ? getId().equals(other.getId()) : false;
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += getId() != null ? getId().hashCode() * 31 : 0;
		return hashCode;
	}

}
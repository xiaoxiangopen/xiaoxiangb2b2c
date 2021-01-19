package net.xiaoxiangshop.entity;

import java.util.Map;

import javax.persistence.Entity;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * Entity - 审计日志
 * 
 */
@Entity
public class AuditLog extends BaseEntity<AuditLog> {

	private static final long serialVersionUID = -4494144902110236826L;

	/**
	 * "审计日志"属性名称
	 */
	public static final String AUDIT_LOG_ATTRIBUTE_NAME = AuditLog.class.getName() + ".AUDIT_LOG";

	/**
	 * 动作
	 */
	private String action;

	/**
	 * 详情
	 */
	private String detail;

	/**
	 * IP
	 */
	private String ip;

	/**
	 * 请求URL
	 */
	private String requestUrl;

	/**
	 * 请求参数
	 */
	private Map<String, String[]> parameters;

	/**
	 * 用户
	 */
	@TableField(exist = false)
	private User user;

	/**
	 * 获取动作
	 * 
	 * @return 动作
	 */
	public String getAction() {
		return action;
	}

	/**
	 * 设置动作
	 * 
	 * @param action
	 *            动作
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 获取详情
	 * 
	 * @return 详情
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * 设置详情
	 * 
	 * @param detail
	 *            详情
	 */
	public void setDetail(String detail) {
		this.detail = detail;
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
	 * 获取请求URL
	 * 
	 * @return 请求URL
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	/**
	 * 设置请求URL
	 * 
	 * @param requestUrl
	 *            请求URL
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	/**
	 * 获取请求参数
	 * 
	 * @return 请求参数
	 */
	public Map<String, String[]> getParameters() {
		return parameters;
	}

	/**
	 * 设置请求参数
	 * 
	 * @param parameters
	 *            请求参数
	 */
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	/**
	 * 获取用户
	 * 
	 * @return 用户
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置用户
	 * 
	 * @param user
	 *            用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 类型转换 - 请求参数
	 * 
	 */
//	@Converter
//	public static class ParameterConverter extends BaseAttributeConverter<Map<String, String[]>> {
//	}

}
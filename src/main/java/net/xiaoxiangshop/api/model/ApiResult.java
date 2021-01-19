package net.xiaoxiangshop.api.model;

import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSON;

/**
 * API接口的基础返回类
 *
 */
public class ApiResult {
	
	/**
	 * http 状态码
	 */
	private int status;

	/**
	 * 说明
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private Object data;
	
	public ApiResult() {
	}
	
	public ApiResult(int status, String message) {
		this.status = status;
		this.message = message;
	}
	 
	public ApiResult setStatus(HttpStatus httpStatus) {
		this.status = httpStatus.value();
		return this;
	}
	

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public ApiResult setMessage(String message) {
		this.message = message;
		return this;
	}

	public Object getData() {
		return data;
	}

	public ApiResult setData(Object data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}

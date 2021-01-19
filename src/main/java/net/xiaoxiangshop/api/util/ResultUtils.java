package net.xiaoxiangshop.api.util;

import org.springframework.http.HttpStatus;

import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.util.SpringUtils;

/**
 * 响应结果生成工具
 */
public class ResultUtils {
	
	/**
	 * 默认200状态消息
	 */
	public static final String DEFAULT_OK_MESSAGE = "common.message.ok";

	/**
	 * 默认400状态消息
	 */
	public static final String DEFAULT_BAD_REQUEST_MESSAGE = "common.message.badRequest";

	/**
	 * 默认401状态消息
	 */
	public static final String DEFAULT_UNAUTHORIZED_MESSAGE = "common.message.unauthorized";

	/**
	 * 默认403状态消息
	 */
	public static final String DEFAULT_FORBIDDEN_MESSAGE = "common.message.forbidden";

	/**
	 * 默认404状态消息
	 */
	public static final String DEFAULT_NOT_FOUND_MESSAGE = "common.message.notFound";

	/**
	 * 默认422状态消息
	 */
	public static final String DEFAULT_UNPROCESSABLE_ENTITY_MESSAGE = "common.message.unprocessableEntity";
	

	/**
	 * 400状态ResponseEntity
	 */
	public static final ApiResult BAD_REQUEST = ResultUtils.badRequest(DEFAULT_BAD_REQUEST_MESSAGE);

	/**
	 * 401状态ResponseEntity
	 */
	public static final ApiResult UNAUTHORIZED = ResultUtils.unauthorized(DEFAULT_UNAUTHORIZED_MESSAGE);

	/**
	 * 403状态ResponseEntity
	 */
	public static final ApiResult FORBIDDEN = ResultUtils.forbidden(DEFAULT_FORBIDDEN_MESSAGE);

	/**
	 * 404状态ResponseEntity
	 */
	public static final ApiResult NOT_FOUND = ResultUtils.notFound(DEFAULT_NOT_FOUND_MESSAGE);

	/**
	 * 422状态ResponseEntity
	 */
	public static final ApiResult UNPROCESSABLE_ENTITY = ResultUtils.unprocessableEntity(DEFAULT_UNPROCESSABLE_ENTITY_MESSAGE);
	
	/**
	 * 设置200状态
	 * 
	 * @param message
	 *            消息
	 */
	public static ApiResult ok() {
		return new ApiResult().setStatus(HttpStatus.OK).setMessage(SpringUtils.getMessage(DEFAULT_OK_MESSAGE));
	}

	/**
	 * 返回200状态ResponseEntity
	 * 
	 * @param message
	 *            消息
	 * @return 200状态ResponseEntity
	 */
	public static ApiResult ok(Object data) {
		return new ApiResult().setStatus(HttpStatus.OK).setMessage(SpringUtils.getMessage(DEFAULT_OK_MESSAGE)).setData(data);
	}

	/**
	 * 设置400状态
	 * 
	 * @param message
	 *            消息
	 */
	public static ApiResult badRequest(String message) {
		return new ApiResult().setStatus(HttpStatus.BAD_REQUEST).setMessage(message);
	}
	
	/**
	 * 设置401状态
	 * 
	 * @param message
	 *            消息
	 */
	public static ApiResult unauthorized(String message) {
		return new ApiResult().setStatus(HttpStatus.UNAUTHORIZED).setMessage(message);
	}
	
	/**
	 * 返回403状态ResponseEntity
	 * 
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 * @return 403状态ResponseEntity
	 */
	public static ApiResult forbidden(String message, Object... args) {
		return new ApiResult().setStatus(HttpStatus.FORBIDDEN).setMessage(SpringUtils.getMessage(message, args));
	}
	
	/**
	 * 设置404状态
	 * 
	 * @param message
	 *            消息
	 */
	public static ApiResult notFound(String message, Object... args) {
		return new ApiResult().setStatus(HttpStatus.NOT_FOUND).setMessage(SpringUtils.getMessage(message, args));
	}
	
	/**
	 * 返回422状态ResponseEntity
	 * 
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 * @return 422状态ResponseEntity
	 */
	public static ApiResult unprocessableEntity(String message, Object... args) {
		return new ApiResult().setStatus(HttpStatus.UNPROCESSABLE_ENTITY).setMessage(SpringUtils.getMessage(message, args));
	}
	
}

package net.xiaoxiangshop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import net.xiaoxiangshop.util.JsonUtils;
import net.xiaoxiangshop.util.SpringUtils;

/**
 * 结果
 * 
 */
public final class Results {

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
	 * 200状态ResponseEntity
	 */
	public static final ResponseEntity<Map<String, String>> OK = Results.ok(DEFAULT_OK_MESSAGE);

	/**
	 * 400状态ResponseEntity
	 */
	public static final ResponseEntity<Map<String, String>> BAD_REQUEST = Results.badRequest(DEFAULT_BAD_REQUEST_MESSAGE);

	/**
	 * 401状态ResponseEntity
	 */
	public static final ResponseEntity<Map<String, String>> UNAUTHORIZED = Results.unauthorized(DEFAULT_UNAUTHORIZED_MESSAGE);

	/**
	 * 403状态ResponseEntity
	 */
	public static final ResponseEntity<Map<String, String>> FORBIDDEN = Results.forbidden(DEFAULT_FORBIDDEN_MESSAGE);

	/**
	 * 404状态ResponseEntity
	 */
	public static final ResponseEntity<Map<String, String>> NOT_FOUND = Results.notFound(DEFAULT_NOT_FOUND_MESSAGE);

	/**
	 * 422状态ResponseEntity
	 */
	public static final ResponseEntity<Map<String, String>> UNPROCESSABLE_ENTITY = Results.unprocessableEntity(DEFAULT_UNPROCESSABLE_ENTITY_MESSAGE);

	/**
	 * JSON内容类型
	 */
	private static final String JSON_CONTENT_TYPE = "application/json";

	/**
	 * 消息KEY
	 */
	private static final String MESSAGE_KEY = "message";

	/**
	 * 构造方法
	 */
	private Results() {
	}

	/**
	 * 设置状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param httpStatus
	 *            HttpStatus
	 * @param data
	 *            数据
	 */
	public static void status(HttpServletResponse response, HttpStatus httpStatus, Object data) {
		Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
		Assert.notNull(httpStatus, "[Assertion failed] - httpStatus is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		response.setContentType(JSON_CONTENT_TYPE);
		response.setStatus(httpStatus.value());
		try {
			JsonUtils.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 设置状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param httpStatus
	 *            HttpStatus
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 */
	public static void status(HttpServletResponse response, HttpStatus httpStatus, String message, Object... args) {
		Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
		Assert.notNull(httpStatus, "[Assertion failed] - httpStatus is required; it must not be null");
		Assert.hasText(message, "[Assertion failed] - message must have text; it must not be null, empty, or blank");

		Map<String, String> data = new HashMap<>();
		data.put(MESSAGE_KEY, SpringUtils.getMessage(message, args));
		status(response, httpStatus, data);
	}

	/**
	 * 返回状态ResponseEntity
	 * 
	 * @param httpStatus
	 *            HttpStatus
	 * @param data
	 *            数据
	 * @return ResponseEntity
	 */
	public static <T> ResponseEntity<T> status(HttpStatus httpStatus, T data) {
		Assert.notNull(httpStatus, "[Assertion failed] - httpStatus is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		return new ResponseEntity<>(data, httpStatus);
	}

	/**
	 * 返回状态ResponseEntity
	 * 
	 * @param httpStatus
	 *            HttpStatus
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 * @return ResponseEntity
	 */
	public static ResponseEntity<Map<String, String>> status(HttpStatus httpStatus, String message, Object... args) {
		Assert.notNull(httpStatus, "[Assertion failed] - httpStatus is required; it must not be null");
		Assert.hasText(message, "[Assertion failed] - message must have text; it must not be null, empty, or blank");

		Map<String, String> data = new HashMap<>();
		data.put(MESSAGE_KEY, SpringUtils.getMessage(message, args));
		return status(httpStatus, data);
	}

	/**
	 * 设置200状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 */
	public static void ok(HttpServletResponse response, String message, Object... args) {
		status(response, HttpStatus.OK, message, args);
	}

	/**
	 * 设置400状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 */
	public static void badRequest(HttpServletResponse response, String message, Object... args) {
		status(response, HttpStatus.BAD_REQUEST, message, args);
	}

	/**
	 * 设置401状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 */
	public static void unauthorized(HttpServletResponse response, String message, Object... args) {
		status(response, HttpStatus.UNAUTHORIZED, message, args);
	}

	/**
	 * 设置403状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 */
	public static void forbidden(HttpServletResponse response, String message, Object... args) {
		status(response, HttpStatus.FORBIDDEN, message, args);
	}

	/**
	 * 设置404状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 */
	public static void notFound(HttpServletResponse response, String message, Object... args) {
		status(response, HttpStatus.NOT_FOUND, message, args);
	}

	/**
	 * 设置422状态
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 */
	public static void unprocessableEntity(HttpServletResponse response, String message, Object... args) {
		status(response, HttpStatus.UNPROCESSABLE_ENTITY, message, args);
	}

	/**
	 * 返回200状态ResponseEntity
	 * 
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 * @return 200状态ResponseEntity
	 */
	public static ResponseEntity<Map<String, String>> ok(String message, Object... args) {
		return status(HttpStatus.OK, message, args);
	}

	/**
	 * 返回400状态ResponseEntity
	 * 
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 * @return 400状态ResponseEntity
	 */
	public static ResponseEntity<Map<String, String>> badRequest(String message, Object... args) {
		return status(HttpStatus.BAD_REQUEST, message, args);
	}

	/**
	 * 返回401状态ResponseEntity
	 * 
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 * @return 401状态ResponseEntity
	 */
	public static ResponseEntity<Map<String, String>> unauthorized(String message, Object... args) {
		return status(HttpStatus.UNAUTHORIZED, message, args);
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
	public static ResponseEntity<Map<String, String>> forbidden(String message, Object... args) {
		return status(HttpStatus.FORBIDDEN, message, args);
	}

	/**
	 * 返回404状态ResponseEntity
	 * 
	 * @param message
	 *            消息
	 * @param args
	 *            参数
	 * @return 404状态ResponseEntity
	 */
	public static ResponseEntity<Map<String, String>> notFound(String message, Object... args) {
		return status(HttpStatus.NOT_FOUND, message, args);
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
	public static ResponseEntity<Map<String, String>> unprocessableEntity(String message, Object... args) {
		return status(HttpStatus.UNPROCESSABLE_ENTITY, message, args);
	}

}
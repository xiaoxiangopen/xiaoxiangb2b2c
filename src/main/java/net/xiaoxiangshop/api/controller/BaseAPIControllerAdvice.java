package net.xiaoxiangshop.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wf.jwtp.exception.TokenException;

import net.xiaoxiangshop.api.model.ApiResult;

/**
 * 注意这个统一异常处理器只对认证过的用户调用接口中的异常有作用， 对AuthenticationException没有用
 */
@RestControllerAdvice(annotations = RestController.class)
public class BaseAPIControllerAdvice {

	/**
	 * 捕捉其他所有异常
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ApiResult errorHandler(Exception ex) {
		ApiResult apiResult = null;
		// 根据不同错误获取错误信息
		if (ex instanceof TokenException) {
			apiResult = new ApiResult(((TokenException) ex).getCode(), ex.getMessage());
		} else {
			ex.printStackTrace();
			apiResult = new ApiResult().setStatus(HttpStatus.INTERNAL_SERVER_ERROR).setMessage(ex.getMessage());
		}
		return apiResult;
	}

}

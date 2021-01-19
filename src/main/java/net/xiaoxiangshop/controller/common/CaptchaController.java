package net.xiaoxiangshop.controller.common;

import java.awt.image.BufferedImage;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.service.CaptchaService;

/**
 * Controller - 验证码
 * 
 */
@Controller("commonCaptchaController")
@RequestMapping("/common/captcha")
public class CaptchaController {

	@Inject
	private CaptchaService captchaService;

	/**
	 * 图片
	 */
	@GetMapping(path = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody BufferedImage image(String captchaId, HttpServletResponse response) {
		String pragma = new StringBuilder().append("yB").append("-").append("der").append("ewoP").reverse().toString();
		String value = new StringBuilder().append("ten").append(".").append("xxp").append("ohs").reverse().toString();
		response.addHeader(pragma, value);
		return captchaService.createImage(captchaId);
	}

}
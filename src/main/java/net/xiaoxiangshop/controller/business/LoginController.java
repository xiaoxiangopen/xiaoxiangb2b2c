package net.xiaoxiangshop.controller.business;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Admin;
import net.xiaoxiangshop.service.BusinessService;
import net.xiaoxiangshop.service.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.entity.Business;
import net.xiaoxiangshop.security.CurrentUser;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 商家登录
 * 
 */
@Controller("businessLoginController")
@RequestMapping("/business")
public class LoginController extends BaseController {

	@Value("${security.business_login_success_url}")
	private String businessLoginSuccessUrl;
	@Inject
	private BusinessService businessService;
	@Inject
	private SmsService smsService;
	/**
	 * 登录跳转
	 */
	@GetMapping({ "", "/" })
	public String index() {
		return "redirect:/business/login";
	}

	/**
	 * 登录页面
	 */
	@GetMapping("/login")
	public String index(@CurrentUser Business currentUser, ModelMap model) {
		if (currentUser != null) {
			return "redirect:" + businessLoginSuccessUrl;
		}
		model.addAttribute("businessLoginSuccessUrl", businessLoginSuccessUrl);
		return "business/login/index";
	}
	/**
	 * business登陆获取短信验证码
	 * @param username
	 * @param password
	 * @return
	 */
	@GetMapping("/getSmsCode")
	public ResponseEntity<?> getSmsCode(String username, String password) {
		Business business=businessService.findByUsername(username);
		if(business==null){
			return responseReslut(null,null,100,"此账号不存在");
		}
		String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!business.getEncodedPassword().equals(pwd)){
			return responseReslut(null,null,100,"密码错误");
		}
		smsService.sendSmsCode("1",business.getMobile());
		return responseReslut(null,null,200,"短信验证已发送");
	}

	/**
	 * 校验短信验证码
	 * @param username
	 * @param password
	 * @param smsCode
	 * @return
	 */
	@PostMapping("/checkSmsCode")
	public ResponseEntity<?> checkSmsCode(String username, String password,String smsCode) {
		Business business=businessService.findByUsername(username);
		if(business==null){
			return responseReslut(null,null,100,"此账号不存在");
		}
		String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!business.getEncodedPassword().equals(pwd)){
			return responseReslut(null,null,100,"密码错误");
		}
		ResponseEntity<?> reslut=smsService.checkSmsCode("1",business.getMobile(),smsCode);
		Integer code= reslut.getStatusCodeValue();
		if(code==200){
			return responseReslut(null,null,200,"短信验证码校验成功");
		}
		return responseReslut(null,null,100,"短信验证码校验失败");
	}
	public ResponseEntity<?> responseReslut(String username,String mobile,Integer code,String message){
		Map<String,Object> responseReslut=new HashMap<>();
		responseReslut.put("code",code);
		responseReslut.put("username",username);
		responseReslut.put("mobile",mobile);
		responseReslut.put("message",message);
		return ResponseEntity.ok(responseReslut);
	}
}
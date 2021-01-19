package net.xiaoxiangshop.api.controller.shop;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wf.jwtp.exception.UnauthorizedException;
import org.wf.jwtp.provider.Token;
import org.wf.jwtp.provider.TokenStore;

import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.User;
import net.xiaoxiangshop.service.UserService;

/**
 * 登录 - 接口类
 */
@RestController
@RequestMapping("/api/login")
public class LoginAPIController {

	@Inject
	private UserService userService;
	@Inject
	private TokenStore tokenStore;

	@PostMapping
	public ApiResult index(@RequestParam("username") String username, @RequestParam("password") String password) {

		User user = userService.findByUsername(username);
		if (user != null && user.isValidCredentials(password)) {
			Token token = tokenStore.createNewToken(String.valueOf(user.getId()), Member.MEMBER_PERMISSIONS, Member.ROLES);
			return ResultUtils.ok("Bearer " + token.getAccessToken());
		} else {
			throw new UnauthorizedException();
		}
	}

}

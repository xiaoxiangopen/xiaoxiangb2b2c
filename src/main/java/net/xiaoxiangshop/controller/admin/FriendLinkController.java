package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.FriendLink;
import net.xiaoxiangshop.service.FriendLinkService;

/**
 * Controller - 友情链接
 * 
 */
@Controller("adminFriendLinkController")
@RequestMapping("/admin/friend_link")
public class FriendLinkController extends BaseController {

	@Inject
	private FriendLinkService friendLinkService;

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("types", FriendLink.Type.values());
		return "admin/friend_link/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(FriendLink friendLink) {
		if (!isValid(friendLink)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (FriendLink.Type.TEXT.equals(friendLink.getType())) {
			friendLink.setLogo(null);
		} else if (StringUtils.isEmpty(friendLink.getLogo())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		friendLinkService.save(friendLink);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("types", FriendLink.Type.values());
		model.addAttribute("friendLink", friendLinkService.find(id));
		return "admin/friend_link/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(FriendLink friendLink) {
		if (!isValid(friendLink)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (FriendLink.Type.TEXT.equals(friendLink.getType())) {
			friendLink.setLogo(null);
		} else if (StringUtils.isEmpty(friendLink.getLogo())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		friendLinkService.update(friendLink);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", friendLinkService.findPage(pageable));
		return "admin/friend_link/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		friendLinkService.delete(ids);
		return Results.OK;
	}

}
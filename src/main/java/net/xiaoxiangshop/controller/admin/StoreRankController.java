package net.xiaoxiangshop.controller.admin;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.StoreRank;
import net.xiaoxiangshop.service.StoreRankService;

/**
 * Controller - 店铺等级
 * 
 */
@Controller("adminStoreRankController")
@RequestMapping("/admin/store_rank")
public class StoreRankController extends BaseController {

	@Inject
	private StoreRankService storeRankService;

	/**
	 * 检查名称是否唯一
	 */
	@GetMapping("/check_name")
	public @ResponseBody boolean checkName(Long id, String name) {
		return StringUtils.isNotEmpty(name) && !storeRankService.nameUnique(id, name);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add() {
		return "admin/store_rank/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(StoreRank storeRank) {
		if (!isValid(storeRank)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (storeRankService.nameExists(storeRank.getName())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		storeRank.setStores(null);
		storeRankService.save(storeRank);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("storeRank", storeRankService.find(id));
		return "admin/store_rank/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(StoreRank storeRank) {
		if (!isValid(storeRank)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		StoreRank pStoreRank = storeRankService.find(storeRank.getId());
		if (pStoreRank == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (storeRankService.nameUnique(storeRank.getId(), storeRank.getName())) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		storeRankService.update(storeRank, "stores");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", storeRankService.findPage(pageable));
		return "admin/store_rank/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				StoreRank storeRank = storeRankService.find(id);
				if (storeRank != null && storeRank.getStores() != null && !storeRank.getStores().isEmpty()) {
					return Results.unprocessableEntity("admin.storeRank.deleteExistNotAllowed", storeRank.getName());
				}
			}
			long totalCount = storeRankService.count();
			if (ids.length >= totalCount) {
				return Results.unprocessableEntity("common.deleteAllNotAllowed");
			}
			storeRankService.delete(ids);
		}
		return Results.OK;
	}

}
package net.xiaoxiangshop.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import net.xiaoxiangshop.entity.Distributor;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.service.DistributorService;
import net.xiaoxiangshop.service.MemberService;

/**
 * Controller - 分销员
 * 
 */
@Controller("adminDistributorController")
@RequestMapping("/admin/distributor")
public class DistributorController extends BaseController {

	@Inject
	private DistributorService distributorService;
	@Inject
	private MemberService memberService;

	/**
	 * 会员选择
	 */
	@GetMapping("/member_select")
	public ResponseEntity<?> memberSelect(String keyword) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (StringUtils.isEmpty(keyword)) {
			return ResponseEntity.ok(data);
		}
		List<Member> members = memberService.search(keyword, null, null);
		for (Member member : members) {
			if (member.getDistributor() == null) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", String.valueOf(member.getId()));
				item.put("username", member.getUsername());
				data.add(item);
			}
		}
		return ResponseEntity.ok(data);
	}

	/**
	 * 上级分销员选择
	 */
	@GetMapping("/parent_distributor_select")
	public ResponseEntity<?> parentDistributorSelect(String keyword, Long excludeId) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (StringUtils.isEmpty(keyword)) {
			return ResponseEntity.ok(data);
		}
		Set<Member> excludes = new HashSet<>(memberService.findList(excludeId));
		List<Member> members = memberService.search(keyword, excludes, null);
		for (Member member : members) {
			if (member.getDistributor() != null) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", String.valueOf(member.getDistributor().getId()));
				item.put("username", member.getUsername());
				data.add(item);
			}
		}
		return ResponseEntity.ok(data);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		return "admin/distributor/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(Long parentDistributorId, Long memberId) {
		Member member = memberService.find(memberId);
		Distributor distributor = distributorService.find(parentDistributorId);
		if (distributor == null) {
			distributorService.create(member);
		} else {
			distributorService.create(member, distributor.getMember());
		}
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("distributor", distributorService.find(id));
		return "admin/distributor/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(Long parentDistributorId, Long id) {
		Distributor distributor = distributorService.find(id);
		if (distributor == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		Distributor parentDistributor = distributorService.find(parentDistributorId);
		if (distributor.equals(parentDistributor)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		distributorService.modify(distributor, parentDistributor != null ? parentDistributor.getMember() : null);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", distributorService.findPage(pageable));
		return "admin/distributor/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		distributorService.delete(ids);
		return Results.OK;
	}

}
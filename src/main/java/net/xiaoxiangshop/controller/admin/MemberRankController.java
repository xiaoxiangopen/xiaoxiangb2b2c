package net.xiaoxiangshop.controller.admin;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import net.xiaoxiangshop.entity.Dict;
import net.xiaoxiangshop.service.DictService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.MemberRank;
import net.xiaoxiangshop.service.MemberRankService;

/**
 * Controller - 会员等级
 * 
 */
@Controller("adminMemberRankController")
@RequestMapping("/admin/member_rank")
public class MemberRankController extends BaseController {

	@Inject
	private MemberRankService memberRankService;

	@Inject
	private DictService dictService;
	/**
	 * 检查消费金额是否唯一
	 */
	@GetMapping("/check_amount")
	public @ResponseBody boolean checkAmount(Long id, BigDecimal amount) {
		return amount != null && memberRankService.amountUnique(id, amount);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		return "admin/member_rank/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(MemberRank memberRank) {
		if (!isValid(memberRank)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (memberRank.getIsSpecial()) {
			memberRank.setAmount(null);
		} else if (memberRank.getAmount() == null || !memberRankService.amountExists(memberRank.getAmount())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		memberRank.setMembers(null);
		memberRankService.save(memberRank);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("memberRank", memberRankService.find(id));

		Dict dict=new Dict();
		dict.setDictType("categoryType");
		List<Dict> dictList= dictService.find(dict);
		model.addAttribute("dictList", dictList);

		StringBuffer  stringBuffer=new StringBuffer();
		for (Dict d : dictList) {
			stringBuffer.append(d.getDictId()).append(",");
		}
		model.addAttribute("dictIds", stringBuffer.substring(0,stringBuffer.length()-1));

		return "admin/member_rank/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(MemberRank memberRank, Long id) {
		if (!isValid(memberRank)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		MemberRank pMemberRank = memberRankService.find(id);
		if (pMemberRank == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (pMemberRank.getIsDefault()) {
			memberRank.setIsDefault(true);
		}
		if (memberRank.getIsSpecial()) {
			memberRank.setAmount(null);
		} else if (memberRank.getAmount() == null || !memberRankService.amountUnique(id, memberRank.getAmount())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		memberRankService.update(memberRank, "members", "promotions");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", memberRankService.findPage(pageable));
		return "admin/member_rank/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				MemberRank memberRank = memberRankService.find(id);
				if (memberRank != null && memberRank.getMembers() != null && !memberRank.getMembers().isEmpty()) {
					return Results.unprocessableEntity("admin.memberRank.deleteExistNotAllowed", memberRank.getName());
				}
			}
			long totalCount = memberRankService.count();
			if (ids.length >= totalCount) {
				return Results.unprocessableEntity("common.deleteAllNotAllowed");
			}
			memberRankService.delete(ids);
		}
		return Results.OK;
	}

}
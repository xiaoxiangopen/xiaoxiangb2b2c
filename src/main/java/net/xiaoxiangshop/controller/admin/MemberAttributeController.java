package net.xiaoxiangshop.controller.admin;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
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
import net.xiaoxiangshop.entity.BaseEntity;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberAttribute;
import net.xiaoxiangshop.service.MemberAttributeService;

/**
 * Controller - 会员注册项
 * 
 */
@Controller("adminMemberAttributeController")
@RequestMapping("/admin/member_attribute")
public class MemberAttributeController extends BaseController {

	@Inject
	private MemberAttributeService memberAttributeService;

	/**
	 * 检查配比语法是否正确
	 */
	@GetMapping("/check_pattern")
	public @ResponseBody boolean checkPattern(String pattern) {
		if (StringUtils.isEmpty(pattern)) {
			return false;
		}
		try {
			Pattern.compile(pattern);
		} catch (PatternSyntaxException e) {
			return false;
		}
		return true;
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("maxOptionSize", MemberAttribute.MAX_OPTION_SIZE);
		return "admin/member_attribute/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(MemberAttribute memberAttribute) {
		if (!isValid(memberAttribute, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (MemberAttribute.Type.SELECT.equals(memberAttribute.getType()) || MemberAttribute.Type.CHECKBOX.equals(memberAttribute.getType())) {
			List<String> options = memberAttribute.getOptions();
			CollectionUtils.filter(options, new AndPredicate(new UniquePredicate(), new Predicate() {
				public boolean evaluate(Object object) {
					String option = (String) object;
					return StringUtils.isNotEmpty(option);
				}
			}));
			if (CollectionUtils.isEmpty(options)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			memberAttribute.setPattern(null);
		} else if (MemberAttribute.Type.TEXT.equals(memberAttribute.getType())) {
			memberAttribute.setOptions(null);
		} else {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (StringUtils.isNotEmpty(memberAttribute.getPattern())) {
			try {
				Pattern.compile(memberAttribute.getPattern());
			} catch (PatternSyntaxException e) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		Integer propertyIndex = memberAttributeService.findUnusedPropertyIndex();
		if (propertyIndex == null) {
			return Results.unprocessableEntity("admin.memberAttribute.addCountNotAllowed", Member.ATTRIBUTE_VALUE_PROPERTY_COUNT);
		}
		memberAttribute.setPropertyIndex(null);
		memberAttributeService.save(memberAttribute);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("maxOptionSize", MemberAttribute.MAX_OPTION_SIZE);
		model.addAttribute("memberAttribute", memberAttributeService.find(id));
		return "admin/member_attribute/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(MemberAttribute memberAttribute) {
		if (!isValid(memberAttribute)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		MemberAttribute pMemberAttribute = memberAttributeService.find(memberAttribute.getId());
		if (pMemberAttribute == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (MemberAttribute.Type.SELECT.equals(pMemberAttribute.getType()) || MemberAttribute.Type.CHECKBOX.equals(pMemberAttribute.getType())) {
			List<String> options = memberAttribute.getOptions();
			CollectionUtils.filter(options, new AndPredicate(new UniquePredicate(), new Predicate() {
				public boolean evaluate(Object object) {
					String option = (String) object;
					return StringUtils.isNotEmpty(option);
				}
			}));
			if (CollectionUtils.isEmpty(options)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			memberAttribute.setPattern(null);
		} else {
			memberAttribute.setOptions(null);
		}
		if (StringUtils.isNotEmpty(memberAttribute.getPattern())) {
			try {
				Pattern.compile(memberAttribute.getPattern());
			} catch (PatternSyntaxException e) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		memberAttributeService.update(memberAttribute, "type", "propertyIndex");
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", memberAttributeService.findPage(pageable));
		return "admin/member_attribute/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		memberAttributeService.delete(ids);
		return Results.OK;
	}

}
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
import net.xiaoxiangshop.entity.BusinessAttribute;
import net.xiaoxiangshop.service.BusinessAttributeService;

/**
 * Controller - 商家注册项
 * 
 */
@Controller("businessAttributeController")
@RequestMapping("/admin/business_attribute")
public class BusinessAttributeController extends BaseController {

	@Inject
	private BusinessAttributeService businessAttributeService;

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
		model.addAttribute("maxOptionSize", BusinessAttribute.MAX_OPTION_SIZE);
		return "admin/business_attribute/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public ResponseEntity<?> save(BusinessAttribute businessAttribute) {
		if (!isValid(businessAttribute)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (BusinessAttribute.Type.SELECT.equals(businessAttribute.getType()) || BusinessAttribute.Type.CHECKBOX.equals(businessAttribute.getType())) {
			List<String> options = businessAttribute.getOptions();
			CollectionUtils.filter(options, new AndPredicate(new UniquePredicate(), new Predicate() {
				public boolean evaluate(Object object) {
					String option = (String) object;
					return StringUtils.isNotEmpty(option);
				}
			}));
			if (CollectionUtils.isEmpty(options)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			businessAttribute.setPattern(null);
		} else if (BusinessAttribute.Type.TEXT.equals(businessAttribute.getType()) || BusinessAttribute.Type.IMAGE.equals(businessAttribute.getType()) || BusinessAttribute.Type.DATE.equals(businessAttribute.getType())) {
			businessAttribute.setOptions(null);
		} else {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (StringUtils.isNotEmpty(businessAttribute.getPattern())) {
			try {
				Pattern.compile(businessAttribute.getPattern());
			} catch (PatternSyntaxException e) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}

		Integer propertyIndex = businessAttributeService.findUnusedPropertyIndex();
		if (propertyIndex == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		businessAttribute.setPropertyIndex(null);
		businessAttributeService.save(businessAttribute);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("maxOptionSize", BusinessAttribute.MAX_OPTION_SIZE);
		model.addAttribute("businessAttribute", businessAttributeService.find(id));
		return "admin/business_attribute/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public ResponseEntity<?> update(BusinessAttribute businessAttribute) {
		if (!isValid(businessAttribute)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		BusinessAttribute pBusinessAttribute = businessAttributeService.find(businessAttribute.getId());
		if (pBusinessAttribute == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (BusinessAttribute.Type.SELECT.equals(pBusinessAttribute.getType()) || BusinessAttribute.Type.CHECKBOX.equals(pBusinessAttribute.getType())) {
			List<String> options = businessAttribute.getOptions();
			CollectionUtils.filter(options, new AndPredicate(new UniquePredicate(), new Predicate() {
				public boolean evaluate(Object object) {
					String option = (String) object;
					return StringUtils.isNotEmpty(option);
				}
			}));
			if (CollectionUtils.isEmpty(options)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			businessAttribute.setPattern(null);
		} else {
			businessAttribute.setOptions(null);
		}
		if (StringUtils.isNotEmpty(businessAttribute.getPattern())) {
			try {
				Pattern.compile(businessAttribute.getPattern());
			} catch (PatternSyntaxException e) {
				return Results.UNPROCESSABLE_ENTITY;
			}
		}
		businessAttributeService.update(businessAttribute);
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", businessAttributeService.findPage(pageable));
		return "admin/business_attribute/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		businessAttributeService.delete(ids);
		return Results.OK;
	}

}
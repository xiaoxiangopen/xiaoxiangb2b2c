package net.xiaoxiangshop.controller.admin;

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Admin;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberDepositLog;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.security.CurrentUser;
import net.xiaoxiangshop.service.MemberDepositLogService;
import net.xiaoxiangshop.service.MemberService;
import net.xiaoxiangshop.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 会员预存款
 * 
 */
@Controller("MemberDepositInfoController")
@RequestMapping("/admin/memberDepositInfo")
public class MemberDepositInfoController extends BaseController {

	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private MemberService memberService;
	/**
	 * 记录----2020-11-07新增--积分卡充值记录
	 */
	@GetMapping("/depositLog")
	public String depositLog(Long memberId, Date beginDate, Date endDate,Pageable pageable, ModelMap model) {
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		String searchProperty=pageable.getSearchProperty();
		String searchValue=pageable.getSearchValue();
		if("member_deposit_log.type".equals(searchProperty)){
			String value=searchValue;
			if(StringUtils.isNotEmpty(value)) {
				switch (value) {
					case "预存款充值":
						value = "0";
						break;
					case "预存款调整":
						value = "1";
						break;
					case "订单支付":
						value = "2";
						break;
					case "订单退款":
						value = "3";
						break;
					case "分销提成":
						value = "4";
						break;
					case "分销提现":
						value = "5";
						break;
					case "充值卡充值":
						value = "6";
						break;
				}
			}
			if(value!=null&&!"".equals(value)){
				pageable.setSearchValue(value);
			}else{
				pageable.setSearchProperty(null);
				pageable.setSearchValue(null);
			}

		}
		Member member=null;
		if("member.username".equals(searchProperty)){
			member=memberService.findByUsername(searchValue);
			pageable.setSearchProperty(null);
			pageable.setSearchValue(null);
		}
		List<Filter> filters = pageable.getFilters();
		if (beginDate != null) {
			filters.add(Filter.ge("created_date", beginDate));
		}
		if (endDate != null) {
			filters.add(Filter.le("created_date", endDate));
		}
		pageable.setFilters(filters);
		Page<MemberDepositLog> page=memberDepositLogService.findPage(member,pageable);

		if(memberId!=null){
			List<MemberDepositLog>  memberDepositLogList=page.getContent();
			List<MemberDepositLog>  newList=new ArrayList<>();
			for(MemberDepositLog  list:memberDepositLogList){
				if(list.getMember().getId().equals(memberId)){
					newList.add(list);
				}
			}
			page=new Page(newList,newList.size(),pageable);
		}
		model.addAttribute("page", page);
		pageable.setSearchProperty(searchProperty);
		pageable.setSearchValue(searchValue);
		return "admin/memberDepositInfo/depositLog";
	}

}
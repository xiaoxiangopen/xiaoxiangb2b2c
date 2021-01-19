package net.xiaoxiangshop.controller.admin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.xiaoxiangshop.*;
import net.xiaoxiangshop.Order;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.util.DatesUtil;
import net.xiaoxiangshop.util.XmlUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import net.xiaoxiangshop.audit.Audit;
import net.xiaoxiangshop.service.MemberAttributeService;
import net.xiaoxiangshop.service.MemberRankService;
import net.xiaoxiangshop.service.MemberService;
import net.xiaoxiangshop.service.UserService;

/**
 * Controller - 会员
 * 
 */
@Controller("adminMemberController")
@RequestMapping("/admin/member")
public class MemberController extends BaseController {

	@Inject
	private MemberService memberService;
	@Inject
	private UserService userService;
	@Inject
	private MemberRankService memberRankService;
	@Inject
	private MemberAttributeService memberAttributeService;

	/**
	 * 检查用户名是否存在
	 */
	@GetMapping("/check_username")
	public @ResponseBody boolean checkUsername(String username) {
		return StringUtils.isNotEmpty(username) && !memberService.usernameExists(username);
	}

	/**
	 * 检查E-mail是否唯一
	 */
	@GetMapping("/check_email")
	public @ResponseBody boolean checkEmail(Long id, String email) {
		return StringUtils.isNotEmpty(email) && !memberService.emailUnique(id, email);
	}

	/**
	 * 检查手机是否唯一
	 */
	@GetMapping("/check_mobile")
	public @ResponseBody boolean checkMobile(Long id, String mobile) {
		return StringUtils.isNotEmpty(mobile) && !memberService.mobileUnique(id, mobile);
	}

	/**
	 * 查看
	 */
	@GetMapping("/view")
	public String view(Long id,String logType,ModelMap model) {
		Member member = memberService.find(id);
		model.addAttribute("logType",logType);
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		model.addAttribute("member", member);
		return "admin/member/view";
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		return "admin/member/add";
	}

	/**
	 * 保存
	 */
	@Audit(action = "auditLog.action.admin.member.save")
	@PostMapping("/save")
	public ResponseEntity<?> save(Member member, Long memberRankId, HttpServletRequest request) {
		member.setMemberRank(memberRankService.find(memberRankId));
		if (!isValid(member, BaseEntity.Save.class)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (memberService.usernameExists(member.getUsername())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (memberService.emailExists(member.getEmail())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (memberService.mobileExists(member.getMobile())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute, memberAttributeValue);
		}
		member.setPoint(0L);
		member.setBalance(BigDecimal.ZERO);
		member.setFrozenAmount(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsLocked(false);
		member.setLockDate(null);
		member.setLastLoginIp(XmlUtils.getIp(request));
		member.setLastLoginDate(null);
		member.setDistributor(null);
		member.setSafeKey(null);
		member.setCart(null);
		member.setOrders(null);
		member.setSocialUsers(null);
		member.setPaymentTransactions(null);
		member.setMemberDepositLogs(null);
		member.setReceivers(null);
		member.setReviews(null);
		member.setConsultations(null);
		member.setProductFavorites(null);
		member.setProductNotifies(null);
		member.setPointLogs(null);
		member.setStoreFavorites(null);
		member.setFromMessages(null);
		member.setToMessages(null);
		member.setAuditLogs(null);
		member.setAftersales(null);
		member.setUser1MessageGroups(null);
		member.setUser2MessageGroups(null);
		memberService.save(member);
		return Results.OK;
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		Member member = memberService.find(id);
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList(true, true));
		model.addAttribute("member", member);
		return "admin/member/edit";
	}

	/**
	 * 更新
	 */
	@Audit(action = "auditLog.action.admin.member.update")
	@PostMapping("/update")
	public ResponseEntity<?> update(Member member, Long id, Long memberRankId, Boolean unlock, HttpServletRequest request) {
		member.setMemberRank(memberRankService.find(memberRankId));
		if (!isValid(member)) {
			return Results.UNPROCESSABLE_ENTITY;
		}
//		if (!memberService.emailUnique(id, member.getEmail())) {
//			return Results.UNPROCESSABLE_ENTITY;
//		}
//		if (!memberService.mobileUnique(id, member.getMobile())) {
//			return Results.UNPROCESSABLE_ENTITY;
//		}
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				return Results.UNPROCESSABLE_ENTITY;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute, memberAttributeValue);
		}
		Member pMember = memberService.find(id);
		if (pMember == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (BooleanUtils.isTrue(pMember.getIsLocked()) && BooleanUtils.isTrue(unlock)) {
			userService.unlock(member);
			memberService.update(member, "username", "encodedPassword", "point", "balance", "frozenAmount", "amount", "lastLoginIp", "lastLoginDate", "loginPluginId", "safeKey", "distributor", "cart", "orders", "socialUsers", "paymentTransactions", "memberDepositLogs", "receivers",
					"reviews", "consultations", "productFavorites", "productNotifies", "pointLogs");
		} else {
			memberService.update(member, "username", "encodedPassword", "point", "balance", "frozenAmount", "amount", "isLocked", "lockDate", "lastLoginIp", "lastLoginDate", "loginPluginId", "safeKey", "distributor", "cart", "orders", "socialUsers", "paymentTransactions", "memberDepositLogs",
					"receivers", "reviews", "consultations", "productFavorites", "productNotifies", "pointLogs");
		}
		return Results.OK;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {

		if(pageable.getOrderProperty()==null){
			pageable.setOrderProperty("createdDate");
			pageable.setOrderDirection(Order.Direction.DESC);
		}
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findAll());
		model.addAttribute("page", memberService.findPage(pageable));
		return "admin/member/list";
	}

	/**
	 * 删除
	 */
	@Audit(action = "auditLog.action.admin.member.delete")
	@PostMapping("/delete")
	public ResponseEntity<?> delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Member member = memberService.find(id);
				if (member != null && member.getBalance().compareTo(BigDecimal.ZERO) > 0) {
					return Results.unprocessableEntity("admin.member.deleteExistDepositNotAllowed", member.getUsername());
				}
			}
			memberService.delete(ids);
		}
		return Results.OK;
	}

	@GetMapping("/memberStatistic")
	public ResponseEntity<?> memberStatistic(Integer source) {
		if(source==0){
			source=null;
		}
		Date now = new Date();
		Date todayMinimumDate = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);
		Map<String,Object> reslut=new HashMap<>();
		reslut.put("todayAddedMemberCount", memberService.statisticCount(source,todayMinimumDate, null));
		reslut.put("yesterdayAddedMemberCount", memberService.statisticCount(source,DateUtils.addDays(todayMinimumDate, -1), DateUtils.addMilliseconds(todayMinimumDate, -1)));
		reslut.put("currentMonthAddedMemberCount", memberService.statisticCount(source,DateUtils.truncate(now, Calendar.MONTH), null));
		reslut.put("memberTotal", memberService.statisticCount(source,null,null));
		return ResponseEntity.ok(reslut);
	}

	/**
	 * 导出会员信息
	 */
	@GetMapping("/export_member")
	public void export_log(HttpServletRequest request, HttpServletResponse response, Long[] ids, Pageable pageable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		HSSFWorkbook wb = null;
		try {
			InputStream fi = new FileInputStream("/www/doc/member.xls");
			wb = (HSSFWorkbook) WorkbookFactory.create(fi);
		} catch (IOException  e) {
			e.printStackTrace();
		}


		List<Member> memberList = new ArrayList<>();
		if (ids != null && ids.length > 0) {
			memberList = memberService.findList(ids);
		} else {
			if(pageable.getOrderProperty()==null){
				pageable.setOrderProperty("createdDate");
				pageable.setOrderDirection(Order.Direction.DESC);
			}
			pageable.setPageSize(100000);
			pageable.setPageNumber(1);
			Page<Member> memberPage= memberService.findPage(pageable);
			memberList=memberPage.getContent();
		}
		HSSFSheet xssfSheet = wb.getSheetAt(0);

		Integer row_num = 1;

		for (Member member : memberList) {
            String username=member.getUsername();
			String email=member.getEmail();
			MemberRank memberRank=member.getMemberRank();
			String memberRankName="";
			if(memberRank!=null){
				memberRankName=memberRank.getName();
			}
			Date createdDate=member.getCreatedDate();
			String created_date ="";
			if(createdDate!=null){
				created_date = formatter.format(createdDate);
			}
			Long dataSource=member.getDataSource();
			String source="";
			if(dataSource!=null){
				if(dataSource==1){
					source="PC";
				}
				if(dataSource==2){
					source="APP";
				}
				if(dataSource==3){
					source="小程序";
				}
			}
            String memberZt="";
			if(member.getIsEnabled()){
				memberZt="禁用";
			} if(member.getIsLocked()){
				memberZt="锁定";
			}else{
				memberZt="正常";
			}

			HSSFRow xssfRow = xssfSheet.createRow(row_num);

			HSSFRow xssfRow_tmp = xssfSheet.getRow(0);
			HSSFCellStyle rowStyle = xssfRow_tmp.getRowStyle();

			Iterator<Cell> iterator = xssfRow_tmp.cellIterator();
			int cell_number = 0;
			while (iterator.hasNext()) {
				Cell cell_tmp = iterator.next();
				Cell cell = xssfRow.createCell(cell_number);
				cell.setCellStyle(cell_tmp.getCellStyle());
				cell_number++;
			}
			xssfRow.getCell(0).setCellValue(username);
			xssfRow.getCell(1).setCellValue(email);
			xssfRow.getCell(2).setCellValue(memberRankName);
			xssfRow.getCell(3).setCellValue(created_date);
			xssfRow.getCell(4).setCellValue(source);
			xssfRow.getCell(5).setCellValue(memberZt);
			row_num++;
		}
		OutputStream out = null;
		String extfilename = "member.xls";
		try {
			String userAgent = request.getHeader("user-agent");

			if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0
					|| userAgent.indexOf("Safari") >= 0) {
				extfilename = new String((extfilename).getBytes(), "ISO8859-1");//IE浏览器
			} else {
				extfilename = URLEncoder.encode(extfilename, "UTF8"); //其他浏览器
			}

			response.setContentType("application/msexcel");
			response.setHeader("Content-disposition", "attachment;filename=\"" + extfilename + "\"");
			out = response.getOutputStream();
			wb.write(out);
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
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

import net.xiaoxiangshop.Filter;
import net.xiaoxiangshop.Page;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.Order;
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

import net.xiaoxiangshop.Pageable;
import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.entity.Admin;
import net.xiaoxiangshop.entity.Member;
import net.xiaoxiangshop.entity.MemberDepositLog;
import net.xiaoxiangshop.security.CurrentUser;
import net.xiaoxiangshop.service.MemberDepositLogService;
import net.xiaoxiangshop.service.MemberService;

/**
 * Controller - 会员预存款
 * 
 */
@Controller("adminMemberDepositController")
@RequestMapping("/admin/member_deposit")
public class MemberDepositController extends BaseController {

	@Inject
	private MemberDepositLogService memberDepositLogService;
	@Inject
	private MemberService memberService;
	@Inject
	private OrderService orderService;
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
			Map<String, Object> item = new HashMap<>();
			item.put("id", member.getId()+"");
			item.put("name", member.getUsername());
			item.put("availableBalance", member.getAvailableBalance());
			data.add(item);
		}
		return ResponseEntity.ok(data);
	}


	/**
	 * 根据订单id 查询订单金额 20201004
	 */
	@GetMapping("/getOrderAmount")
	public  ResponseEntity<?> getOrderAmount(String sn,Long memberId, ModelMap model) {
		Map<String, Object> data = new HashMap<>();

		if (sn == null) {
			data.put("code",100);
			data.put("message","请输入订单编号");
			return ResponseEntity.ok(data);
		}
		if(memberId==null){
			data.put("code",100);
			data.put("message","会员不能为空");
			return ResponseEntity.ok(data);
		}
		Order order=orderService.findBySn(sn);
		if (order == null) {
			data.put("code",100);
			data.put("message","订单不存在");
			return ResponseEntity.ok(data);
		}

		if(memberId.longValue()!=order.getMemberId().longValue()){
			data.put("code",100);
			data.put("message","订单用户与当前会员不一致");
			return ResponseEntity.ok(data);
		}
		BigDecimal amount=new BigDecimal("0");
		if(order!=null){
			amount=order.getAmount();
		}
		data.put("code",200);
		data.put("message",amount);
		return ResponseEntity.ok(data);
	}
	/**
	 * 调整
	 */
	@GetMapping("/adjust")
	public String adjust() {
		return "admin/member_deposit/adjust";
	}

	/**
	 * 调整
	 */
	@PostMapping("/adjust")
	public ResponseEntity<?> adjust(Long memberId, BigDecimal amount, String memo, @CurrentUser Admin currentUser) {
		Member member = memberService.find(memberId);
		if (member == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (member.getBalance() == null || member.getAvailableBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		memberService.addBalance(member, amount, MemberDepositLog.Type.ADJUSTMENT, memo);
		return Results.OK;
	}

	/**
	 * 记录
	 */
	@GetMapping("/log")
	public String log(Long memberId, Date beginDate, Date endDate,Pageable pageable, ModelMap model) {
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
		return "admin/member_deposit/log";
	}

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
		List<Filter> filters = pageable.getFilters();
		if (beginDate != null) {
			filters.add(Filter.ge("created_date", beginDate));
		}
		if (endDate != null) {
			filters.add(Filter.le("created_date", endDate));
		}
		pageable.setFilters(filters);
		Page<MemberDepositLog> page=memberDepositLogService.findPage(null,pageable);

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

		pageable.setSearchValue(searchValue);
		return "admin/member_deposit/depositLog";
	}
	/**
	 * 导出会员预存款变更信息
	 */
	@GetMapping("/export_log")
	public void export_log(HttpServletRequest request, HttpServletResponse response,Long memberId, Long[] ids, Date beginDate, Date endDate, Pageable pageable) {

		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		HSSFWorkbook wb = null;
		try {
			InputStream fi = new FileInputStream("/www/doc/member_depositLog.xls");
			wb = (HSSFWorkbook) WorkbookFactory.create(fi);
		} catch (IOException  e) {
			e.printStackTrace();
		}

		List<MemberDepositLog> memberDepositLogList = new ArrayList<>();
		if (ids != null && ids.length > 0) {
			memberDepositLogList = memberDepositLogService.findList(ids);
		} else {
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
			//-----创建时间查询
			List<Filter> filters = pageable.getFilters();
			if (beginDate != null) {
				filters.add(Filter.ge("created_date", beginDate));
			}
			if (endDate != null) {
				filters.add(Filter.le("created_date", endDate));
			}
			pageable.setFilters(filters);
			pageable.setPageSize(100000);
			pageable.setPageNumber(1);
			Page<MemberDepositLog> pageMemberDepositLog = memberDepositLogService.findPage(null,pageable);
			if (pageMemberDepositLog != null && pageMemberDepositLog.getContent() != null) {
				if(memberId!=null){
					memberDepositLogList=pageMemberDepositLog.getContent();
					List<MemberDepositLog>  newList=new ArrayList<>();
					for(MemberDepositLog  list:memberDepositLogList){
						if(list.getMember().getId().equals(memberId)){
							newList.add(list);
						}
					}
					pageMemberDepositLog=new Page(newList,newList.size(),pageable);
				}
				memberDepositLogList = pageMemberDepositLog.getContent();
			}

		}
		HSSFSheet xssfSheet = wb.getSheetAt(0);

		Integer row_num = 1;

		for (MemberDepositLog log : memberDepositLogList) {
			MemberDepositLog.Type type =log.getType();
			String log_type="";
			switch (type.getValue()) {
				case 0:
					log_type = "预存款充值";
					break;
				case 1:
					log_type = "预存款调整";
					break;
				case 2:
					log_type = "订单支付";
					break;
				case 3:
					log_type = "订单退款";
					break;
				case 4:
					log_type = "分销提成";
					break;
				case 5:
					log_type= "分销提现";
					break;
				case 6:
					log_type = "充值卡充值";
					break;

			}

			Order order=log.getOrder();
			String sn="";
			if(order!=null){
				sn=order.getSn();
			}
			BigDecimal credit=log.getCredit();
			BigDecimal debit=log.getDebit();
			BigDecimal balance=log.getBalance();
			Member member=log.getMember();
			String username="";
			if(member!=null){
				username=member.getUsername();
			}
			String cardNo=log.getCardNo();
			String memo=log.getMemo();
			Date createdDate=log.getCreatedDate();
			String log_createdDate = formatter.format(createdDate);
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

			xssfRow.getCell(0).setCellValue(log_type);
			xssfRow.getCell(1).setCellValue(sn);
			xssfRow.getCell(2).setCellValue(credit+"");
			xssfRow.getCell(3).setCellValue(debit+"");
			xssfRow.getCell(4).setCellValue(balance+"");
			xssfRow.getCell(5).setCellValue(username);
			xssfRow.getCell(6).setCellValue(cardNo);
			xssfRow.getCell(7).setCellValue(memo);
			xssfRow.getCell(8).setCellValue(log_createdDate);
			row_num++;
		}

		OutputStream out = null;
		String extfilename = "member_depositLog.xls";

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
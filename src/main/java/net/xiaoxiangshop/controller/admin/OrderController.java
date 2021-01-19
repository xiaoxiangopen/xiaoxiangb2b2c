package net.xiaoxiangshop.controller.admin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.xiaoxiangshop.*;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.service.*;
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
import org.springframework.web.bind.annotation.RequestMapping;

import net.xiaoxiangshop.util.SystemUtils;

/**
 * Controller - 订单
 * 
 */
@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

	@Inject
	private OrderService orderService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private PaymentMethodService paymentMethodService;
	@Inject
	private DeliveryCorpService deliveryCorpService;
	@Inject
	private OrderShippingService orderShippingService;
	@Inject
	private MemberService memberService;
	/**
	 * 物流动态
	 */
	@GetMapping("/transit_step")
	public ResponseEntity<?> transitStep(Long shippingId) {
		Map<String, Object> data = new HashMap<>();
		OrderShipping orderShipping = orderShippingService.find(shippingId);
		if (orderShipping == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Customer()) || StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(orderShipping.getDeliveryCorpCode()) || StringUtils.isEmpty(orderShipping.getTrackingNo())) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		data.put("transitSteps", orderShippingService.getTransitSteps(orderShipping));
		return ResponseEntity.ok(data);
	}

	/**
	 * 查看
	 */
	@GetMapping("/view")
	public String view(Long id, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("methods", OrderPayment.Method.values());
		model.addAttribute("refundsMethods", OrderRefunds.Method.values());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Customer()) && StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		model.addAttribute("order", orderService.find(id));
		return "admin/order/view";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Order.Type type, Order.Status status, String memberUsername,String cardId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Date beginDate, Date endDate,String transactionSn, Pageable pageable, ModelMap model) {
		model.addAttribute("types", Order.Type.values());
		model.addAttribute("statuses", Order.Status.values());
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		model.addAttribute("memberUsername", memberUsername);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("transactionSn", transactionSn);
		model.addAttribute("isPendingReceive", isPendingReceive);
		model.addAttribute("isPendingRefunds", isPendingRefunds);
		model.addAttribute("isAllocatedStock", isAllocatedStock);
		model.addAttribute("hasExpired", hasExpired);
		model.addAttribute("cardId", cardId);

		Member member = memberService.findByUsername(memberUsername);
		if (StringUtils.isNotEmpty(memberUsername) && member == null) {
			model.addAttribute("page", Page.emptyPage(pageable));
		} else {
			//-----订单创建时间查询
			List<Filter> filters=pageable.getFilters();
			if(beginDate!=null){
				filters.add(Filter.ge("created_date",beginDate));
			}
			if(endDate!=null){
				filters.add(Filter.le("created_date",endDate));
			}
			if(transactionSn!=null){
				filters.add(Filter.other("transactionSn",transactionSn));
			}
			pageable.setFilters(filters);
			//-----订单创建时间查询
			model.addAttribute("page", orderService.findPage(type, status, null, member, null, isPendingReceive, isPendingRefunds, null, isAllocatedStock, hasExpired, pageable));
		}
		return "admin/order/list";
	}
	/**
	 * 导出顺丰快递订单信息
	 */
	@GetMapping("/export_express")
	public void export_express(HttpServletRequest request, HttpServletResponse response, Long[] ids,Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Date beginDate, Date endDate,String transactionSn, Pageable pageable) {

		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		HSSFWorkbook wb = null;
		try {
			InputStream fi = new FileInputStream("/www/doc/mail_info.xls");
			wb = (HSSFWorkbook) WorkbookFactory.create(fi);
		} catch (IOException e) {
			e.printStackTrace();
		}


		List<Order> orderList = new ArrayList<>();
		if (ids != null && ids.length > 0) {
			orderList = orderService.findList(ids);
		} else {
			Member member =null;
			if(StringUtils.isNotEmpty(memberUsername)){
				 member = memberService.findByUsername(memberUsername);
			}
				//-----订单创建时间查询
				List<Filter> filters = pageable.getFilters();
				if (beginDate != null) {
					filters.add(Filter.ge("created_date", beginDate));
				}
				if (endDate != null) {
					filters.add(Filter.le("created_date", endDate));
				}
				if (transactionSn != null) {
					filters.add(Filter.other("transactionSn", transactionSn));
				}
				pageable.setFilters(filters);
				//-----订单创建时间查询
				pageable.setPageSize(100000);
				pageable.setPageNumber(1);
				Page<Order> pageOrder = orderService.findPage(type, status, null, member, null, isPendingReceive, isPendingRefunds,  null, isAllocatedStock, hasExpired, pageable);
				if (pageOrder != null && pageOrder.getContent() != null) {
					orderList = pageOrder.getContent();
				}

		}
			HSSFSheet xssfSheet = wb.getSheetAt(0);

			Integer row_num = 1;

			for (Order order : orderList) {
				String orderSn = order.getSn();
				String order_date = formatter.format(order.getCreatedDate());
				String order_amount = order.getAmount().setScale(2).toString();
				String order_status = "";
				Order.Status orderStatus = order.getStatus();
				switch (orderStatus.getValue()) {
					case 0:
						order_status = "等待付款";
						break;
					case 1:
						order_status = "备货中";
						break;
					case 2:
						order_status = "待发货";
						break;
					case 3:
						order_status = "已发货";
						break;
					case 4:
						order_status = "已收货";
						break;
					case 5:
						order_status = "已完成";
						break;
					case 6:
						order_status = "已失败";
						break;
					case 7:
						order_status = "已取消";
						break;
					case 8:
						order_status = "已拒绝";
						break;
				}

//            PaymentTransaction paymentTransaction = paymentTransactionDao.findByAttribute("orders", id);
				String paid = "未完成支付";
//            if (null != paymentTransaction && paymentTransaction.getIsSuccess()) {
//                paid = "已完成支付";
//            }
				if (orderStatus.getValue() == 1 || orderStatus.getValue() == 2 || orderStatus.getValue() == 3 || orderStatus.getValue() == 4 ||
						orderStatus.getValue() == 5) {
					paid = "已完成支付";
				}

				String customer_name = order.getConsignee();
				String phone = order.getPhone();
				String address = order.getAreaName() + order.getAddress();

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

				xssfRow.getCell(0).setCellValue(orderSn);
				xssfRow.getCell(1).setCellValue(order_date);
				xssfRow.getCell(2).setCellValue(order_amount);
				xssfRow.getCell(3).setCellValue(order_status);
				xssfRow.getCell(4).setCellValue(paid);
				xssfRow.getCell(5).setCellValue(customer_name);
				xssfRow.getCell(6).setCellValue(phone);
				xssfRow.getCell(7).setCellValue(address);
				row_num++;
			}


			OutputStream out = null;
			String extfilename = "sfexpress.xls";

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
package net.xiaoxiangshop.controller.business;

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

import com.alibaba.fastjson.JSON;
import net.xiaoxiangshop.*;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import net.xiaoxiangshop.exception.UnauthorizedException;
import net.xiaoxiangshop.security.CurrentStore;
import net.xiaoxiangshop.security.CurrentUser;
import net.xiaoxiangshop.util.SystemUtils;

/**
 * Controller - 订单
 */
@Controller("businessOrderController")
@RequestMapping("/business/order")
public class OrderController extends BaseController {

    private static final Logger _logger = LoggerFactory.getLogger(OrderController.class);
    @Inject
    private AreaService areaService;
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
    @Inject
    private ErpSyncService erpSyncService;

    /**
     *  网购积分卡
     */
    @Value("${payment_type.ONLINE_CASH_CARD}")
    private String ONLINE_CASH_CARD;



    /**
     *  银联
     */
    @Value("${payment_type.UNIONPAY}")
    private String UNIONPAY;

    /**
     * 微信
     */
    @Value("${payment_type.WEIXINPAY}")
    private String WEIXINPAY;

    /**
     *  小程序微信
     */
    @Value("${payment_type.WEIXIN_MINI_PAY}")
    private String WEIXIN_MINI_PAY;

    /**
     * 银联二维码(云闪付)
     */
    @Value("${payment_type.UNION_BARCODE_PAY}")
    private String UNION_BARCODE_PAY;


    /**
     * 支付宝
     */
    @Value("${payment_type.ALIPAY}")
    private String ALIPAY;


    /**
     * 积分卡
     */
    @Value("${payment_type.CASH_CARD}")
    private String CASH_CARD;

    /**
     * 添加属性
     */
    @ModelAttribute
    public void populateModel(Long orderId, @CurrentStore Store currentStore, ModelMap model) {
        Order order = orderService.find(orderId);
        if (order != null && !currentStore.equals(order.getStore())) {
            throw new UnauthorizedException();
        }
        model.addAttribute("order", order);
    }

    /**
     * 获取订单锁
     */
    @PostMapping("/acquire_lock")
    public @ResponseBody
    boolean acquireLock(@ModelAttribute(binding = false) Order order) {
        return order != null && orderService.acquireLock(order);
    }

    /**
     * 计算
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@ModelAttribute(binding = false) Order order, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount) {
        if (order == null) {
            return Results.UNPROCESSABLE_ENTITY;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("amount", orderService.calculateAmount(order.getPrice(), order.getFee(), freight, tax,offsetAmount));
        return ResponseEntity.ok(data);
    }


    /**
     * 完成发货
     */
    @GetMapping("/shipping_over")
    public ResponseEntity<?> shippingOver(Long orderId) {
        Map<String, Object> data = new HashMap<>();
        Order order=orderService.find(orderId);
        order.setStatus(Order.Status.SHIPPED);
        orderService.update(order);
        return Results.OK;
    }


    /**
     * 物流动态
     */
    @GetMapping("/transit_step")
    public ResponseEntity<?> transitStep(Long shippingId, @CurrentStore Store currentStore) {
        Map<String, Object> data = new HashMap<>();
        OrderShipping orderShipping = orderShippingService.find(shippingId);
        if (orderShipping == null || !currentStore.equals(orderShipping.getOrder().getStore())) {
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
     * 编辑
     */
    @GetMapping("/edit")
    public String edit(@ModelAttribute(binding = false) Order order, ModelMap model) {
        if (order == null || order.hasExpired() || (!Order.Status.PENDING_PAYMENT.equals(order.getStatus()) && !Order.Status.PENDING_REVIEW.equals(order.getStatus()))) {
            return UNPROCESSABLE_ENTITY_VIEW;
        }
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("order", order);
        return "business/order/edit";
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute(binding = false) Order order, Long areaId, Long paymentMethodId, Long shippingMethodId, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount, Long rewardPoint, String consignee, String address, String zipCode, String phone,
                                    String invoiceTitle, String invoiceTaxNumber, String memo, @CurrentUser Business currentUser) {
        Area area = areaService.find(areaId);
        PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
        ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);

//        if (order == null || !orderService.acquireLock(order)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        if (order.hasExpired() || (!Order.Status.PENDING_PAYMENT.equals(order.getStatus()) && !Order.Status.PENDING_REVIEW.equals(order.getStatus()))) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (order.getStatus().equals(Order.Status.PENDING_REVIEW) || order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            Setting setting = SystemUtils.getSetting();
            offsetAmount = BigDecimal.ZERO;
            tax = setting.setScale(order.getTax());
            freight = setting.setScale(order.getFreight());
        }

        Invoice invoice = StringUtils.isNotEmpty(invoiceTitle) && StringUtils.isNotEmpty(invoiceTaxNumber) ? new Invoice(invoiceTitle, invoiceTaxNumber, null) : null;
        order.setTax(invoice != null ? tax : BigDecimal.ZERO);
        order.setOffsetAmount(offsetAmount);
        order.setRewardPoint(rewardPoint);
        order.setMemo(memo);
        order.setInvoice(invoice);
        order.setPaymentMethod(paymentMethod);
        if (order.getIsDelivery()) {
            order.setFreight(freight.compareTo(BigDecimal.ZERO) > 0 ? freight : BigDecimal.ZERO);
            order.setConsignee(consignee);
            order.setAddress(address);
            order.setZipCode(zipCode);
            order.setPhone(phone);
            order.setArea(area);
            order.setShippingMethod(shippingMethod);
            if (!isValid(order, Order.Delivery.class)) {
                return Results.UNPROCESSABLE_ENTITY;
            }
        } else {
            order.setFreight(BigDecimal.ZERO);
            order.setConsignee(null);
            order.setAreaName(null);
            order.setAddress(null);
            order.setZipCode(null);
            order.setPhone(null);
            order.setShippingMethodName(null);
            order.setArea(null);
            order.setShippingMethod(null);
            if (!isValid(order)) {
                return Results.UNPROCESSABLE_ENTITY;
            }
        }
        orderService.modify(order);

        return Results.OK;
    }


    /**
     * 查看
     */
    @GetMapping("/view")
    public String view(@ModelAttribute(binding = false) Order order, ModelMap model) {
        if (order == null) {
            return UNPROCESSABLE_ENTITY_VIEW;
        }

        Setting setting = SystemUtils.getSetting();
        model.addAttribute("methods", OrderPayment.Method.values());
        model.addAttribute("refundsMethods", OrderRefunds.Method.values());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Customer()) && StringUtils.isNotEmpty(setting.getKuaidi100Key()));
        model.addAttribute("order", order);
        return "business/order/view";
    }

    /**
     * 审核
     */
    @PostMapping("/review")
    public ResponseEntity<?> review(@ModelAttribute(binding = false) Order order, Boolean passed, @CurrentUser Business currentUser) {
        if (order == null || order.hasExpired() || !Order.Status.PENDING_REVIEW.equals(order.getStatus()) || passed == null) {
            return Results.UNPROCESSABLE_ENTITY;
        }
//        if (!orderService.acquireLock(order, currentUser)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        orderService.review(order, passed);

        return Results.OK;
    }

    /**
     * 收款
     */
    @PostMapping("/payment")
    public ResponseEntity<?> payment(OrderPayment orderPaymentForm, @ModelAttribute(binding = false) Order order, Long paymentMethodId, @CurrentUser Business currentUser, @CurrentStore Store currentStore) {
        if (order == null || !Store.Type.SELF.equals(currentStore.getType())) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        orderPaymentForm.setOrder(order);
        orderPaymentForm.setPaymentMethod(paymentMethodService.find(paymentMethodId));
        if (!isValid(orderPaymentForm)) {
            return Results.UNPROCESSABLE_ENTITY;
        }
//        if (!orderService.acquireLock(order, currentUser)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        orderPaymentForm.setFee(BigDecimal.ZERO);
        orderService.payment(order, orderPaymentForm);

        return Results.OK;
    }

    /**
     * 退款
     */
    @PostMapping("/refunds")
    public ResponseEntity<?> refunds(OrderRefunds orderRefundsForm, @ModelAttribute(binding = false) Order order, Long paymentMethodId, @CurrentUser Business currentUser) {
        if (order == null || order.hasExpired()) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (order.getRefundableAmount().compareTo(BigDecimal.ZERO) <= 0 && !order.getIsAllowRefund()) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (orderRefundsForm.getAmount().compareTo(order.getAmountPaid()) > 0 || order.getAmountPaid().compareTo(BigDecimal.ZERO) <= 0) {
            return Results.UNPROCESSABLE_ENTITY;
        }

        orderRefundsForm.setOrder(order);
        orderRefundsForm.setPaymentMethod(paymentMethodService.find(paymentMethodId));
        if (!isValid(orderRefundsForm)) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        if (OrderRefunds.Method.DEPOSIT.equals(orderRefundsForm.getMethod()) && orderRefundsForm.getAmount().compareTo(order.getStore().getBusiness().getAvailableBalance()) > 0) {
            return Results.UNPROCESSABLE_ENTITY;
        }
//        if (!orderService.acquireLock(order, currentUser)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        orderService.refunds(order, orderRefundsForm);

        return Results.OK;
    }

    /**
     * 发货
     */
    @PostMapping("/shipping")
    public ResponseEntity<?> shipping(OrderShipping orderShippingForm, @ModelAttribute(binding = false) Order order, Long shippingMethodId, Long deliveryCorpId, Long areaId, @CurrentUser Business currentUser) {
//        || order.getShippableQuantity() <= 0
        if (order == null ) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        boolean isDelivery = false;
        for (Iterator<OrderShippingItem> iterator = orderShippingForm.getOrderShippingItems().iterator(); iterator.hasNext(); ) {
            OrderShippingItem orderShippingItem = iterator.next();
            if (orderShippingItem == null || StringUtils.isEmpty(orderShippingItem.getSn()) || orderShippingItem.getQuantity() == null || orderShippingItem.getQuantity() <= 0) {
                iterator.remove();
                continue;
            }
            OrderItem orderItem = order.getOrderItem(orderShippingItem.getSn());

            if (orderItem == null || orderShippingItem.getQuantity() > orderItem.getShippableQuantity()) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            Sku sku = orderItem.getSku();
//            if (sku != null && orderShippingItem.getQuantity() > sku.getStock()) {
//                return Results.UNPROCESSABLE_ENTITY;
//            }
            orderShippingItem.setName(orderItem.getName());
            orderShippingItem.setIsDelivery(orderItem.getIsDelivery());
            orderShippingItem.setSku(sku);
            orderShippingItem.setOrderShipping(orderShippingForm);
            orderShippingItem.setSpecifications(orderItem.getSpecifications());
            orderShippingItem.setId(IdWorker.getId());
            orderShippingItem.setCreatedDate(new Date());
            orderShippingItem.setVersion(0L);
            if (orderItem.getIsDelivery()) {
                isDelivery = true;
            }
        }
        orderShippingForm.setOrder(order);
        orderShippingForm.setShippingMethod(shippingMethodService.find(shippingMethodId));
        orderShippingForm.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
        orderShippingForm.setArea(areaService.find(areaId));
//        if (isDelivery) {
//            if (!isValid(orderShippingForm, OrderShipping.Delivery.class)) {
//                return Results.UNPROCESSABLE_ENTITY;
//            }
//        } else {
//            orderShippingForm.setShippingMethod((String) null);
//            orderShippingForm.setDeliveryCorp((String) null);
//            orderShippingForm.setDeliveryCorpUrl(null);
//            orderShippingForm.setDeliveryCorpCode(null);
//            orderShippingForm.setTrackingNo(null);
//            orderShippingForm.setFreight(null);
//            orderShippingForm.setConsignee(null);
//            orderShippingForm.setArea((String) null);
//            orderShippingForm.setAddress(null);
//            orderShippingForm.setZipCode(null);
//            orderShippingForm.setPhone(null);
//            if (!isValid(orderShippingForm)) {
//                return Results.UNPROCESSABLE_ENTITY;
//            }
//        }
//        if (!orderService.acquireLock(order, currentUser)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        orderService.shipping(order, orderShippingForm);

        return Results.OK;
    }

    /**
     * 退货
     */
    @PostMapping("/returns")
    public ResponseEntity<?> returns(OrderReturns orderReturnsForm, @ModelAttribute(binding = false) Order order, Long shippingMethodId, Long deliveryCorpId, Long areaId, @CurrentUser Business currentUser) {
        if (order == null || order.getReturnableQuantity() <= 0) {
            return Results.UNPROCESSABLE_ENTITY;
        }
        for (Iterator<OrderReturnsItem> iterator = orderReturnsForm.getOrderReturnsItems().iterator(); iterator.hasNext(); ) {
            OrderReturnsItem orderReturnsItem = iterator.next();
            if (orderReturnsItem == null || StringUtils.isEmpty(orderReturnsItem.getSn()) || orderReturnsItem.getQuantity() == null || orderReturnsItem.getQuantity() <= 0) {
                iterator.remove();
                continue;
            }
            OrderItem orderItem = order.getOrderItem(orderReturnsItem.getSn());
            if (orderItem == null || orderReturnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
                return Results.UNPROCESSABLE_ENTITY;
            }
            orderReturnsItem.setName(orderItem.getName());
            orderReturnsItem.setOrderReturns(orderReturnsForm);
            orderReturnsItem.setSpecifications(orderItem.getSpecifications());
        }
        orderReturnsForm.setOrder(order);
        orderReturnsForm.setShippingMethod(shippingMethodService.find(shippingMethodId));
        orderReturnsForm.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
        orderReturnsForm.setArea(areaService.find(areaId));
        if (!isValid(orderReturnsForm)) {
            return Results.UNPROCESSABLE_ENTITY;
        }
//        if (!orderService.acquireLock(order, currentUser)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        orderService.returns(order, orderReturnsForm);

        return Results.OK;
    }

    /**
     * 完成
     */
    @PostMapping("/complete")
    public ResponseEntity<?> complete(@ModelAttribute(binding = false) Order order, @CurrentUser Business currentUser) {
        if (order == null || order.hasExpired() || !Order.Status.RECEIVED.equals(order.getStatus())) {
            return Results.UNPROCESSABLE_ENTITY;
        }
//        if (!orderService.acquireLock(order, currentUser)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        orderService.complete(order);

        return Results.OK;
    }

    /**
     * 失败
     */
    @PostMapping("/fail")
    public ResponseEntity<?> fail(@ModelAttribute(binding = false) Order order, @CurrentUser Business currentUser) {
        if (order == null || order.hasExpired() || (!Order.Status.PENDING_SHIPMENT.equals(order.getStatus()) && !Order.Status.SHIPPED.equals(order.getStatus()) && !Order.Status.RECEIVED.equals(order.getStatus()))) {
            return Results.UNPROCESSABLE_ENTITY;
        }
//        if (!orderService.acquireLock(order, currentUser)) {
//            return Results.UNPROCESSABLE_ENTITY;
//        }
        orderService.fail(order);

        return Results.OK;
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public String list(Order.Type type, Order.Status status, String memberUsername,String cardId, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Date beginDate, Date endDate,String transactionSn,Pageable pageable, @CurrentStore Store currentStore, ModelMap model) {
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
            model.addAttribute("page", orderService.findPage(type, status, currentStore, member, null, isPendingReceive, isPendingRefunds,  null, isAllocatedStock, hasExpired, pageable));
        }
        return "business/order/list";
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(Long[] ids, @CurrentUser Business currentUser, @CurrentStore Store currentStore) {
        if (ids != null) {
            for (Long id : ids) {
                Order order = orderService.find(id);
                if (order == null || !currentStore.equals(order.getStore())) {
                    return Results.UNPROCESSABLE_ENTITY;
                }
//                if (!orderService.acquireLock(order, currentUser)) {
//                    return Results.unprocessableEntity("business.order.deleteLockedNotAllowed", order.getSn());
//                }
                if (!order.canDelete()) {
                    return Results.unprocessableEntity("business.order.deleteStatusNotAllowed", order.getSn());
                }
            }
            orderService.delete(ids);
        }
        return Results.OK;
    }

    /**
     * 导出订单
     */
    @GetMapping("/export_order")
    public void export_order(HttpServletRequest request, HttpServletResponse response, Long orderId) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Order order = orderService.find(orderId);
        String orderSn = order.getSn();
        String customer_name = order.getConsignee();
        String phone = order.getPhone();
        String order_date = formatter.format(order.getCreatedDate());
        String address = order.getAreaName() + order.getAddress();
        String zipcode = order.getZipCode();
        String memo = order.getMemo();
        String invoice_title = "";
        String invoice_content = "";
        Invoice invoice = order.getInvoice();

        if (null != invoice) {
            invoice_title = invoice.getTitle();
            if (null == invoice_title)
                invoice_title = "";

            invoice_content = invoice.getContent();
            if (null == invoice_content)
                invoice_content = "";
        }

        String username = "";
        Member member = order.getMember();
        if (null != member)
            username = member.getUsername();

        String vip_card = member.getAttributeValue0();

        if (null == vip_card || vip_card.equals("0"))
            vip_card = "无";

        BigDecimal balance_pay = BigDecimal.ZERO;
        BigDecimal weixn_pay = BigDecimal.ZERO;
        BigDecimal union_pay = BigDecimal.ZERO;
        BigDecimal alipay_pay = BigDecimal.ZERO;
        String payment = "";
        Set<OrderPayment> orderPaymentSet = order.getOrderPayments();

        if (orderPaymentSet.size() > 0) {
            for (OrderPayment orderPayment : orderPaymentSet) {
                String payment_method = orderPayment.getPaymentMethod();
                if (null == payment_method) {
                    payment_method = "网购积分卡|" + ONLINE_CASH_CARD;
                }

                try {
                    String method_name = payment_method.split("\\|")[1];
                    if (method_name.equals(ONLINE_CASH_CARD) || method_name.equals(CASH_CARD)) {
                        balance_pay = balance_pay.add(orderPayment.getAmount());
                        if(payment.indexOf("网乐购")==-1){
                            payment += "网乐购账户金额支付、";
                        }
                    }

                    if (method_name.equals(UNIONPAY) || method_name.equals(UNION_BARCODE_PAY)) {
                        union_pay = orderPayment.getAmount();
                        payment += "在线银联、";
                    }

                    if (method_name.equals(WEIXINPAY) || method_name.equals(WEIXIN_MINI_PAY)) {
                        weixn_pay = orderPayment.getAmount();
                        payment += "微信支付、";
                    }

                    if (method_name.equals(ALIPAY)) {
                        alipay_pay = orderPayment.getAmount();
                        payment += "支付宝、";
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        payment = payment.substring(0, payment.length() - 1);

        List<OrderItem> orderItemList = order.getOrderItems();
        HSSFWorkbook wb = null;

        try {
            InputStream fi = new FileInputStream("/www/doc/foxtable_order.xls");
            wb = (HSSFWorkbook) WorkbookFactory.create(fi);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet xssfSheet = wb.getSheetAt(0);
        HSSFRow xssfRow = xssfSheet.getRow(1);
        xssfRow.getCell(1).setCellValue(orderSn);
        xssfRow.getCell(4).setCellValue(customer_name);
        xssfRow.getCell(7).setCellValue(phone);
        xssfRow = xssfSheet.getRow(2);
        xssfRow.getCell(1).setCellValue(order_date);
        xssfRow.getCell(4).setCellValue(username);
        xssfRow.getCell(7).setCellValue(payment);
        xssfRow = xssfSheet.getRow(3);
        xssfRow.getCell(1).setCellValue(address);
        xssfRow.getCell(7).setCellValue(zipcode);
        xssfRow = xssfSheet.getRow(4);
        xssfRow.getCell(1).setCellValue(memo);
        //订单明细表
        Integer item_size = orderItemList.size();
        Integer left_rows = 30;
        Integer start_rows = 1;
        Integer head = 7;

        HSSFRow xssfRow_b0 = xssfSheet.getRow(head + left_rows);
        HSSFRow xssfRow_b1 = xssfSheet.getRow(head + left_rows + 1);
        HSSFRow xssfRow_b2 = xssfSheet.getRow(head + left_rows + 2);
        HSSFRow xssfRow_b3 = xssfSheet.getRow(head + left_rows + 3);
        HSSFRow xssfRow_b4 = xssfSheet.getRow(head + left_rows + 4);

        if (item_size > 0) {

            if (item_size > left_rows)
                xssfSheet.shiftRows(head + left_rows, head + left_rows + 5, item_size - left_rows);

            int item_row_index = 0;

            for (OrderItem orderItem : orderItemList) {
                if (item_row_index >= left_rows) {
                    xssfRow = xssfSheet.createRow(7 + item_row_index);
                    HSSFRow xssfRow_tmp = xssfSheet.getRow(7);
                    HSSFCellStyle rowStyle = xssfRow_tmp.getRowStyle();
                    xssfRow.setHeight(xssfRow_tmp.getHeight());
                    xssfRow.setHeightInPoints(xssfRow_tmp.getHeightInPoints());
                    xssfRow.setZeroHeight(xssfRow_tmp.getZeroHeight());

                    Iterator<Cell> iterator = xssfRow_tmp.cellIterator();
                    int cell_number = 0;

                    while (iterator.hasNext()) {
                        Cell cell_tmp = iterator.next();
                        Cell cell = xssfRow.createCell(cell_number);
                        cell.setCellStyle(cell_tmp.getCellStyle());

                        cell_number++;
                    }

                } else
                    xssfRow = xssfSheet.getRow(item_row_index + head);

                item_row_index++;
                Sku sku = orderItem.getSku();
                String counter_name = sku.getCounterName();
                String internal_number = sku.getInternalNumber();
                Product product = sku.getProduct();
                String product_name = orderItem.getName();

                //折前单价,显示SKU原价
                BigDecimal product_normal_price1 = orderItem.getPrice();
                BigDecimal product_normal_price2 = orderItem.getSku().getPrice();
                //折后单价
                orderItem.setCounterdiscount(orderItem.getCounterdiscount()==null?BigDecimal.ZERO:orderItem.getCounterdiscount() );
                BigDecimal product_order_price =product_normal_price1.subtract(orderItem.getCounterdiscount());

                Integer quantity = orderItem.getQuantity();
                String specificationValues = JSON.toJSONString(orderItem.getSpecifications());
//                BigDecimal subtotal = orderItem.getSubtotal();
                BigDecimal subtotal =orderItem.getSubtotal().subtract(orderItem.getTotaldiscount());
//                        product_order_price.multiply(new BigDecimal(orderItem.getQuantity()));

                xssfRow.getCell(0).setCellValue(counter_name);
                xssfRow.getCell(1).setCellValue(internal_number);
                xssfRow.getCell(2).setCellValue(product_name);
                xssfRow.getCell(4).setCellValue(product_normal_price2.setScale(2).toString());
                xssfRow.getCell(5).setCellValue(product_order_price.setScale(2).toString());
                xssfRow.getCell(6).setCellValue(quantity);
                xssfRow.getCell(7).setCellValue(specificationValues);
                xssfRow.getCell(8).setCellValue(subtotal.setScale(2).toString());

                List<String> productCategoryList = new ArrayList<>();
                try {
                    ProductCategory productCategory = product.getProductCategory();
                    productCategoryList.add(productCategory.getName() + "|" + productCategory.getId());
                    List<ProductCategory> productCategories = new ArrayList<>();
                    productCategories = productCategory.getParents();

                    for (int i = productCategories.size() - 1; i >= 0; i--)
                        productCategoryList.add(productCategories.get(i).getName() + "|" + productCategories.get(i).getId());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Setting setting = SystemUtils.getSetting();
                String zgdmCScategoryCode = setting.getZgdmCScategoryCode();
                String zgdmCScategorySubCode = setting.getZgdmCScategorySubCode();
                String zgdmCScategoryName = setting.getZgdmCScategoryName();
                String[] category = null;

                try {

                    xssfRow.getCell(10).setCellValue("无分类");
                    category = productCategoryList.get(productCategoryList.size() - 1).split("\\|");
                    xssfRow.getCell(10).setCellValue(category[0]);

                    if (zgdmCScategoryCode.indexOf("," + category[1] + ",") > -1)
                        xssfRow.getCell(0).setCellValue(zgdmCScategoryName);

                    xssfRow.getCell(11).setCellValue("无分类");
                    category = productCategoryList.get(productCategoryList.size() - 2).split("\\|");
                    xssfRow.getCell(11).setCellValue(category[0]);

                    if (zgdmCScategorySubCode.indexOf("," + category[1] + ",") > -1)
                        xssfRow.getCell(0).setCellValue(zgdmCScategoryName);

                    xssfRow.getCell(12).setCellValue("无分类");
                    xssfRow.getCell(12).setCellValue(productCategoryList.get(productCategoryList.size() - 3).split("\\|")[0]);

                    xssfRow.getCell(13).setCellValue(product.getIsMarketable() ? "上架" : "下架");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (item_size > left_rows)
            xssfRow = xssfSheet.getRow(head + item_size);
        else
            xssfRow = xssfSheet.getRow(head + left_rows);

        Integer bottom_row_num = xssfRow.getRowNum() - 1;

        xssfRow.setHeight(xssfSheet.getRow(bottom_row_num).getHeight());
        xssfRow.setHeightInPoints(xssfSheet.getRow(bottom_row_num).getHeightInPoints());
        xssfRow.setZeroHeight(xssfSheet.getRow(bottom_row_num).getZeroHeight());

        xssfRow.getCell(1).setCellValue(vip_card);
        xssfRow.getCell(4).setCellValue(order.getQuantity());
        xssfRow.getCell(6).setCellValue(order.getFreight().setScale(2).toString() + "元");
        xssfRow.getCell(8).setCellValue(order.getAmount().subtract(order.getFreight()).setScale(2).toString() + "元");


        if (item_size > left_rows)
            xssfRow = xssfSheet.getRow(head + item_size + 1);
        else
            xssfRow = xssfSheet.getRow(head + left_rows + 1);

        xssfRow.setHeight(xssfSheet.getRow(bottom_row_num).getHeight());
        xssfRow.setHeightInPoints(xssfSheet.getRow(bottom_row_num).getHeightInPoints());
        xssfRow.setZeroHeight(xssfSheet.getRow(bottom_row_num).getZeroHeight());

        xssfRow.getCell(1).setCellValue(invoice_title);
        xssfRow.getCell(4).setCellValue(invoice_content);
        xssfRow.getCell(8).setCellValue(order.getAmount().setScale(2).toString() + "元");

        xssfRow.getCell(10).setCellValue(balance_pay.setScale(2).toString());
        xssfRow.getCell(11).setCellValue(union_pay.setScale(2).toString());
        xssfRow.getCell(12).setCellValue(alipay_pay.setScale(2).toString());
        xssfRow.getCell(13).setCellValue(weixn_pay.setScale(2).toString());

        int merge_size = left_rows;

        if (item_size > left_rows)
            merge_size = item_size;

        for (int i = 0; i < merge_size; i++)
            xssfSheet.addMergedRegion(new CellRangeAddress(head + i, head + i, 2, 3));

        xssfSheet.addMergedRegion(new CellRangeAddress(1, head + merge_size + 1, 9, 9));

//        if (item_size < left_rows) {
//            for (int i = 0; i < left_rows - item_size; i++) {
//                xssfRow = xssfSheet.getRow(+head + left_rows + i - 1);
//                xssfRow.getCell(10).setCellValue("");
//                xssfRow.getCell(11).setCellValue("");
//                xssfRow.getCell(12).setCellValue("");
//                xssfRow.getCell(13).setCellValue("");
//            }
//        }


        head = 6;
        HSSFSheet xssfSheet_1 = wb.getSheetAt(1);
        xssfRow = xssfSheet_1.getRow(0);
        xssfRow.getCell(7).setCellValue(orderSn);
        xssfRow = xssfSheet_1.getRow(1);
        xssfRow.getCell(1).setCellValue(customer_name);
        xssfRow.getCell(3).setCellValue(vip_card);
        xssfRow.getCell(5).setCellValue(phone);
        xssfRow.getCell(7).setCellValue(order_date);
        xssfRow = xssfSheet_1.getRow(2);
        xssfRow.getCell(1).setCellValue(address);
        xssfRow.getCell(7).setCellValue(zipcode);
        xssfRow = xssfSheet_1.getRow(3);
        xssfRow.getCell(1).setCellValue(memo);

        if (item_size > 0) {
            if (item_size > left_rows)
                xssfSheet_1.shiftRows(head + left_rows, head + left_rows + 5, item_size - left_rows, true, true);

            int item_row_index = 0;

            for (OrderItem orderItem : orderItemList) {
                if (item_row_index >= left_rows) {
                    xssfRow = xssfSheet_1.createRow(head + item_row_index);
                    HSSFRow xssfRow_tmp = xssfSheet_1.getRow(head);
                    HSSFCellStyle rowStyle = xssfRow_tmp.getRowStyle();
                    xssfRow.setHeight(xssfRow_tmp.getHeight());
                    xssfRow.setHeightInPoints(xssfRow_tmp.getHeightInPoints());
                    xssfRow.setZeroHeight(xssfRow_tmp.getZeroHeight());

                    Iterator<Cell> iterator = xssfRow_tmp.cellIterator();
                    int cell_number = 0;

                    while (iterator.hasNext()) {
                        Cell cell_tmp = iterator.next();
                        Cell cell = xssfRow.createCell(cell_number);
                        cell.setCellStyle(cell_tmp.getCellStyle());
                        cell_number++;
                    }

                } else
                    xssfRow = xssfSheet_1.getRow(item_row_index + head);

                item_row_index++;
                Sku sku = orderItem.getSku();
                String counter_name = sku.getCounterName();
                String internal_number = sku.getInternalNumber();
                Product product = sku.getProduct();
                String product_name = orderItem.getName();
                BigDecimal product_normal_price = product.getPrice();
//                BigDecimal product_order_price = orderItem.getPrice();
                BigDecimal product_order_price = product_normal_price.subtract(orderItem.getCounterdiscount());
                Integer quantity = orderItem.getQuantity();
                String specificationValues = JSON.toJSONString(orderItem.getSpecifications());
//                BigDecimal subtotal = orderItem.getSubtotal();
                BigDecimal subtotal = orderItem.getSubtotal().subtract(orderItem.getTotaldiscount());
                xssfRow.getCell(0).setCellValue(counter_name);
                xssfRow.getCell(1).setCellValue(internal_number);
                xssfRow.getCell(2).setCellValue(product_name);
//                xssfRow.getCell(4).setCellValue(product_normal_price.setScale(2).toString());
                xssfRow.getCell(5).setCellValue(product_order_price.setScale(2).toString());
                xssfRow.getCell(6).setCellValue(quantity);
                xssfRow.getCell(7).setCellValue(specificationValues);
                xssfRow.getCell(8).setCellValue(subtotal.setScale(2).toString());
                List<String> productCategoryList = new ArrayList<>();
                ProductCategory productCategory = product.getProductCategory();
                productCategoryList.add(productCategory.getName() + "|" + productCategory.getId());
                List<ProductCategory> productCategories = new ArrayList<>();
                productCategories = productCategory.getParents();

                for (int i = productCategories.size() - 1; i >= 0; i--)
                    productCategoryList.add(productCategories.get(i).getName() + "|" + productCategories.get(i).getId());

                Setting setting = SystemUtils.getSetting();
                String zgdmCScategoryCode = setting.getZgdmCScategoryCode();
                String zgdmCScategoryName = setting.getZgdmCScategoryName();
                String[] category = null;

                try {

                    category = productCategoryList.get(productCategoryList.size() - 1).split("\\|");
                    if (zgdmCScategoryCode.indexOf("," + category[1] + ",") > -1)
                        xssfRow.getCell(0).setCellValue(zgdmCScategoryName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (item_size > left_rows)
            xssfRow = xssfSheet_1.getRow(head + item_size);
        else
            xssfRow = xssfSheet_1.getRow(head + left_rows);

        xssfRow.getCell(1).setCellValue("");
        xssfRow.getCell(3).setCellValue(order.getQuantity());
        xssfRow.getCell(5).setCellValue(order.getFreight().setScale(2).toString() + "元");
        xssfRow.getCell(7).setCellValue(order.getAmount().setScale(2).toString() + "元");
        xssfRow = xssfSheet_1.getRow(xssfRow.getRowNum() + 1);
        xssfRow.getCell(1).setCellValue(invoice_title);
        xssfRow.getCell(5).setCellValue(invoice_content);


        if (item_size > left_rows)
            merge_size = item_size;

        for (int i = 0; i < merge_size; i++)
            xssfSheet_1.addMergedRegion(new CellRangeAddress(head + i, head + i, 2, 4));

        OutputStream out = null;
        String extfilename = orderSn + ".xls";

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
                order.setIsExport(true);
                order.setExportDate(new Date());
                orderService.update(order);
            } catch (IOException e) {
                order.setIsExport(false);
                order.setExportDate(new Date());
                orderService.update(order);
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出顺丰快递订单信息
     */
    @GetMapping("/export_express")
    public void export_express(HttpServletRequest request, HttpServletResponse response, Long[] ids) {

        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        HSSFWorkbook wb = null;
        try {
            InputStream fi = new FileInputStream("/www/doc/mail_info.xls");
            wb = (HSSFWorkbook) WorkbookFactory.create(fi);
        } catch (IOException  e) {
            e.printStackTrace();
        }

        HSSFSheet xssfSheet = wb.getSheetAt(0);

        Integer row_num = 1;

        for (Long id : ids) {
            Order order = orderService.find(id);
            String orderSn = order.getSn();
            String order_date = formatter.format(order.getCreatedDate());
            String order_amount = order.getAmount().setScale(2).toString();
            String order_status = "";
            Order.Status status = order.getStatus();

            switch (status.getValue()) {
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
            if (status.getValue() == 1 || status.getValue() == 2 || status.getValue() == 3 || status.getValue() == 4 ||
                    status.getValue() == 5) {
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

    /**
     * 上传订单到中台
     */
    @GetMapping("/uploadOrder")
    public ResponseEntity<?> uploadOrder(Long orderId) {
        Order order=orderService.find(orderId);
        HashMap map= erpSyncService.uploadOrder(order.getSn());
        String msg="订单上传失败！";
        if(("0").equals(map.get("resultCode"))){
            msg="订单上传成功！";
        }
        map.put("msg",msg);
        return ResponseEntity.ok(map);
    }

}
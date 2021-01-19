package net.xiaoxiangshop.api.controller.shop;


import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import net.xiaoxiangshop.entity.PaymentTransaction;
import net.xiaoxiangshop.service.OrderService;
import net.xiaoxiangshop.service.PaymentTransactionService;
import net.xiaoxiangshop.service.StatisticService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统计分析 - 接口类
 */
@RestController("statisticsAPIController")
@RequestMapping("/api/statistics")
public class StatisticsAPIController {

    private static final Logger _logger = LoggerFactory.getLogger(StatisticsAPIController.class);

    @Inject
    private StatisticService statisticService;

    @Inject
    private OrderService orderService;
    /**
     * 每日资金统计
     */
    @ResponseBody
    @RequestMapping("/daily_finance")
    public void daily_finance(HttpServletRequest request, HttpServletResponse response) {
        try {
            statisticService.dailyFinanceStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


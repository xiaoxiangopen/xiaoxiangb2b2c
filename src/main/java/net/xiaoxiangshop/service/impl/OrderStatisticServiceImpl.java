package net.xiaoxiangshop.service.impl;

import net.xiaoxiangshop.dao.*;
import net.xiaoxiangshop.entity.Order;
import net.xiaoxiangshop.entity.OrderStatistic;
import net.xiaoxiangshop.entity.Statistic;
import net.xiaoxiangshop.entity.Store;
import net.xiaoxiangshop.service.OrderStatisticService;
import net.xiaoxiangshop.service.StatisticService;
import net.xiaoxiangshop.util.DatesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Service - 统计
 */
@Service
public class OrderStatisticServiceImpl extends BaseServiceImpl<OrderStatistic> implements OrderStatisticService {

    @Inject
    private OrderStatisticDao orderStatisticDao;

    @Override
    @Transactional(readOnly = true)
    public Map<String,Object> analyze(Store store, Date beginDate, Date endDate) {
        Map<String,Object> reslut=new HashMap<>();
        List<String> xAxisDatas=new ArrayList<>();
        List<Long> orderNums=new ArrayList<>();
        List<BigDecimal> totalOrderAmounts=new ArrayList<>();
        List<BigDecimal> averageOrderAmounts=new ArrayList<>();

        List<OrderStatistic> orderStatistics=orderStatisticDao.findList(store,DatesUtil.getStringDate(beginDate,"yyyy-MM-dd"),DatesUtil.getStringDate(endDate,"yyyy-MM-dd"));
        if(orderStatistics!=null){
            for(OrderStatistic orderStatistic:orderStatistics){
                orderNums.add(orderStatistic.getOrderNum());
                totalOrderAmounts.add(orderStatistic.getTotalOrderAmount());
                xAxisDatas.add(DatesUtil.getStringDate(orderStatistic.getStatisticDate(),"yyyy-MM-dd"));
                averageOrderAmounts.add(this.averageAmount(orderStatistic.getOrderNum(),orderStatistic.getTotalOrderAmount()));
            }
        }
        reslut.put("xAxisDatas",xAxisDatas);
        reslut.put("orderNums",orderNums);
        reslut.put("totalOrderAmounts",totalOrderAmounts);
        reslut.put("averageOrderAmounts",averageOrderAmounts);
        return reslut;
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String,Object> orderSourceDataAnalyze(Store store,Integer source, Date beginDate, Date endDate) {
        Map<String,Object> reslut=new HashMap<>();
        List<String> xAxisDatas=new ArrayList<>();
        List<Long> orderNums=new ArrayList<>();
        List<BigDecimal> totalOrderAmounts=new ArrayList<>();
        List<BigDecimal> averageOrderAmounts=new ArrayList<>();


        List<OrderStatistic> orderStatistics=orderStatisticDao.findList(store,DatesUtil.getStringDate(beginDate,"yyyy-MM-dd"),DatesUtil.getStringDate(endDate,"yyyy-MM-dd"));
        if(orderStatistics!=null){
            for(OrderStatistic orderStatistic:orderStatistics){
                xAxisDatas.add(DatesUtil.getStringDate(orderStatistic.getStatisticDate(),"yyyy-MM-dd"));

                /**总订单书及金额*/
                if(source==0) {
                    orderNums.add(orderStatistic.getOrderNum());
                    totalOrderAmounts.add(orderStatistic.getTotalOrderAmount());
                    averageOrderAmounts.add(this.averageAmount(orderStatistic.getOrderNum(), orderStatistic.getTotalOrderAmount()));
                }
                /**pc订单书及金额*/
                if(source==1){
                    orderNums.add(orderStatistic.getPcOPrderNum());
                    totalOrderAmounts.add(orderStatistic.getPcOrderAmount());
                    averageOrderAmounts.add(this.averageAmount(orderStatistic.getPcOPrderNum(),orderStatistic.getPcOrderAmount()));
                }

                /**app订单书及金额*/
                if(source==2) {
                    orderNums.add(orderStatistic.getAppOrderNum());
                    totalOrderAmounts.add(orderStatistic.getAppOrderAmount());
                    averageOrderAmounts.add(this.averageAmount(orderStatistic.getAppOrderNum(), orderStatistic.getAppOrderAmount()));
                }
                /**小程序订单书及金额*/
                if(source==3) {
                    orderNums.add(orderStatistic.getAppletsOrderNum());
                    totalOrderAmounts.add(orderStatistic.getAppletsOrderAmount());
                    averageOrderAmounts.add(this.averageAmount(orderStatistic.getAppletsOrderNum(), orderStatistic.getAppletsOrderAmount()));
                }
            }
        }
        reslut.put("xAxisDatas",xAxisDatas);
        reslut.put("orderNums",orderNums);
        reslut.put("totalOrderAmounts",totalOrderAmounts);
        reslut.put("averageOrderAmounts",averageOrderAmounts);
        return reslut;
    }

    public BigDecimal averageAmount(Long num,BigDecimal amount){
        DecimalFormat   df   =new DecimalFormat("#.00");
        BigDecimal averageAmount=new BigDecimal("0");
        if(num!=null&&num!=0){
            String amountStr=df.format(amount.divide(new BigDecimal(num)));
            averageAmount=new BigDecimal(amountStr);
        }
        return averageAmount;
    }
}
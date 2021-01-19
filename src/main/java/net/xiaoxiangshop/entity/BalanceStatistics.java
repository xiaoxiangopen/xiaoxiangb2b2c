package net.xiaoxiangshop.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
public class BalanceStatistics {

    private Date createTime;
    private BigDecimal beginning;
    private BigDecimal ending;
    private BigDecimal recharge;
    private BigDecimal consumption;
    private BigDecimal adjustIn;
    private BigDecimal adjustOut;
    private Date statisticsDate;
    private BigDecimal returnMoney;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getBeginning() {
        return beginning;
    }

    public void setBeginning(BigDecimal beginning) {
        this.beginning = beginning;
    }

    public BigDecimal getEnding() {
        return ending;
    }

    public void setEnding(BigDecimal ending) {
        this.ending = ending;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public BigDecimal getAdjustIn() {
        return adjustIn;
    }

    public void setAdjustIn(BigDecimal adjustIn) {
        this.adjustIn = adjustIn;
    }

    public BigDecimal getAdjustOut() {
        return adjustOut;
    }

    public void setAdjustOut(BigDecimal adjustOut) {
        this.adjustOut = adjustOut;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public BigDecimal getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(BigDecimal returnMoney) {
        this.returnMoney = returnMoney;
    }
}

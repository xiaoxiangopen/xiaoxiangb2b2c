/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity.api.order;
import java.util.Date;

/**
 * Auto-generated: 2020-09-17 15:20:15
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Payment {

    private String paymentcode;
    private String amount;
    private String avalues;
    private String balance;
    private String reqcode;
    private String tradecode;
    private String paytime;
    private String lsh;
    private String bank_type;
    private String trade_channel;
    public void setPaymentcode(String paymentcode) {
        this.paymentcode = paymentcode;
    }
    public String getPaymentcode() {
        return paymentcode;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAmount() {
        return amount;
    }

    public void setAvalues(String avalues) {
        this.avalues = avalues;
    }
    public String getAvalues() {
        return avalues;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getBalance() {
        return balance;
    }

    public void setReqcode(String reqcode) {
        this.reqcode = reqcode;
    }
    public String getReqcode() {
        return reqcode;
    }

    public void setTradecode(String tradecode) {
        this.tradecode = tradecode;
    }
    public String getTradecode() {
        return tradecode;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }
    public String getLsh() {
        return lsh;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }
    public String getBank_type() {
        return bank_type;
    }

    public void setTrade_channel(String trade_channel) {
        this.trade_channel = trade_channel;
    }
    public String getTrade_channel() {
        return trade_channel;
    }

}
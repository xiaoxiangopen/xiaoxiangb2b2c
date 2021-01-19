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
public class Head {

    private String billkey;
    private String billid;
    private String billdate;
    private String recaddr;
    private String recuser;
    private String rectel;
    private int deliveryfee;
    private String hykh;

    public void setBillkey(String billkey) {
        this.billkey = billkey;
    }

    public String getBillkey() {
        return billkey;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getBillid() {
        return billid;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public void setRecaddr(String recaddr) {
        this.recaddr = recaddr;
    }

    public String getRecaddr() {
        return recaddr;
    }

    public void setRecuser(String recuser) {
        this.recuser = recuser;
    }

    public String getRecuser() {
        return recuser;
    }

    public void setRectel(String rectel) {
        this.rectel = rectel;
    }

    public String getRectel() {
        return rectel;
    }

    public void setDeliveryfee(int deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public int getDeliveryfee() {
        return deliveryfee;
    }

    public void setHykh(String hykh) {
        this.hykh = hykh;
    }

    public String getHykh() {
        return hykh;
    }

}
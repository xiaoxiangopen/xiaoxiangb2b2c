package net.xiaoxiangshop.entity;

import java.util.List;

public class CardpayRequestBean {
    private String method;
    private String type;
    private String cardid;
    private String orgid;
    private String payje;



    public String getPayje() {
        return payje;
    }

    public void setPayje(String payje) {
        this.payje = payje;
    }

    public String getBillkey() {
        return billkey;
    }

    public void setBillkey(String billkey) {
        this.billkey = billkey;
    }

    private String billkey;

    private List<Salelist> salelist;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public List<Salelist> getSalelist() {
        return salelist;
    }

    public void setSalelist(List<Salelist> salelist) {
        this.salelist = salelist;
    }
}

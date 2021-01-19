/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity.api.order;

/**
 * Auto-generated: 2020-09-17 15:20:15
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class OrderRequestBean {

    private String method;
    private String orgid;
    private OrderData data;
    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return method;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }
    public String getOrgid() {
        return orgid;
    }

    public void setData(OrderData data) {
        this.data = data;
    }
    public OrderData getData() {
        return data;
    }

}
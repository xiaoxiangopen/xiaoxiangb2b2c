/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity.api.orderUpload;

/**
 * Auto-generated: 2020-10-13 19:56:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class OrderUploadRequestBean {

    private String method;
    private String orgid;
    private DataRequestOrderUpload data;
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

    public void setData(DataRequestOrderUpload data) {
        this.data = data;
    }
    public DataRequestOrderUpload getData() {
        return data;
    }

}
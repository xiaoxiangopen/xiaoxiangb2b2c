/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity;
import java.util.List;

/**
 * Auto-generated: 2020-09-15 22:2:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SkuRequestBean {

    private String method;
    private String orgid;
    private List<String> productbarcodes;
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

    public void setProductbarcodes(List<String> productbarcodes) {
        this.productbarcodes = productbarcodes;
    }
    public List<String> getProductbarcodes() {
        return productbarcodes;
    }

}
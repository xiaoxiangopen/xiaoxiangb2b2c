/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity.api.orderUpload;
import java.util.Date;

/**
 * Auto-generated: 2020-10-13 19:56:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class HeadRequestOrderUpload {

    private String billid;
    private String  billdate;
    private String hykh;
    public void setBillid(String billid) {
        this.billid = billid;
    }
    public String getBillid() {
        return billid;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }
    public String getBilldate() {
        return billdate;
    }

    public void setHykh(String hykh) {
        this.hykh = hykh;
    }
    public String getHykh() {
        return hykh;
    }

}
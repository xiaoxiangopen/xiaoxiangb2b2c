/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity.api.sku;
import java.util.List;

/**
 * Auto-generated: 2020-09-16 11:57:27
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SkuResponseBean {

    private int code;
    private String msg;
    private int sub_code;
    private String sub_msg;
    private int recordcount;
    private List<Data> data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setSub_code(int sub_code) {
        this.sub_code = sub_code;
    }
    public int getSub_code() {
        return sub_code;
    }

    public void setSub_msg(String sub_msg) {
        this.sub_msg = sub_msg;
    }
    public String getSub_msg() {
        return sub_msg;
    }

    public void setRecordcount(int recordcount) {
        this.recordcount = recordcount;
    }
    public int getRecordcount() {
        return recordcount;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

}
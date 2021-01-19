/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity.api.orderUpload;
import java.util.List;

/**
 * Auto-generated: 2020-10-13 19:56:26
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class DataRequestOrderUpload {

    private HeadRequestOrderUpload head;
    private List<ProductsRequestOrderUpload> products;
    public void setHead(HeadRequestOrderUpload head) {
        this.head = head;
    }
    public HeadRequestOrderUpload getHead() {
        return head;
    }

    public void setProducts(List<ProductsRequestOrderUpload> products) {
        this.products = products;
    }
    public List<ProductsRequestOrderUpload> getProducts() {
        return products;
    }

}
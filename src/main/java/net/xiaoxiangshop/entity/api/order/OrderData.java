/**
 * Copyright 2020 bejson.com
 */
package net.xiaoxiangshop.entity.api.order;
import java.util.List;

/**
 * Auto-generated: 2020-09-17 15:20:15
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class OrderData {

    private Head head;
    private List<Products> products;
    private List<Payment> payment;
    public void setHead(Head head) {
        this.head = head;
    }
    public Head getHead() {
        return head;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
    public List<Products> getProducts() {
        return products;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }
    public List<Payment> getPayment() {
        return payment;
    }

}
package net.xiaoxiangshop.service;

import net.xiaoxiangshop.entity.api.orderUpload.ProductsRequestOrderUpload;

import java.util.HashMap;
import java.util.List;

/**
 * Service - 订单项
 * 
 */
public interface ErpSyncService  {

    HashMap uploadOrder(String orderSn);


}
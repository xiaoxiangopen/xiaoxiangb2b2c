package net.xiaoxiangshop.service;


import net.xiaoxiangshop.entity.ErpResult;

import java.util.HashMap;

/**
 * Service - ERP返回结果
 */
public interface ErpResultService extends BaseService<ErpResult> {

    //保存数据
    void add(HashMap map);

}
package net.xiaoxiangshop.dao;

import net.xiaoxiangshop.entity.Ad;

import java.util.HashMap;
import java.util.Map;

/**
 * Dao - 短信
 * 
 */
public interface SmsDao{
    /**
     * 短信发送记录
     * @param smsInfo
     */
    void insetSmsInfo(Map<String, Object> smsInfo);
}
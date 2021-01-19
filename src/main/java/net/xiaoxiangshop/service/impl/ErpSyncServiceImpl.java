package net.xiaoxiangshop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xiaoxiangshop.ErpInterfaceMethod;
import net.xiaoxiangshop.dao.OrderPaymentDao;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.entity.api.order.*;
import net.xiaoxiangshop.entity.api.orderUpload.*;
import net.xiaoxiangshop.service.*;
import net.xiaoxiangshop.util.WebUtils;
import net.xiaoxiangshop.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service - ERP接口同步
 */
@Service
public class ErpSyncServiceImpl implements ErpSyncService {


    @Override
    public HashMap uploadOrder(String orderSn) {
        HashMap map = new HashMap();
        return  map;
    }

}
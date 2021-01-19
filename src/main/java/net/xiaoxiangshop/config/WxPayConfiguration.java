package net.xiaoxiangshop.config;

import javax.inject.Inject;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

import net.xiaoxiangshop.Setting;
import net.xiaoxiangshop.plugin.PaymentPlugin;
import net.xiaoxiangshop.service.PluginService;
import net.xiaoxiangshop.util.SystemUtils;

@Configuration
@ConditionalOnClass(WxPayService.class)
public class WxPayConfiguration {

    // 小程序支付插件Id
    public static final String paymentPluginId = "weixinMiniPaymentPlugin";

    @Inject
    private PluginService pluginService;

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        WxPayService wxPayService = new WxPayServiceImpl();
        if (paymentPlugin != null) {
        	 WxPayConfig payConfig = new WxPayConfig();
             payConfig.setAppId(paymentPlugin.getAttribute("appId")); // 公众号appid
             payConfig.setMchId(paymentPlugin.getAttribute("mchId")); // 商户号
             payConfig.setMchKey(paymentPlugin.getAttribute("apiKey")); // 商户密钥

             Setting setting = SystemUtils.getSetting();
             payConfig.setNotifyUrl(setting.getSiteUrl() + "/api/payment/pay_notify");
             wxPayService.setConfig(payConfig);
        }
        return wxPayService;
    }

}


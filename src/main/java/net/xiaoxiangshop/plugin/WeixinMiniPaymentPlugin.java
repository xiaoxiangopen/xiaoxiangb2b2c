package net.xiaoxiangshop.plugin;

import net.xiaoxiangshop.entity.PaymentTransaction;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Plugin - 微信支付(小程序支付)
 *
 */
@Component("weixinMiniPaymentPlugin")
public class WeixinMiniPaymentPlugin extends PaymentPlugin {
    @Override
    public String getName() {
        return "微信支付(小程序支付)|0302";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getAuthor() {
        return "xiaoxiangAI";
    }

    @Override
    public String getSiteUrl() {
        return "http://www.xiaoxiangshop.net";
    }

    @Override
    public String getInstallUrl() {
        return "/admin/plugin/weixin_mini_payment/install";
    }

    @Override
    public String getUninstallUrl() {
        return "/admin/plugin/weixin_mini_payment/uninstall";
    }

    @Override
    public String getSettingUrl() {
        return "/admin/plugin/weixin_mini_payment/setting";
    }

    @Override
    public void payHandle(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public boolean isPaySuccess(PaymentPlugin paymentPlugin, PaymentTransaction paymentTransaction, String paymentDescription, String extra, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return false;
    }
}


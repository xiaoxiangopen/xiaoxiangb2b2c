package net.xiaoxiangshop.controller.admin.plugin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.xiaoxiangshop.Results;
import net.xiaoxiangshop.controller.admin.BaseController;
import net.xiaoxiangshop.entity.PluginConfig;
import net.xiaoxiangshop.plugin.PaymentPlugin;
import net.xiaoxiangshop.plugin.WeixinMiniPaymentPlugin;
import net.xiaoxiangshop.service.PluginConfigService;

/**
 * Controller - 微信支付(小程序支付)
 *
 */
@Controller("adminPluginWeixinMiniPaymentController")
@RequestMapping("/admin/plugin/weixin_mini_payment")
public class WeixinMiniPaymentController extends BaseController {

    @Inject
    private WeixinMiniPaymentPlugin weixinMiniPaymentPlugin;
    @Inject
    private PluginConfigService pluginConfigService;

    /**
     * 安装
     */
    @PostMapping("/install")
    public ResponseEntity<?> install() {
        if (!weixinMiniPaymentPlugin.getIsInstalled()) {
            PluginConfig pluginConfig = new PluginConfig();
            pluginConfig.setPluginId(weixinMiniPaymentPlugin.getId());
            pluginConfig.setIsEnabled(false);
            pluginConfig.setAttributes(null);
            pluginConfigService.save(pluginConfig);
        }
        return Results.OK;
    }

    /**
     * 卸载
     */
    @PostMapping("/uninstall")
    public ResponseEntity<?> uninstall() {
        if (weixinMiniPaymentPlugin.getIsInstalled()) {
            pluginConfigService.deleteByPluginId(weixinMiniPaymentPlugin.getId());
        }
        return Results.OK;
    }

    /**
     * 设置
     */
    @GetMapping("/setting")
    public String setting(ModelMap model) {
        PluginConfig pluginConfig = weixinMiniPaymentPlugin.getPluginConfig();
        model.addAttribute("feeTypes", PaymentPlugin.FeeType.values());
        model.addAttribute("pluginConfig", pluginConfig);
        return "/admin/plugin/weixin_mini_payment/setting";
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(String displayName, String appId, String appSecret, String mchId, String apiKey, PaymentPlugin.FeeType feeType, BigDecimal fee, String logo, String description, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order) {
        PluginConfig pluginConfig = weixinMiniPaymentPlugin.getPluginConfig();
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PaymentPlugin.DISPLAY_NAME_ATTRIBUTE_NAME, displayName);
        attributes.put("appId", appId);
        attributes.put("appSecret", appSecret);
        attributes.put("mchId", mchId);
        attributes.put("apiKey", apiKey);
        attributes.put(PaymentPlugin.FEE_TYPE_ATTRIBUTE_NAME, String.valueOf(feeType));
        attributes.put(PaymentPlugin.FEE_ATTRIBUTE_NAME, String.valueOf(fee));
        attributes.put(PaymentPlugin.LOGO_ATTRIBUTE_NAME, logo);
        attributes.put(PaymentPlugin.DESCRIPTION_ATTRIBUTE_NAME, description);
        pluginConfig.setAttributes(attributes);
        pluginConfig.setIsEnabled(isEnabled);
        pluginConfig.setOrder(order);
        pluginConfigService.update(pluginConfig);
        return Results.OK;
    }

}


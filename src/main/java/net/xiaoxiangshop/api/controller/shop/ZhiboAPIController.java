package net.xiaoxiangshop.api.controller.shop;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.error.WxErrorException;
import net.xiaoxiangshop.api.controller.member.BaseAPIController;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.ZhiboList;
import net.xiaoxiangshop.entity.ZhiboRequestBean;
import net.xiaoxiangshop.entity.api.orderUpload.ProductsRequestOrderUpload;
import net.xiaoxiangshop.plugin.LoginPlugin;
import net.xiaoxiangshop.service.PluginService;
import net.xiaoxiangshop.service.RedisService;
import net.xiaoxiangshop.util.WebUtils;
import net.xiaoxiangshop.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 直播相关
 * 
 */
@RestController
@RequestMapping("/api/zhibo")
public class ZhiboAPIController extends BaseAPIController {
	private static final Logger _logger = LoggerFactory.getLogger(PhoneAPIController.class);
	private static final String loginPluginId = "weixinMiniLoginPlugin";
	private static final String ACCESS_TOKEN_REQUEST_URL = "https://api.weixin.qq.com/wxa/business/getliveinfo?access_token=";
	private final WxMaServiceImpl wxMaService = new WxMaServiceImpl();
	private static final String REDIS_ZHIBO_KEY = "redisZhiboKey";
	private static final int REDIS_ZHIBO_KEY_CODE = 300;
	private LoginPlugin loginPlugin;
	@Inject
	private PluginService pluginService;
	@Inject
	private RedisService redisService;


	@ModelAttribute
	public void populateModel() {
		loginPlugin = pluginService.getLoginPlugin(loginPluginId);
		WxMaInMemoryConfig wxMaInMemoryConfig = new WxMaInMemoryConfig();
		wxMaInMemoryConfig.setAppid(loginPlugin.getAttribute("appId"));
		wxMaInMemoryConfig.setSecret(loginPlugin.getAttribute("appSecret"));
		wxMaService.setWxMaConfig(wxMaInMemoryConfig);
	}

	@PostMapping("/getZhiboList")
	public ApiResult signIn(String start ,String limit) {
		String  url= null;
		try {
			url = ACCESS_TOKEN_REQUEST_URL+wxMaService.getAccessToken();
		} catch (WxErrorException e) {
			_logger.error("access_token获取失败.");
			e.printStackTrace();
		}
		Map<String, Object> data = new HashMap<>();
		ZhiboRequestBean zhiboRequestBean=new ZhiboRequestBean();
		zhiboRequestBean.setStart(start);
		zhiboRequestBean.setLimit(limit);
		List<ZhiboList> zhiboLists = new ArrayList<>();
		try {
			if(redisService.get(REDIS_ZHIBO_KEY)!=null){
				zhiboLists= (List<ZhiboList>) redisService.get(REDIS_ZHIBO_KEY);
			}else{
				String retV = WebUtils.sendPost(url,  JSONObject.toJSONString(zhiboRequestBean));
				JSONObject obj = JSONObject.parseObject(retV);
				String errcode = obj.getString("errcode");
				_logger.error("获取到直播列表："+retV);
				if("0".equals(errcode)){
					zhiboLists = JSON.parseArray(obj.getString("room_info"), ZhiboList.class);
					for (ZhiboList zb:zhiboLists){
						zb.setStart_time(XmlUtils.secondToTime(zb.getStart_time()));
						zb.setEnd_time(XmlUtils.secondToTime(zb.getEnd_time()));
					}
					redisService.set(REDIS_ZHIBO_KEY,zhiboLists,REDIS_ZHIBO_KEY_CODE);
				}
			}
		} catch (IOException e) {
			_logger.error("获取到直播列表失败.");
			e.printStackTrace();
		}
		data.put("list",zhiboLists);
		return ResultUtils.ok(data);
	}
}
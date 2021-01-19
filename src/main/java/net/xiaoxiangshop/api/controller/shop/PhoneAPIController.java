package net.xiaoxiangshop.api.controller.shop;
import com.alibaba.fastjson.JSONObject;
import net.xiaoxiangshop.api.controller.member.BaseAPIController;
import net.xiaoxiangshop.api.model.ApiResult;
import net.xiaoxiangshop.api.util.ResultUtils;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

/**
 * Controller - 获取手机号
 * 
 */
@RestController
@RequestMapping("/api/social_phone")
public class PhoneAPIController extends BaseAPIController {
	private static final Logger _logger = LoggerFactory.getLogger(PhoneAPIController.class);
	private static final String REDIS_UUID_KEY = "UUID";
	/**
	 * 小程序登录id
	 */
	private static final String loginPluginId = "weixinMiniLoginPlugin";
	@Inject
	private MemberService memberService;

	@PostMapping("/getPhone")
	public ApiResult getPhone(String encryptedData, String iv,String session_key,String userId) {

		String  phone=	getPhoneNumber(encryptedData,session_key,iv);
		Member member = memberService.find(Long.valueOf(userId));
		if(phone!=null){
			member.setMobile(phone);
			memberService.updateMembers(member);
		}
		Map<String, Object> data = new HashMap<>();
		return ResultUtils.ok(data);

	}
	public  String getPhoneNumber(String encryptedData, String session_key, String iv)  {
		// 被加密的数据
		byte[] dataByte = Base64.getDecoder().decode(encryptedData);
		// 加密秘钥
		byte[] keyByte = Base64.getDecoder().decode(session_key);
		// 偏移量
		byte[] ivByte = Base64.getDecoder().decode(iv);
		try {
			// 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
			int base = 16;
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			}
			// 初始化
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				JSONObject jsonObject=JSONObject.parseObject(result);
				_logger.info("获取到手机号信息："+jsonObject);
				return jsonObject.getString("phoneNumber");
			}
		} catch (Exception e) {
			e.printStackTrace();
			_logger.info("获取手机号失败");
		}
		return null;
	}

}
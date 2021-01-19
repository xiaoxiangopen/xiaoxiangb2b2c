package net.xiaoxiangshop.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.xiaoxiangshop.ApplicationB2B2C;
import net.xiaoxiangshop.entity.Store;
import org.springframework.boot.SpringApplication;
import org.springframework.util.Assert;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import sun.applet.Main;

import javax.servlet.http.HttpServletRequest;

/**
 * Utils - XML
 * 
 */
public final class XmlUtils {

	//数据来源，
	// 1、pc,
	// 2、APP,
	// 3、小程序
	public static   final  Long   DATA_SOURCE_ONE=1L;
	public static   final  Long   DATA_SOURCE_TWO=2L;
	public static   final  Long   DATA_SOURCE_THREE=3L;

    //微信用户特殊符号处理
    private static String special_symbols="[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[^\\\\u0000-\\\\uFFFF]";

	//商城电子卷
	public  static String MALL_E_COUPONS="商城电子卷|0217";

	/**
	 * XmlMapper
	 */
	private static final XmlMapper XML_MAPPER;

	static {
		XML_MAPPER = new XmlMapper();
		XML_MAPPER.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
		XML_MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	}

	/**
	 * 不可实例化
	 */
	private XmlUtils() {
	}

	/**
	 * 将对象转换为XML字符串
	 * 
	 * @param value
	 *            对象
	 * @return XML字符串
	 */
	public static String toXml(Object value) {
		Assert.notNull(value, "[Assertion failed] - value is required; it must not be null");

		try {
			JacksonXmlModule module = new JacksonXmlModule();
			module.setDefaultUseWrapper(true);
			XmlMapper xmlMapper = new XmlMapper(module);
			return xmlMapper.writer().withRootName("xml").writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将XML字符串转换为对象
	 * 
	 * @param xml
	 *            XML字符串
	 * @param valueType
	 *            类型
	 * @return 对象
	 */
	public static <T> T toObject(String xml, Class<T> valueType) {
		Assert.hasText(xml, "[Assertion failed] - xml must have text; it must not be null, empty, or blank");
		Assert.notNull(valueType, "[Assertion failed] - valueType is required; it must not be null");

		try {
			return XML_MAPPER.readValue(xml, valueType);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将XML字符串转换为对象
	 * 
	 * @param xml
	 *            XML字符串
	 * @param typeReference
	 *            类型
	 * @return 对象
	 */
	public static <T> T toObject(String xml, TypeReference<?> typeReference) {
		Assert.hasText(xml, "[Assertion failed] - xml must have text; it must not be null, empty, or blank");
		Assert.notNull(typeReference, "[Assertion failed] - typeReference is required; it must not be null");

		try {
			return XML_MAPPER.readValue(xml, typeReference);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将XML字符串转换为对象
	 * 
	 * @param xml
	 *            XML字符串
	 * @param javaType
	 *            类型
	 * @return 对象
	 */
	public static <T> T toObject(String xml, JavaType javaType) {
		Assert.hasText(xml, "[Assertion failed] - xml must have text; it must not be null, empty, or blank");
		Assert.notNull(javaType, "[Assertion failed] - javaType is required; it must not be null");

		try {
			return XML_MAPPER.readValue(xml, javaType);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将XML字符串转换为树
	 * 
	 * @param xml
	 *            XML字符串
	 * @return 树
	 */
	public static JsonNode toTree(String xml) {
		Assert.hasText(xml, "[Assertion failed] - xml must have text; it must not be null, empty, or blank");

		try {
			return XML_MAPPER.readTree(xml);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将对象转换为XML流
	 * 
	 * @param writer
	 *            Writer
	 * @param value
	 *            对象
	 */
	public static void writeValue(Writer writer, Object value) {
		Assert.notNull(writer, "[Assertion failed] - writer is required; it must not be null");
		Assert.notNull(value, "[Assertion failed] - value is required; it must not be null");

		try {
			XML_MAPPER.writeValue(writer, value);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 构造类型
	 * 
	 * @param type
	 *            类型
	 * @return 类型
	 */
	public static JavaType constructType(Type type) {
		Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

		return TypeFactory.defaultInstance().constructType(type);
	}

	/**
	 * 构造类型
	 * 
	 * @param typeReference
	 *            类型
	 * @return 类型
	 */
	public static JavaType constructType(TypeReference<?> typeReference) {
		Assert.notNull(typeReference, "[Assertion failed] - typeReference is required; it must not be null");

		return TypeFactory.defaultInstance().constructType(typeReference);
	}

	//生存两位随机数
	public static String getRandom(int len) {
		int rs = (int) ((Math.random() * 9 + 1) * Math.pow(10, len - 1));
		return String.valueOf(rs);
	}

	/**
	 * 判断是否存在特殊字符串
	 */
	public static boolean hasEmoji(String content) {
		Pattern pattern = Pattern.compile(special_symbols);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 替换字符串中的emoji字符
	 */
	public static String replaceEmoji(String str) {
		if (!hasEmoji(str)) {
			return str;
		} else {
			str = str.replaceAll(special_symbols, "");
			return str;
		}
	}
	/**
	 * 将距离1970年的数字时间转换成正常的字符串格式时间；
	 * 比如数字时间："1513345743"
	 * 转换后："2017-12-15 21:49:03"
	 *
	 * @param time
	 * @return
	 */
	public static String secondToTime(String time) {
		String dateStr = "1970-1-1 08:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (time.equals("0")) {
			return "";
		}
		Date miDate;
		String returnstr = "";
		try {
			miDate = sdf.parse(dateStr);
			Object t1 = miDate.getTime();
			long h1 = Long.parseLong(time.toString()) * 1000 + Long.parseLong(t1.toString());
			returnstr = sdf.format(h1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnstr;
	}


	//获取IP地址
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;

	}



	//写日志到txt
	public static void appendMethodC(String content, String erp_type) {
		try {
			String fileName = "";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String date = df.format(new Date());
			fileName = "/www/upload/txt/upload/" + erp_type + "_" + date + ".txt";
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//取出map最大key
	public static Object getMaxKey(Map<BigDecimal, Store> map) {
		if(map==null)return null;
		Set<BigDecimal> set= map.keySet();
		Object[] obj=set.toArray();
		Arrays.sort(obj);
		return obj[obj.length-1];
	}

	//订单总号
	public static String  getTnt() {
		return  new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+getRandom(5);
	}


}

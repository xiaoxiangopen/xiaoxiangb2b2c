package net.xiaoxiangshop.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import net.xiaoxiangshop.Setting;

/**
 * Utils - Web
 */
public final class WebUtils {


    /**
     * PoolingHttpClientConnectionManager
     */
    private static final PoolingHttpClientConnectionManager HTTP_CLIENT_CONNECTION_MANAGER;

    /**
     * CloseableHttpClient
     */
    private static final CloseableHttpClient HTTP_CLIENT;

    static {
        HTTP_CLIENT_CONNECTION_MANAGER = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build());
        HTTP_CLIENT_CONNECTION_MANAGER.setDefaultMaxPerRoute(100);
        HTTP_CLIENT_CONNECTION_MANAGER.setMaxTotal(200);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000).setConnectTimeout(60000).setSocketTimeout(60000).build();
        HTTP_CLIENT = HttpClientBuilder.create().setConnectionManager(HTTP_CLIENT_CONNECTION_MANAGER).setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 不可实例化
     */
    private WebUtils() {
    }

    /**
     * 获取HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null && requestAttributes instanceof ServletRequestAttributes ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
    }

    /**
     * 获取HttpServletResponse
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null && requestAttributes instanceof ServletRequestAttributes ? ((ServletRequestAttributes) requestAttributes).getResponse() : null;
    }

    /**
     * 判断是否为AJAX请求
     *
     * @param request HttpServletRequest
     * @return 是否为AJAX请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");

        return StringUtils.equalsIgnoreCase(request.getHeader("X-Requested-With"), "XMLHttpRequest");
    }

    /**
     * 添加cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     Cookie名称
     * @param value    Cookie值
     * @param maxAge   有效期(单位: 秒)
     * @param path     路径
     * @param domain   域
     * @param secure   是否启用加密
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Integer maxAge, String path, String domain, Boolean secure) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");
        Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
        Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");
        Assert.hasText(value, "[Assertion failed] - value must have text; it must not be null, empty, or blank");

        try {
            name = URLEncoder.encode(name, "UTF-8");
            value = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(name, value);
            if (maxAge != null) {
                cookie.setMaxAge(maxAge);
            }
            if (StringUtils.isNotEmpty(path)) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            if (secure != null) {
                cookie.setSecure(secure);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 添加cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     Cookie名称
     * @param value    Cookie值
     * @param maxAge   有效期(单位: 秒)
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Integer maxAge) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");
        Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
        Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");
        Assert.hasText(value, "[Assertion failed] - value must have text; it must not be null, empty, or blank");

        Setting setting = SystemUtils.getSetting();
        addCookie(request, response, name, value, maxAge, setting.getCookiePath(), setting.getCookieDomain(), null);
    }

    /**
     * 添加cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     Cookie名称
     * @param value    Cookie值
     */
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");
        Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
        Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");
        Assert.hasText(value, "[Assertion failed] - value must have text; it must not be null, empty, or blank");

        Setting setting = SystemUtils.getSetting();
        addCookie(request, response, name, value, null, setting.getCookiePath(), setting.getCookieDomain(), null);
    }

    /**
     * 获取cookie
     *
     * @param request HttpServletRequest
     * @param name    Cookie名称
     * @return Cookie值，若不存在则返回null
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");
        Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
                for (Cookie cookie : cookies) {
                    if (StringUtils.equals(name, cookie.getName())) {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 移除cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     Cookie名称
     * @param path     路径
     * @param domain   域
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name, String path, String domain) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");
        Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
        Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");

        try {
            name = URLEncoder.encode(name, "UTF-8");
            Cookie cookie = new Cookie(name, null);
            cookie.setMaxAge(0);
            if (StringUtils.isNotEmpty(path)) {
                cookie.setPath(path);
            }
            if (StringUtils.isNotEmpty(domain)) {
                cookie.setDomain(domain);
            }
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 移除cookie
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param name     Cookie名称
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");
        Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
        Assert.hasText(name, "[Assertion failed] - name must have text; it must not be null, empty, or blank");

        Setting setting = SystemUtils.getSetting();
        removeCookie(request, response, name, setting.getCookiePath(), setting.getCookieDomain());
    }

    /**
     * 参数解析
     *
     * @param query    查询字符串
     * @param encoding 编码格式
     * @return 参数
     */
    public static Map<String, String> parse(String query, String encoding) {
        Assert.hasText(query, "[Assertion failed] - query must have text; it must not be null, empty, or blank");

        Charset charset;
        if (StringUtils.isNotEmpty(encoding)) {
            charset = Charset.forName(encoding);
        } else {
            charset = Charset.forName("UTF-8");
        }
        List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(query, charset);
        Map<String, String> parameterMap = new HashMap<>();
        for (NameValuePair nameValuePair : nameValuePairs) {
            parameterMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        return parameterMap;
    }

    /**
     * 解析参数
     *
     * @param query 查询字符串
     * @return 参数
     */
    public static Map<String, String> parse(String query) {
        Assert.hasText(query, "[Assertion failed] - query must have text; it must not be null, empty, or blank");

        return parse(query, null);
    }

    /**
     * 重定向
     *
     * @param request          HttpServletRequest
     * @param response         HttpServletResponse
     * @param url              URL
     * @param contextRelative  是否相对上下文路径
     * @param http10Compatible 是否兼容HTTP1.0
     */
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url, boolean contextRelative, boolean http10Compatible) {
        Assert.notNull(request, "[Assertion failed] - request is required; it must not be null");
        Assert.notNull(response, "[Assertion failed] - response is required; it must not be null");
        Assert.hasText(url, "[Assertion failed] - url must have text; it must not be null, empty, or blank");

        StringBuilder targetUrl = new StringBuilder();
        if (contextRelative && url.startsWith("/")) {
            targetUrl.append(request.getContextPath());
        }
        targetUrl.append(url);
        String encodedRedirectURL = response.encodeRedirectURL(String.valueOf(targetUrl));
        if (http10Compatible) {
            try {
                response.sendRedirect(encodedRedirectURL);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            response.setStatus(303);
            response.setHeader("Location", encodedRedirectURL);
        }
    }

    /**
     * 重定向
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param url      URL
     */
    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) {
        sendRedirect(request, response, url, true, true);
    }

    /**
     * POST请求
     *
     * @param url URL
     * @param xml XML
     * @return 返回结果
     */
    public static String post(String url, String xml) {
        Assert.hasText(url, "[Assertion failed] - url must have text; it must not be null, empty, or blank");

        return post(url, null, new StringEntity(xml, "UTF-8"));
    }

    /**
     * POST请求
     *
     * @param url          URL
     * @param parameterMap 请求参数
     * @return 返回结果
     */
    public static String post(String url, Map<String, Object> parameterMap) {
        Assert.hasText(url, "[Assertion failed] - url must have text; it must not be null, empty, or blank");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (parameterMap != null) {
                for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
                    String name = entry.getKey();
                    String value = ConvertUtils.convert(entry.getValue());
                    if (StringUtils.isNotEmpty(name)) {
                        nameValuePairs.add(new BasicNameValuePair(name, value));
                    }
                }
            }
            return post(url, null, new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * POST请求
     *
     * @param url    URL
     * @param header Header
     * @param entity HttpEntity
     * @return 返回结果
     */
    public static String post(String url, Header header, HttpEntity entity) {
        return post(url, header, entity, String.class);
    }

    /**
     * POST请求
     *
     * @param url        URL
     * @param header     Header
     * @param entity     HttpEntity
     * @param resultType 返回结果类型
     * @return 返回结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T post(String url, Header header, HttpEntity entity, Class<T> resultType) {
        Assert.hasText(url, "[Assertion failed] - url must have text; it must not be null, empty, or blank");
        Assert.notNull(resultType, "[Assertion failed] - resultType is required; it must not be null");

        try {
            HttpPost httpPost = new HttpPost(url);
            if (header != null) {
                httpPost.setHeader(header);
            }
            if (entity != null) {
                httpPost.setEntity(entity);
            }
            //CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
            HttpEntity httpEntity = null;
            try (CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost)) {
                httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    if (String.class.isAssignableFrom(resultType)) {
                        return (T) EntityUtils.toString(httpEntity, "UTF-8");
                    } else if (resultType.isArray() && byte.class.isAssignableFrom(resultType.getComponentType())) {
                        return (T) EntityUtils.toByteArray(httpEntity);
                    }
                }
            } finally {
                EntityUtils.consume(httpEntity);
                //IOUtils.closeQuietly(httpResponse);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    /**
     * GET请求
     *
     * @param url          URL
     * @param parameterMap 请求参数
     * @return 返回结果
     */
    public static String get(String url, Map<String, Object> parameterMap) {
        Assert.hasText(url, "[Assertion failed] - url must have text; it must not be null, empty, or blank");

        String result = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (parameterMap != null) {
                for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
                    String name = entry.getKey();
                    String value = ConvertUtils.convert(entry.getValue());
                    if (StringUtils.isNotEmpty(name)) {
                        nameValuePairs.add(new BasicNameValuePair(name, value));
                    }
                }
            }
            HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
            //CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpGet);
            try (CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpGet)) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity);
                    EntityUtils.consume(httpEntity);
                }
            }
//			} finally {
//				IOUtils.closeQuietly(httpResponse);
//			}
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }


//	public static String sendPost(String url, String param) {
//		PrintWriter out = null;
//		BufferedReader in = null;
//		String result = "";
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			URLConnection conn = realUrl.openConnection();
//			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
////			conn.setRequestProperty("Cookie", "cookie的参数");
//			conn.setRequestProperty("user-agent",
//					"Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			// 获取URLConnection对象对应的输出流
//			out = new PrintWriter(conn.getOutputStream());
//			// 发送请求参数
//			out.print(param);
//			// flush输出流的缓冲
//			out.flush();
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(
//					new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				System.out.println(line);
//				result += line;
//			}
//		} catch (Exception e) {
//			System.out.println("发送 POST 请求出现异常！"+e);
//			e.printStackTrace();
//		}
//		//使用finally块来关闭输出流、输入流
//		finally{
//			try{
//				if(out!=null){
//					out.close();
//				}
//				if(in!=null){
//					in.close();
//				}
//			}
//			catch(IOException ex){
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
    public static String sendPost(String urlString, String parm) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true); //设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
        connection.setDoInput(true); // 设置连接输入流为true
        connection.setRequestMethod("POST"); // 设置请求方式为post
        connection.setUseCaches(false); // post请求缓存设为false
        connection.setConnectTimeout(3000);
        connection.setInstanceFollowRedirects(true);
        // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
        // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();

        // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
        DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
//        String parm = "storeId=" + URLEncoder.encode("32", "utf-8"); //URLEncoder.encode()方法  为字符串进行编码
        dataout.writeBytes(parm);
        dataout.flush();
        dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
        // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
        BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder(); // 用来存储响应数据

        String retV = "";
        // 循环读取流,若不到结尾处
        while ((line = bf.readLine()) != null) {
            sb.append(line);
            retV+= line;
        }
        bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
        connection.disconnect(); // 销毁连接
        System.out.println(sb.toString());
        return  sb.toString();

    }


//	public static String sendPost(String url, String data) {
//		String response = null;
//
//		try {
//			CloseableHttpClient httpclient = null;
//			CloseableHttpResponse httpresponse = null;
//			try {
//				httpclient = HttpClients.createDefault();
//
//				HttpPost httppost = new HttpPost(url);
//
//				StringEntity stringentity = new StringEntity(data,
//						ContentType.create("text/json", "UTF-8"));
//				httppost.setEntity(stringentity);
//				httppost
//				httpresponse = httpclient.execute(httppost);
//				response = EntityUtils
//						.toString(httpresponse.getEntity());
//			} finally {
//				if (httpclient != null) {
//					httpclient.close();
//				}
//				if (httpresponse != null) {
//					httpresponse.close();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}


}
package com.example.demo.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import sun.misc.BASE64Encoder;


public class UserTokenInterceptor implements HandlerInterceptor {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		String token = request.getHeader("token");
		// token不存在
		String user = null;
		if (token != null) {
//			try {
//				user = JwtToken.unsign(token);
//			} catch (Exception e) {
//				response.setContentType("application/json; charset=utf-8");
//				ObjectMapper mapper = new ObjectMapper();
//				String json = mapper.writeValueAsString(ResponseResult.ERROR.msg("请经过授权访问！"));
//				PrintWriter out = response.getWriter();
//				out.print(json);
//				out.flush();
//				out.close();
//				return false;
//			}
//			if (user != null) {
//				return true;
//			}
		}
//		response.setContentType("application/json; charset=utf-8");
//		response.setHeader("auth", "5fe85d9277805386990856b3ae2e2f0f");
////		response.setHeader("date", new Date());
//		String date = String.valueOf(new Date());
//		response.setHeader("date", date);
		HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://openapi.dragonex.im/api/v1/token/new/");
        
        httpPost.setHeader("content-type","application/json");
        httpPost.setHeader("auth", "5fe85d9277805386990856b3ae2e2f0f");
//		response.setHeader("date", new Date());
		String date = String.valueOf(new Date());
		httpPost.setHeader("date", date);
		String entityStr = null;
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity entity = httpResponse.getEntity();
        entityStr = EntityUtils.toString(entity);
        System.out.println(entityStr);
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(ResponseResult.ERROR.msg("请经过授权访问！"));
//		PrintWriter out = response.getWriter();
//		out.print(json);
//		out.flush();
//		out.close();
		return true;
	}
	
	
//	//get接口掉方法
//    public String callInterface(){
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost("https://openapi.dragonex.im/api/v1/token/new/");
////        httpPost.
////        HttpGet httpGet = new HttpGet("http://localhost:8083/tycj/getHostMenu.shtml");
//
//        String entityStr = null;
//        try {
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            HttpEntity entity = httpResponse.getEntity();
//            entityStr = EntityUtils.toString(entity);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return entityStr;
//    }
	
	
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
	}
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
	}
	
	public static void main(String [] args){
		HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://openapi.dragonex.im/api/v1/token/new/");
        
        httpPost.setHeader("Content-Type","application/json");
        
        httpPost.setHeader("auth", "ThisIsAccessKey:b0e873cd55f5539aab82e97a3ef28cd2");
//		response.setHeader("date", new Date());
//      //设置基础时间为格林威治时间
//        TimeZone gmtTz = TimeZone.getTimeZone("GMT");
////        设置目标时间为中国标准时
//        TimeZone desTz = TimeZone.getTimeZone("Asia/Shanghai");
//        GregorianCalendar rightNow = new GregorianCalendar(gmtTz);
//        Date mydate=rightNow.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'",Locale.ENGLISH);//MMM dd hh:mm:ss Z yyyy
//		String date = dateFormat.format(DateTime.now(DateTimeZone.UTC));
//        DateTime.now(DateTimeZone.UTC);
//        DateTime
//        System.out.println(TimeZone.getTimeZone("GMT"));
//        DateTime d = DateTime.now(DateTimeZone.UTC);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
//        System.out.println(dateFormat.format(new Date()));
		httpPost.setHeader("date", dateFormat.format(new Date()));
//		httpPost.setURI("https://openapi.dragonex.io/api/v1/token/new/");
		
		
		String entityStr = null;
		HttpResponse httpResponse;
		try {
//			String values = encrypt(sb.toString(), "b0e873cd55f5539aab82e97a3ef28cd2");
//			httpPost.setHeader("auth", "ThisIsAccessKey:"+values);
			httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
	        entityStr = EntityUtils.toString(entity);
	        System.out.println(entityStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	 /**
     * Description 根据键值进行加密
     * @param data 
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }
    
    
    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
 
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
 
        return cipher.doFinal(data);
    }
	
}

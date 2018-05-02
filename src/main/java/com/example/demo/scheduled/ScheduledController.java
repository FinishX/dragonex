/**   
* @Title: ScheduledController.java 
* @Package com.example.demo.scheduled 
* @Description:
* @author hjq  
* @date 2018年5月2日 上午11:01:22 
* @version V1.0   
*/
package com.example.demo.scheduled;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.openApi.HostConstant;
import com.example.demo.openApi.HttpParams;
import com.example.demo.openApi.HttpUtils;
import com.example.demo.utils.RedisUtil;

/** 
* @ClassName: ScheduledController 
* @Description:
* @author hjq
* @date 2018年5月2日 上午11:01:22 
*  
*/
@Component
@EnableScheduling
public class ScheduledController {
	
	@Resource
	private RedisUtil redisUtil;

	@Scheduled(cron = "0 40 15 * * ?")
	public void getToken(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String access_key = "853383797d695e67bfcd768e0706e323";
				String secret_key = "c104adfc7e1b5e5e99c7c307ad65524e";
				// get token
				String hasToken = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_TOKEN);
				JSONObject tokenJson = JSONObject.parseObject(hasToken);
				String code = tokenJson.getString("code");
				System.out.println(hasToken);
				if("1".equals(code)){
					String dataJson = tokenJson.getString("data");
					JSONObject tokenJsons = JSONObject.parseObject(dataJson);
					String token = tokenJsons.getString("token");
					redisUtil.set("token", token);
					redisUtil.set("test", "1123123");
//					request.getSession().setAttribute("token", token);
				}
			}
		}).start();
		
	}
	
	
	@Scheduled(cron = "0 0/3 * * * ?")
	public void checkToken(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String access_key = "853383797d695e67bfcd768e0706e323";
				String secret_key = "c104adfc7e1b5e5e99c7c307ad65524e";
				// get token
				String token = redisUtil.get("token");
				HttpParams.setToken(token);
				String hasToken = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.CHECK_TOKEN_STATUS);
				JSONObject tokenJson = JSONObject.parseObject(hasToken);
				String code = tokenJson.getString("code");
				System.out.println(hasToken);
//				System.out.println(uid);
				if(!"1".equals(code)){
					getToken();
				}
			}
		}).start();
		
	}
}

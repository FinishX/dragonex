/**   
* @Title: ScheduledController.java 
* @Package com.example.demo.scheduled 
* @Description:
* @author hjq  
* @date 2018年5月2日 上午11:01:22 
* @version V1.0   
*/
package com.example.demo.scheduled;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.CoinController;
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
	
	@Resource
	private CoinController coinController;

	@Scheduled(cron = "0 02 01 * * ?")
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
	
	
	@Scheduled(cron = "0 50 * * * ?")
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
	
	
	@Scheduled(cron = "0 0/10 * * * ?")
	public void buy(){
		List<Map<String, Object>> listCoin = coinController.userOwn();
		for(Map<String, Object> map :listCoin){
			
				double closePrice = coinController.closePrice("119");
				List<String> priceList = redisUtil.getList("119priceList");
				
				String lastBuyPrice = priceList.get(priceList.size()-1);
				String firstBuyPrice = priceList.get(0);
				double lastBuyPriceD = Double.valueOf(lastBuyPrice);
				double firstBuyPriceD = Double.valueOf(firstBuyPrice);
				List<String> valuemeList = redisUtil.getList("119valumeList");
				if(!((firstBuyPriceD-lastBuyPriceD)/firstBuyPriceD>0.2)){
					
					if((lastBuyPriceD-closePrice)>0){//最后一次买入价格大于当前价格 判断是否跌了10%
						if(map.get("code").equals("usdt")&&Double.valueOf(map.get("volume").toString())>150){
							double  ss = (lastBuyPriceD-closePrice)/lastBuyPriceD;
							if(ss>0.1){//跌幅超10%  设置当前价格90%价格挂单买入上次买入数量的一半
								coinController.orderBuy("119", String.valueOf(closePrice*0.9),String.valueOf(Double.valueOf(valuemeList.get(valuemeList.size()-1))*0.5));
								
								valuemeList.add(String.valueOf(Double.valueOf(valuemeList.get(valuemeList.size()-1))*0.5));
								redisUtil.setList("119valumeList", valuemeList);
								priceList.add(String.valueOf(closePrice));
								redisUtil.setList("119priceList", priceList);
							}else{
								System.out.println("跌幅未到10%：当前跌幅"+ss);
							}
						}else{
							System.out.println("资金不足150");
						}
						
					}else{
						double  ss = (closePrice-lastBuyPriceD)/closePrice;
						if(ss>0.3){//涨幅超30%  设置当前价格挂单卖出上次买入数量的一半
							coinController.orderSell("119", String.valueOf(closePrice),String.valueOf(Double.valueOf(valuemeList.get(valuemeList.size()-1))*0.5));
							valuemeList.remove(valuemeList.size()-1);
							redisUtil.setList("119valumeList", valuemeList);
							priceList.remove(priceList.size()-1);
							redisUtil.setList("119priceList", priceList);
						}else{
							System.out.println("涨幅未到30%：当前涨幅"+ss);
						}
					}
				}else{
					System.out.println("热币项目亏损已大于20%");
					System.out.println("热币项目进入仅出售阶段");
					double  ss = (closePrice-lastBuyPriceD)/closePrice;
					if(ss>0.3){//涨幅超30%  设置当前价格挂单卖出上次买入数量的一半
						coinController.orderSell("119", String.valueOf(closePrice),String.valueOf(Double.valueOf(valuemeList.get(valuemeList.size()-1))*0.5));
						valuemeList.remove(valuemeList.size()-1);
						redisUtil.setList("119valumeList", valuemeList);
						priceList.remove(priceList.size()-1);
						redisUtil.setList("119priceList", priceList);
					}else{
						System.out.println("涨幅未到30%：当前涨幅"+ss);
					}
				}
			}
		}
}

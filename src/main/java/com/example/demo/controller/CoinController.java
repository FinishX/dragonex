/**   
* @Title: Coin.java 
* @Package com.example.demo.controller 
* @Description:
* @author hjq  
* @date 2018年4月28日 上午9:21:57 
* @version V1.0   
*/
package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.openApi.HostConstant;
import com.example.demo.openApi.HttpParams;
import com.example.demo.openApi.HttpUtils;
import com.example.demo.utils.RedisUtil;

/** 
* @ClassName: Coin 
* @Description:
* @author hjq
* @date 2018年4月28日 上午9:21:57 
*  
*/
@Controller
@RequestMapping("coin")
public class CoinController {
	
	@Resource
	private RedisUtil redisUtil;
//	public static String avgPrice;

	@RequestMapping("price")
	public void setPrice(double price,String volume,String symbolId){
		redisUtil.set("avgPrice", price);
		
		double closePrice = closePrice(symbolId);
		double differenceRatio = (closePrice-price)/price;
		if(differenceRatio<0.9){//如果当前价格小于输入价格的10%则以当前价格买入volume数量
			String orderId = orderBuy(symbolId, String.valueOf(price), volume);
			System.out.println(orderId);
			double ownVolume = userOwn("119");
			System.out.println(ownVolume);
		}
		
//		JSONObject object = new JSONObject();
//		object.put("symbol_id", 119);
//		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				String marketReal = HttpUtils.receiveGet(HostConstant.MAIN_HOST, HostConstant.GET_MARKET_REAL,object);
//				JSONObject marketJson = JSONObject.parseObject(marketReal);
//				String price = marketJson.getString("price_base");
//				
//				System.out.println(marketReal);
//			}
//		}).start();
	}
	
	//获取交易对ID的当前价格
	public double closePrice(String symbolId){
		JSONObject object = new JSONObject();
		object.put("symbol_id", symbolId);
		String closePrice = null;
		new Thread(new Runnable() {
			@Override
			public void run() {
				String marketReal = HttpUtils.receiveGet(HostConstant.MAIN_HOST, HostConstant.GET_MARKET_REAL,object);
//				JSONObject marketJson = JSONObject.parseObject(marketReal);
//				String price = marketJson.getString("close_price");
				JSONObject marketJson = JSONObject.parseObject(marketReal);
				String dataJson = marketJson.getString("data");
				JSONObject jsons = JSONObject.parseObject(dataJson);
				String closePrice = jsons.getString("close_price");
				System.out.println(closePrice);
				closePrice=closePrice;
			}
		}).start();
		return Double.valueOf(closePrice);
	}
	
	
	//买入
		public String orderBuy(String symbolId,String price,String volume){
			JSONObject object = new JSONObject();
			String access_key = "853383797d695e67bfcd768e0706e323";
			String secret_key = "c104adfc7e1b5e5e99c7c307ad65524e";
			object.put("symbol_id", symbolId);	
			object.put("price", price);
			object.put("volume", volume);
			String orderId = null;//订单编号
			String token = redisUtil.get("token");
			HttpParams.setToken(token);
			new Thread(new Runnable() {
				@Override
				public void run() {
					String marketReal = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_ORDER_BUY, object);
//					JSONObject marketJson = JSONObject.parseObject(marketReal);
//					String price = marketJson.getString("close_price");
					JSONObject marketJson = JSONObject.parseObject(marketReal);
					if(marketJson.getString("code").equals("1")){
						String dataJson = marketJson.getString("data");
						JSONObject jsons = JSONObject.parseObject(dataJson);
						String orderId = jsons.getString("order_id");
						System.out.println(orderId);
						orderId=orderId;
					}
				}
			}).start();
			System.out.println(orderId);
			return orderId;
		}
		
		
		public double userOwn(String coinId){
//			JSONObject object = new JSONObject();
			String access_key = "853383797d695e67bfcd768e0706e323";
			String secret_key = "c104adfc7e1b5e5e99c7c307ad65524e";
//			object.put("symbol_id", symbolId);	
//			object.put("price", price);
//			object.put("volume", volume);
			String volume = null;//订单编号
			String token = redisUtil.get("token");
			HttpParams.setToken(token);
			new Thread(new Runnable() {
				@Override
				public void run() {
					String marketReal = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_USER_COIN);
//					JSONObject marketJson = JSONObject.parseObject(marketReal);
//					String price = marketJson.getString("close_price");
					JSONObject marketJson = JSONObject.parseObject(marketReal);
					if(marketJson.getString("code").equals("1")){
						String dataJson = marketJson.getString("data");
						JSONArray jsonArray = JSONArray.parseArray(dataJson);
						for(int i=0;i<jsonArray.size();i++){
							JSONObject job = jsonArray.getJSONObject(i);
							if(coinId.equals(job.getInteger("coin_id"))){
//								return  job.getDouble("1");
							}
//							System.out.println(object);
						}
//						String orderId = jsons.getString("order_id");
//						System.out.println(orderId);
//						orderId=orderId;
					}
				}
			}).start();
			System.out.println(volume);
			return Double.valueOf(volume);
		}
		
//		public static void main(String []args){
//			orderBuy("119", "0.12", "10");
//		}
}

/**   
* @Title: Coin.java 
* @Package com.example.demo.controller 
* @Description:
* @author hjq  
* @date 2018年4月28日 上午9:21:57 
* @version V1.0   
*/
package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.openApi.HostConstant;
import com.example.demo.openApi.HttpParams;
import com.example.demo.openApi.HttpUtils;
import com.example.demo.utils.RedisUtil;
import com.example.demo.utils.SerializeUtil;

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
	@ResponseBody
	public void setPrice(String price,String volume,String symbolId){
//		redisUtil.set("avgPrice", price);
		List<Object> priceList = new ArrayList<Object>();
		List<Object> valumeList = new ArrayList<Object>();
//		Map<String, String> redisMap = new HashMap<>();
		priceList.add(price);
		valumeList.add(volume);
//		redisMap.put("volume", volume);
//		redisMap.put("symbolId", symbolId);
//		redisList.add(redisMap);
		redisUtil.setList(symbolId+"priceList", priceList);
		redisUtil.setList(symbolId+"valumeList", valumeList);
//		System.out.println(redisUtil.getList("priceList"));k
//		if(redisUtil.get("oldPrice")==null){
//			redisUtil.set("oldPrice", price);
//		}
		double closePrice = closePrice(symbolId);
		double ss = Double.valueOf(price);
//		double differenceRatio = (closePrice-ss)/ss;
//		if(differenceRatio<0.9){//如果当前价格小于输入价格的10%则以当前价格买入volume数量
			String orderId = orderBuy(symbolId, String.valueOf(price), volume);
			System.out.println(orderId);
//			double ownVolume = userOwn("119");
//			System.out.println(ownVolume);
//		}
		
	}
	
	//获取交易对ID的当前价格
	@RequestMapping("closePrice")
	@ResponseBody
	public double closePrice(String symbolId){
		JSONObject object = new JSONObject();
		object.put("symbol_id", symbolId);
		String closePrice = null;
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				String marketReal = HttpUtils.receiveGet(HostConstant.MAIN_HOST, HostConstant.GET_MARKET_REAL,object);
//				JSONObject marketJson = JSONObject.parseObject(marketReal);
//				String price = marketJson.getString("close_price");
				JSONObject marketJson = JSONObject.parseObject(marketReal);
				String dataJson = marketJson.getString("data");
				JSONArray array = JSONArray.parseArray(dataJson);
				Object data = array.get(0);
				JSONObject jsons = JSONObject.parseObject(data.toString());
				closePrice = jsons.getString("close_price");
				System.out.println(closePrice);
//			}
//		}).start();
		return Double.valueOf(closePrice);
	}
	
	@RequestMapping("index")
	public void index(){
		
	}
	
	//获取交易对ID的当前价格
	@RequestMapping("coinAll")
	@ResponseBody
		public List<Map<String, Object>> coinAll(){
			JSONObject object = new JSONObject();
			List<Map<String, Object>> returnMap = new ArrayList<Map<String, Object>>();
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					String marketReal = HttpUtils.receiveGet(HostConstant.MAIN_HOST, HostConstant.GET_COIN_ALL,object);
//					JSONObject marketJson = JSONObject.parseObject(marketReal);
//					String dataJson = marketJson.getString("data");
//					JSONArray jsonArray = JSONArray.parseArray(dataJson);
//					for(int i=0;i<jsonArray.size();i++){
//						JSONObject job = jsonArray.getJSONObject(i);
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put("coin_id", job.get("coin_id"));
//						map.put("code", job.get("code"));
//						returnMap.add(map);
//					}
//				}
//			}).start();
			String marketReal = HttpUtils.receiveGet(HostConstant.MAIN_HOST, HostConstant.GET_COIN_ALL,object);
			JSONObject marketJson = JSONObject.parseObject(marketReal);
			String dataJson = marketJson.getString("data");
			JSONArray jsonArray = JSONArray.parseArray(dataJson);
			for(int i=0;i<jsonArray.size();i++){
				JSONObject job = jsonArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("coin_id", job.get("coin_id"));
				map.put("code", job.get("code"));
				returnMap.add(map);
			}
//			map.put("value", returnMap);
			return returnMap;
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
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
					String marketReal = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_ORDER_BUY, object);
//					JSONObject marketJson = JSONObject.parseObject(marketReal);
//					String price = marketJson.getString("close_price");
					JSONObject marketJson = JSONObject.parseObject(marketReal);
					if(marketJson.getString("code").equals("1")){
						String dataJson = marketJson.getString("data");
						JSONObject jsons = JSONObject.parseObject(dataJson);
						orderId = jsons.getString("order_id");
						System.out.println(orderId);
					}else{
						orderId=marketJson.getString("msg");
					}
//				}
//			}).start();
			System.out.println(orderId);
			return orderId;
		}
		
		
		//卖出
				public String orderSell(String symbolId,String price,String volume){
					JSONObject object = new JSONObject();
					String access_key = "853383797d695e67bfcd768e0706e323";
					String secret_key = "c104adfc7e1b5e5e99c7c307ad65524e";
					object.put("symbol_id", symbolId);	
					object.put("price", price);
					object.put("volume", volume);
					String orderId = null;//订单编号
					String token = redisUtil.get("token");
					HttpParams.setToken(token);
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
							String marketReal = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_MARKET_SELL, object);
//							JSONObject marketJson = JSONObject.parseObject(marketReal);
//							String price = marketJson.getString("close_price");
							JSONObject marketJson = JSONObject.parseObject(marketReal);
							if(marketJson.getString("code").equals("1")){
								String dataJson = marketJson.getString("data");
								JSONObject jsons = JSONObject.parseObject(dataJson);
								orderId = jsons.getString("order_id");
								System.out.println(orderId);
							}else{
								orderId=marketJson.getString("msg");
							}
//						}
//					}).start();
					System.out.println(orderId);
					return orderId;
				}
		
		
		@RequestMapping("userOwn")
		@ResponseBody
		public List<Map<String, Object>> userOwn(){
			List<Map<String, Object>> list = new ArrayList<>();
//			JSONObject object = new JSONObject();
			String access_key = "853383797d695e67bfcd768e0706e323";
			String secret_key = "c104adfc7e1b5e5e99c7c307ad65524e";
//			object.put("symbol_id", symbolId);	
//			object.put("price", price);
//			object.put("volume", volume);
			double volume1 = 0;//订单编号
			String token = redisUtil.get("token");
			HttpParams.setToken(token);
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
					
					String marketReal = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_USER_COIN);
//					JSONObject marketJson = JSONObject.parseObject(marketReal);
//					String price = marketJson.getString("close_price");
					JSONObject marketJson = JSONObject.parseObject(marketReal);
					Map<String, Object> map = new HashMap<>();
					if(marketJson.getString("code").equals("1")){
						String dataJson = marketJson.getString("data");
						JSONArray jsonArray = JSONArray.parseArray(dataJson);
						for(int i=0;i<jsonArray.size();i++){
							map = new HashMap<>();
							JSONObject job = jsonArray.getJSONObject(i);
							if(job.getDoubleValue("volume")>0){
								map.put("volume", job.getDoubleValue("volume"));//总数
								map.put("code", job.getString("code"));
								map.put("frozen", job.getDoubleValue("frozen"));//被冻结
								list.add(map);
							}
//							System.out.println(object);
						}
//						String orderId = jsons.getString("order_id");
//						System.out.println(orderId);
//						orderId=orderId;
					}
//				}
//			}).start();
//			System.out.println(volume1);
			return list;
		}
		
//		public static void main(String []args){
//			orderBuy("119", "0.12", "10");
//		}
}

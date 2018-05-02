package com.example.demo.openApi;

import com.alibaba.fastjson.JSONObject;

public class Main {
	public static void main(String[] args) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("start!");
				String access_key = "853383797d695e67bfcd768e0706e323";
				String secret_key = "c104adfc7e1b5e5e99c7c307ad65524e";
				// get token
				String hasToken = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_TOKEN);
				JSONObject tokenJson = JSONObject.parseObject(hasToken);
				String dataJson = tokenJson.getString("data");
				JSONObject tokenJsons = JSONObject.parseObject(dataJson);
				String token = tokenJsons.getString("token");
				// set token
				System.out.println(token);
				HttpParams.setToken(token);
				// get token status
				JSONObject object = new JSONObject();
				object.put("symbol_id", 119);	
				object.put("price", "0.12");
				object.put("volume", "10");
				String tokenStatusJson = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.CHECK_TOKEN_STATUS);
				System.out.println(tokenStatusJson);
				//获取用户所拥有币种
//				String hasCoin = HttpUtils.sendPost(access_key, secret_key, HostConstant.MAIN_HOST, HostConstant.GET_USER_COIN);
//				System.out.println(hasCoin);
//				JSONObject object = new JSONObject();
//				object.put("symbol_id", 119);				
//				String symbolAll = HttpUtils.receiveGet(HostConstant.MAIN_HOST, HostConstant.GET_MARKET_REAL,object);
//				System.out.println(symbolAll);
			}
		}).start();

	}
}
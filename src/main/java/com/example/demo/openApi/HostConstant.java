package com.example.demo.openApi;

public class HostConstant {
	
	public static String MAIN_HOST = "https://openapi.dragonex.im";
	
	// 获取token
	public static String GET_TOKEN = "/api/v1/token/new/";
	
	// 校验token状态
	public static String CHECK_TOKEN_STATUS = "/api/v1/token/status/";
	
	// 获得所有币种信息
	public static String GET_COIN_ALL ="/api/v1/coin/all/";
	
	// 获取你拥有的币种信息
	public static String GET_USER_COIN ="/api/v1/user/own/";
	
	// 获取所有交易对
	public static String GET_SYMBOL_ALL ="/api/v1/symbol/all/";
	
	// 获取k线
	public static String GET_MARKET_KLINE ="/api/v1/market/kline/";
	
	// 获取买盘
	public static String GET_MARKET_BUY ="/api/v1/market/buy/";
	
	// 获取卖盘
	public static String GET_MARKET_SELL ="/api/v1/market/sell/";
	
	// 获取实时行情
	public static String GET_MARKET_REAL ="/api/v1/market/real/";
	
	// 下买单
	public static String GET_ORDER_BUY ="/api/v1/order/buy/";
	
	// 下卖单
	public static String GET_ORDER_SELL ="/api/v1/order/sell/";
	
	// 取消订单
	public static String GET_ORDER_CANCEL ="/api/v1/order/cancel/";
	
	// 获取用户委托记录详情
	public static String GET_ORDER_DETAIL ="/api/v1/order/detail/";
	
	// 获取用户委托记录
	public static String GET_ORDER_HISTORY ="/api/v1/order/history/";
	
	// 获取用户成交记录
	public static String GET_DEAL_HISTORY ="/api/v1/deal/history/";
}

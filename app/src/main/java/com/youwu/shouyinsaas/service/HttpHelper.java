package com.youwu.shouyinsaas.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 代理类
 */
public class HttpHelper implements IhttpProcessor {

    private static IhttpProcessor mIhttpProcessor;
    private static HttpHelper _instance;
	private Map<String,Object> mParams;
    
	private HttpHelper(){
		mParams = new HashMap<>();
    }

   

    public static HttpHelper obtain(){
		synchronized (HttpHelper.class){
			if(_instance == null){
				_instance = new HttpHelper();
			}
		}
        return _instance;
    }

	public static void init(IhttpProcessor httpProcessor){
        mIhttpProcessor = httpProcessor;

    }

    @Override
    public void get(String url, Map<String, Object> params, ICallBack callback) {
		mIhttpProcessor.get(url,params,callback);
    }

    @Override
    public void post(String url, MyHashMap<String> params, ICallBack callback) {

        params.put("store_id", "");
        params.put("type", "");
        params.put("cashier_id", "");
        params.put("store_name", "");
        params.put("user_id", "");
        params.put("user_name", "");
        params.put("arrival_date", "");
        params.put("end_time", "");
        params.put("template_id", "");
        params.put("dot_number", "");
        params.put("order_item", "");
        params.put("order_sn", "");
        params.put("item_details", "");
        params.put("goods_list", "");
        params.put("mark", "");
        params.put("return_order_sn", "");
        params.put("arrival_time", "");
        params.put("order_list", "");


//        params.put("password", "");

		mIhttpProcessor.post(url,params,callback);
    }

    @Override
    public void put(String url, MyHashMap<String> params, ICallBack callback) {
        params.put("phone", "");


        mIhttpProcessor.put(url,params,callback);
    }
    @Override
    public void delete(String url, Map<String, Object> params, ICallBack callback) {
        mIhttpProcessor.delete(url,params,callback);
    }



	//拼接url
	private String appendParams(String url, Map<String, Object> params){
        return "";
	}
}

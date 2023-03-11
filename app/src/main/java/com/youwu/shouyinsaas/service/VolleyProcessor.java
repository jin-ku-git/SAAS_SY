package com.youwu.shouyinsaas.service;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.Map;


public class VolleyProcessor implements IhttpProcessor {

    public static final String TAG ="VolleyProcessor";

    private static RequestQueue mQueue = null;

    public VolleyProcessor(Context context){
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void get(String url, Map<String, Object> params, final ICallBack callback) {
        StringRequestWithAuth stringRequest = new StringRequestWithAuth(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed(volleyError.toString());
            }
        });
        mQueue.add(stringRequest);
    }

    @Override
    public void post(String url, final MyHashMap<String> params, final ICallBack callback) {
//        mQueue = Volley.newRequestQueue(this, null,true, R.raw.srca) ;
        StringRequestWithAuth stringRequest = new StringRequestWithAuth(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed(volleyError.toString());
            }
        }){

            @Override
            protected MyHashMap<String> getParams() throws AuthFailureError {
                MyHashMap<String> postMap = new MyHashMap<String>();

                postMap.put("store_id", params.get("store_id"));
                postMap.put("type", params.get("type"));
                postMap.put("cashier_id", params.get("cashier_id"));
                postMap.put("store_name", params.get("store_name"));
                postMap.put("user_id", params.get("user_id"));
                postMap.put("end_time", params.get("end_time"));
                postMap.put("template_id", params.get("template_id"));
                postMap.put("dot_number", params.get("dot_number"));
                postMap.put("order_item", params.get("order_item"));
                postMap.put("user_name", params.get("user_name"));
                postMap.put("arrival_date", params.get("arrival_date"));
                postMap.put("order_sn", params.get("order_sn"));
                postMap.put("item_details", params.get("item_details"));
                postMap.put("goods_list", params.get("goods_list"));
                postMap.put("mark", params.get("mark"));
                postMap.put("return_order_sn", params.get("return_order_sn"));
                postMap.put("arrival_time", params.get("arrival_time"));
                postMap.put("order_list", params.get("order_list"));


                //过滤掉Value为空的
                MapRemoveNullUtil.removeNullValue(postMap);

                System.out.println("上传参数:"+postMap);
                Log.e("上传参数:",postMap.toString());

                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };
        System.out.println("stringRequest：" + stringRequest);
        mQueue.add(stringRequest);
    }

    @Override
    public void put(String url, MyHashMap<String> params, final ICallBack callback) {
        StringRequestWithAuth stringRequest = new StringRequestWithAuth(Request.Method.PUT,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed(volleyError.toString());
            }
        }){

            @Override
            protected MyHashMap<String> getParams() throws AuthFailureError {
                MyHashMap<String> postMap = new MyHashMap<String>();

                //过滤掉Value为空的
                MapRemoveNullUtil.removeNullValue(postMap);
                System.out.println("上传参数:"+postMap);
                Log.e("上传参数:",postMap.toString());
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };
        System.out.println("stringRequest：" + stringRequest);
        mQueue.add(stringRequest);
    }

    @Override
    public void delete(String url, Map<String, Object> params, final ICallBack callback) {
        StringRequestWithAuth stringRequest = new StringRequestWithAuth(Request.Method.DELETE,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed(volleyError.toString());
            }
        });
        mQueue.add(stringRequest);
    }


}

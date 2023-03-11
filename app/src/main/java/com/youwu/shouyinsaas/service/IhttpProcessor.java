package com.youwu.shouyinsaas.service;

import java.util.Map;

public interface IhttpProcessor {
    //GET请求
    void get(String url, Map<String, Object> params, ICallBack callback);
    //POST请求
    void post(String url, MyHashMap<String> params, ICallBack callback);

    void put(String url, MyHashMap<String> params, ICallBack callback);

    void delete(String url, Map<String, Object> params, ICallBack callback);


}

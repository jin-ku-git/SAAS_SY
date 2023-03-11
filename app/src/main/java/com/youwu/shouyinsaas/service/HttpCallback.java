package com.youwu.shouyinsaas.service;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Result 是javabean对象
 */

public abstract class  HttpCallback<T> implements ICallBack {

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Class<?> cls = analysisClazzInfo(this);


        Log.e("返回结果-----未解析",result);
        Log.e("返回结果-----已解析",toPrettyFormat(result));
        T objResult = (T)gson.fromJson(result,cls);
        onSuccess(objResult);

    }

    /**android 传递的中文数据 解码*/
    private static String decode(String s){
        try {
            s = URLDecoder.decode(s, "utf-8");
            return s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }

    /**android 传递的中文数据 转码*/
    private static String encode(String s){
        try {
            s = URLEncoder.encode(s, "utf-8");
            return s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }


    /**
     * 格式化输出JSON字符串
     * @return 格式化后的JSON字符串
     */
    public static String toPrettyFormat(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    public abstract void onSuccess(T result);

    public static Class<?> analysisClazzInfo(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }


}

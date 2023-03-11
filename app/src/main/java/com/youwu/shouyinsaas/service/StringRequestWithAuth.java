package com.youwu.shouyinsaas.service;



import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.youwu.shouyinsaas.app.AppApplication;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 在Volley http请求中添加请求头
 * Created by dxb on 2017/6/1.
 */
public class StringRequestWithAuth extends StringRequest {

    public StringRequestWithAuth(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequestWithAuth(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new LinkedHashMap<>();
        //添加请求头
        //获取token
        headers.put("Authorization", AppApplication.spUtils.getString("tokenType")+" "+AppApplication.spUtils.getString("accessToken"));

        return headers;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        String parsed = null;
        try {
            parsed = new String(response.data,
                    "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }


}
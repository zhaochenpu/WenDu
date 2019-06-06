package com.nightfeed.wendu.net;

import android.text.TextUtils;


import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by el on 2016/3/14.
 */
public class COkhttp {
    private static COkhttp instance;
    private static OkHttpClient client;

    private COkhttp(){
        //client = new OkHttpClient.Builder().build();
        client = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).readTimeout(10000, TimeUnit.MILLISECONDS).build();
    }

    public static COkhttp getInstance(){

        if(instance == null){

            instance = new COkhttp();
        }

        return instance;
    }

    public OkHttpClient getClient(){
        return client;
    }

    public  String get(String url){
        try {
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public  Call getCall(String url,String[] header){
        Request request;
        if(header==null){
            request = new Request.Builder().url(url).build();
        }else {
            request = new Request.Builder().header(header[0],header[1]).url(url).build();
        }

        if(client==null){
            getInstance();
        }
        return client.newCall(request);
    }

    public  String get(String url, int timeout){
        try {
            OkHttpClient clone= client.newBuilder().connectTimeout(timeout, TimeUnit.MILLISECONDS).readTimeout(timeout, TimeUnit.MILLISECONDS).build();
            Request request = new Request.Builder().url(url).build();

            Response response = clone.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public Call postCall(String url, Map<String, String> params){
        FormBody.Builder builder = new FormBody.Builder();
        addParams(builder,params);
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        return client.newCall(request);
    }

    public  String post(String url, Map<String, String> params){
        try {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder,params);
            Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static void addParams(FormBody.Builder builder, Map<String, String> params) {
        for (String key : params.keySet())
        {
            if(!TextUtils.isEmpty(params.get(key))){
                builder.add(key, params.get(key));
            }else{
                builder.add(key,"");
            }
        }
    }

    public Call postCall(String url, JSONObject JSON){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),JSON.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return client.newCall(request);
    }
}

package com.nightfeed.wendu.net

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.nightfeed.wendu.utils.SystemUtil
import okhttp3.*
import java.io.IOException

class RequestUtils {
    interface OnResultListener{
        fun onSuccess(result:String)
        fun onError()
    }

    companion object {
        fun get(url:String, onRequestListener:OnResultListener?){
            get(url,null,onRequestListener)
        }

        fun get(url:String, header:Array<String>?, onRequestListener:OnResultListener?){
            val mHandler =  object : Handler(Looper.getMainLooper()){}
            COkhttp.getCall(url,header).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    if (onRequestListener!=null){
                        mHandler.post { onRequestListener.onError() }
                    }
                }

                override fun onResponse(call: Call?, response: Response?) {
                    try {
                        if (response!!.isSuccessful) {
                            val result=response.body()!!.string()
                            if(TextUtils.isEmpty(result)){
                                if (onRequestListener!=null){
                                    mHandler.post { onRequestListener.onError() }
                                }
                            }else{
                                if (onRequestListener!=null){
                                    mHandler.post { onRequestListener.onSuccess(result) }
                                }
                            }
                        }else{
                            if (onRequestListener!=null){
                                mHandler.post { onRequestListener.onError() }
                            }
                        }
                    }catch (e:Throwable){
                        if (onRequestListener!=null){
                            mHandler.post { onRequestListener.onError() }
                        }
                    }

                }
            })
        }

        fun post(url:String,params:Map<String, String>, onRequestListener:OnResultListener?){
            val mHandler =  object : Handler(Looper.getMainLooper()){}
            COkhttp.postCall(url,params).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    if (onRequestListener!=null){
                        mHandler.post { onRequestListener.onError() }
                    }
                }

                override fun onResponse(call: Call?, response: Response?) {
                    try {
                        if (response!!.isSuccessful) {
                            val result=response.body()!!.string()
                            if(TextUtils.isEmpty(result)){
                                if (onRequestListener!=null){
                                    mHandler.post { onRequestListener.onError() }
                                }
                            }else{
                                if (onRequestListener!=null){
                                    mHandler.post { onRequestListener.onSuccess(result) }
                                }
                            }
                        }else{
                            if (onRequestListener!=null){
                                mHandler.post { onRequestListener.onError() }
                            }
                        }
                    }catch (e:Throwable){
                        if (onRequestListener!=null){
                            mHandler.post { onRequestListener.onError() }
                        }
                    }

                }
            })
        }

        fun getPic(url: String, onRequestListener:OnResultListener?) {
            val mHandler =  object : Handler(Looper.getMainLooper()){}

            //获取okHttp对象get请求
            try {

                COkhttp.getCall(url,null).enqueue(object :Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        if (onRequestListener!=null){
                            mHandler.post { onRequestListener.onError() }
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {

                        var path= SystemUtil.saveBitmap(BitmapFactory.decodeStream(response!!.body()!!.byteStream()),System.currentTimeMillis().toString())!!.path
                        if(!TextUtils.isEmpty(path)){
                            if (onRequestListener!=null){
                                mHandler.post { onRequestListener.onSuccess(path) }
                            }
                        }else{
                            if (onRequestListener!=null){
                                mHandler.post { onRequestListener.onError() }
                            }
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                if (onRequestListener!=null){
                    mHandler.post { onRequestListener.onError() }
                }
            }
        }
    }


}
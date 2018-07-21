package com.nightfeed.wendu.net

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class RequestUtils {
    interface OnResultListener{
        fun onSuccess(result:String)
        fun onError()
    }

    companion object {
        fun get(url:String, onRequestListener:OnResultListener?){
            val mHandler =  object : Handler(Looper.getMainLooper()){}
            COkhttp.getCall(url).enqueue(object : Callback {
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
    }

}
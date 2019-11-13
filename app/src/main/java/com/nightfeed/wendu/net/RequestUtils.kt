package com.nightfeed.wendu.net


import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.nightfeed.wendu.utils.SystemUtil
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

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
            COkhttp.getInstance().getCall(url,header).enqueue(object : Callback {
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
            COkhttp.getInstance().postCall(url,params).enqueue(object : Callback {
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

        fun post(url:String, body: JSONObject, onRequestListener:OnResultListener?){
            val mHandler =  object : Handler(Looper.getMainLooper()){}
            COkhttp.getInstance().postCall(url,body).enqueue(object : Callback {
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

                COkhttp.getInstance().getCall(url,null).enqueue(object :Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        if (onRequestListener!=null){
                            mHandler.post { onRequestListener.onError() }
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {

                        var path="";
                        if (url.contains(".gif")||url.contains(".GIF")||url.contains(".Gif")){
                            path=saveFile("gif",response,mHandler,null)
                        }else{
                            path=SystemUtil.saveBitmap(BitmapFactory.decodeStream(response!!.body()!!.byteStream()),System.currentTimeMillis().toString(),"png")!!.path
                        }
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

        fun downloadFile(url: String,type: String, onRequestListener:OnResultListener){
            val mHandler =  object : Handler(Looper.getMainLooper()){}

            //获取okHttp对象get请求
            try {

                COkhttp.getInstance().getCall(url,null).enqueue(object :Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        mHandler.post { onRequestListener.onError() }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        saveFile(type,response,mHandler,onRequestListener)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                mHandler.post { onRequestListener.onError() }
            }
        }

        fun saveFile(type: String,response: Response?,mHandler : Handler,onRequestListener:OnResultListener?):String{
            var name=""
            var fileDir = Environment.getExternalStorageDirectory().absolutePath + "/wendu/"
            var inputStream: InputStream? = null
            val buf = ByteArray(2048)
            var len = 0
            var fos: FileOutputStream? = null
            // 储存下载文件的目录
            val any = try {
                 name = SimpleDateFormat("yyyyMMddHHmmss").format(Date()) +"."+ type
                inputStream = response!!.body()!!.byteStream()
                val total = response.body()!!.contentLength()
                val file = File(fileDir, name)
                fos = FileOutputStream(file)
                var sum: Long = 0
                len = inputStream!!.read(buf);
                while (len != -1) {
                    fos!!.write(buf, 0, len)
                    sum += len.toLong()
                    val progress = (sum * 1.0f / total * 100).toInt()
                    len = inputStream!!.read(buf)
                }
                fos!!.flush()
                // 下载完成
                mHandler.post { onRequestListener?.onSuccess(fileDir + "/" + name) }

            } catch (e: Exception) {
                e.printStackTrace()
                mHandler.post { onRequestListener?.onError() }

            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                }

                try {
                    fos?.close()
                } catch (e: IOException) {
                }
            }
            return  fileDir+name
        }
    }

}
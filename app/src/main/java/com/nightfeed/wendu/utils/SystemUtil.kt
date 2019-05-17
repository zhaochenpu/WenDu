package com.nightfeed.wendu.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget



class SystemUtil{
    companion object {

        fun shareBitmap(activity: Activity,url: Uri){
            var share_intent =  Intent()
            share_intent.action = Intent.ACTION_SEND //设置分享行为
            share_intent.type = "image/*"   //设置分享内容的类型
            share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            share_intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(activity, "com.nightfeed.wendu.fileprovider",File(url.toString())))
            activity.startActivity(share_intent)
        }

        fun shareBitmap(activity: Activity,bm: Bitmap,type :String){
            var share_intent =  Intent()
            share_intent.action = Intent.ACTION_SEND //设置分享行为
            share_intent.type = "image/*"   //设置分享内容的类型
            share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            var f=saveBitmap(bm,"img",type)

            if(f!=null){
                share_intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(activity, "com.nightfeed.wendu.fileprovider",f))
                activity.startActivity(share_intent);
            }
        }


        /** * 将图片存到本地 */
        fun  saveBitmap(bm: Bitmap,picName :String,type :String): File? {
            try {
                var f =  File(Environment.getExternalStorageDirectory().absolutePath +"/wendu/"+picName+"."+type)
                if (!f.exists()) {
                    f.parentFile.mkdirs()
                    f.createNewFile()
                }
                var out = FileOutputStream(f)
                bm.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()
                return f
            } catch ( e: FileNotFoundException) {
                e.printStackTrace()
            } catch ( e: IOException) {
                e.printStackTrace()
            }
            return null
        }

//        fun saveGif( imgUrl:String,  context:Context,  finalImagePath:String) {
//
//            copyFile(Glide.with(context).load(imgUrl).get().getAbsolutePath(), finalImagePath)
//        }
    }
}
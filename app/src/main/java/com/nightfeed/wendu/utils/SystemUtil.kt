package com.nightfeed.wendu.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.TextUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


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

        fun getImageTemporaryFile(context: Context): File {
            return File(context.filesDir, "pic.jpg")
        }

        fun getSelectFilePath(context: Context, uri: Uri): String? {

            // DocumentProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)

                    if (!TextUtils.isEmpty(id) && id.startsWith("raw:")) {
                        return id.replaceFirst("raw:".toRegex(), "")
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                            val contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                            return getDataColumn(context, contentUri, null, null)
                        } else {

                            return getDataColumn(context, uri, null, null)
                        }
                    }
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)
            return null
        }

        fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                          selectionArgs: Array<String>?): String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)

                if (cursor != null) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index)
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                var u = uri!!.path
                if (u!!.contains("/storage/")) {
                    u = "/storage/" + u.split("/storage/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                } else if (u.contains("/external/")) {
                    u = "/storage/emulated/0/" + u.split("/external/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                }
                return u
            } finally {
                cursor?.close()
            }
            return null
        }


        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }


    }
}
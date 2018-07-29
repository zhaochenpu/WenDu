package com.nightfeed.wendu.net

import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyJSON {

    companion object {
        fun getString(content: JSONObject, content_title:String) : String {
            try {
                return content.getString(content_title)
            }catch (e: JSONException){

            }
            return ""
        }

        fun getString(content: String?, content_title:String) : String {
            try {
                if(!TextUtils.isEmpty(content)){
                    return JSONObject(content).getString(content_title)
                }
            }catch (e: JSONException){

            }
            return ""
        }

        fun getJSONObject(content: String?, content_title:String) : JSONObject? {
            try {
                if(!TextUtils.isEmpty(content)){
                    return JSONObject(content).getJSONObject(content_title)
                }
            }catch (e: JSONException){

            }
            return null
        }


        fun getJSONArray(content: String?, content_title:String) : JSONArray? {
            try {
                if(!TextUtils.isEmpty(content)){
                    return JSONObject(content).getJSONArray(content_title)
                }
            }catch (e: JSONException){

            }
            return null
        }
        fun getJSONArray(content: JSONObject, content_title:String) : JSONArray? {
            try {
                return content.getJSONArray(content_title)
            }catch (e: JSONException){

            }
            return null
        }
    }
}
package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.OneSentence
import com.nightfeed.wendu.model.ZhiHuDetail
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_zhihu.*

class ZhiHuActivity : AppCompatActivity() {
    val instance by lazy { this }
    var id=""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu)
        StatusBarUtil.transparencyBar(instance)

        setSupportActionBar(toolbar)
        id=intent.getStringExtra("id")
        zhihu_title.text=intent.getStringExtra("title")
        Glide.with(instance).load(intent.getStringExtra("image_url")).into(imageview)

        val webSettings = zhihu_webview.settings
        webSettings.javaScriptEnabled = true// enable navigator.geolocation
        webSettings.domStorageEnabled = true
        webSettings. setAppCacheEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }


//        zhihu_webview.loadUrl(webUrl)

        RequestUtils.get(URLs.ZHIHU_CONTENT+id,object :RequestUtils.OnResultListener{
            override fun onSuccess(result: String) {
                var detail= Gson().fromJson(result, ZhiHuDetail::class.java)
                Glide.with(instance).load(detail.image).into(imageview)
                image_source.text=detail.image_source

                if(TextUtils.isEmpty(detail.body)){
                    zhihu_webview.loadUrl(detail.share_url)
                }else{
                    val css = "<link rel=\"stylesheet\" href=\"file:///android_asset/news.css\" type=\"text/css\">"
                    var html = "<html><head>" + css + "</head><body>" + detail.body + "</body></html>"
                    html = html.replace("<div class=\"img-place-holder\">", "")
                    zhihu_webview.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null)
                }
            }

            override fun onError() {

            }
        })
    }


}

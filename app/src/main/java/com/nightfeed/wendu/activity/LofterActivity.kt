package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import com.nightfeed.wendu.R
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.activity_lofter.*

class LofterActivity : AppCompatActivity() {
    val instance by lazy { this }
    var webUrl=""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lofter)
        webUrl=intent.getStringExtra("url")

        val webSettings = lofter_webview.settings
        webSettings.javaScriptEnabled = true// enable navigator.geolocation
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true;
        webSettings.loadWithOverviewMode = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        lofter_webview.addJavascriptInterface(MVPJSInterface(), "mb.m.meizu.g")

        lofter_webview.loadUrl(webUrl)
    }


    internal inner class MVPJSInterface {
        @JavascriptInterface
        fun <T> goTagPage (text: T) {
            val intent = Intent(instance,LofterActivity::class.java)
            //获取intent对象
            intent.putExtra("url", text.toString())
            startActivity(intent)
        }
    }
}

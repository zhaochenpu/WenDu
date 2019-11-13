package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
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

        var title=intent.getStringExtra("label")
        if(TextUtils.isEmpty(title)){
            toolbar.title=""
        }else{
            toolbar.title=intent.getStringExtra("label")
        }

        setSupportActionBar(toolbar)
        webUrl=intent.getStringExtra("url")

        val webSettings = lofter_webview.settings
        webSettings.javaScriptEnabled = true// enable navigator.geolocation
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true;
        webSettings.loadWithOverviewMode = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        lofter_webview.addJavascriptInterface(MVPJSInterface(), "flymenews")

        lofter_webview.loadUrl(webUrl)

        toolbar.setNavigationOnClickListener {startActivity(Intent(instance,BrowsePictureActivity::class.java))}
    }


    internal inner class MVPJSInterface {
        @JavascriptInterface
        public fun clickRecommendImage(paramString:String ) {
            val intent = Intent(instance,LofterActivity::class.java)
            //获取intent对象
            intent.putExtra("url", paramString)
            startActivity(intent)
        }

        @JavascriptInterface
        public fun jumpLabelDetail(paramString:String) {
            val intent = Intent(instance,BrowsePictureActivity::class.java)
            intent.putExtra("label",paramString)
            startActivity(intent)
            finish()
        }
    }
}

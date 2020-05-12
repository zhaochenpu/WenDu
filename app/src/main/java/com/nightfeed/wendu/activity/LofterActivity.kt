package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nightfeed.wendu.R
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.view.ImagePopUpWindow
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
        lofter_webview.addJavascriptInterface(MVPJSInterface(instance), "flymenews")

        lofter_webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                addImageClickListener()
            }
        }

        lofter_webview.loadUrl(webUrl)

        toolbar.setNavigationOnClickListener {startActivity(Intent(instance,BrowsePictureActivity::class.java))}
    }

    fun addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        lofter_webview.loadUrl("javascript:(function(){var imgs = document.getElementsByTagName(\"img\");var imgUrl='';var arr = new Array();for(var i=0;i<imgs.length;i++){  imgs[i].pos = i;  arr[i] = imgs[i].src; imgUrl+=imgs[i].src+',,,';   imgs[i].onclick=function(){     window.flymenews.openImage(this.src);     }  }})()")
    }

    internal inner class MVPJSInterface (activity :Activity){
        var activity=activity

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

        @JavascriptInterface
        fun openImage(img: String) {

            ImagePopUpWindow(activity, null,img)
        }
    }
}

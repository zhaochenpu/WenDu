package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import com.nightfeed.wendu.R
import kotlinx.android.synthetic.main.activity_lofter.*

class ENSentenceActivity : AppCompatActivity() {
    val instance by lazy { this }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lofter)

        toolbar.title=""

        setSupportActionBar(toolbar)

        val webSettings = lofter_webview.settings
        webSettings.javaScriptEnabled = true// enable navigator.geolocation
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true;
        webSettings.loadWithOverviewMode = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        lofter_webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                if (Build.VERSION.SDK_INT < 26) {
                    lofter_webview.loadUrl(url)
                    return true
                } else {
                    return false
                }
            }
        }

        lofter_webview.loadUrl(intent.getStringExtra("url"))

        toolbar.setNavigationOnClickListener {startActivity(Intent(instance,ImageWordActivity::class.java))}
    }

    override fun onBackPressed() {
        if(lofter_webview.canGoBack()){
            lofter_webview.goBack()
        }else{
            finish()
        }
    }
}

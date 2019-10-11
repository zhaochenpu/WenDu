package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import com.nightfeed.wendu.R
import kotlinx.android.synthetic.main.activity_lofter.*

class WebActivity : AppCompatActivity() {
    val instance by lazy { this }

    var webUrl=""
    var title=""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lofter)

        title=intent.getStringExtra("title")
        toolbar.title=title

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
                if(url.contains("gamersky://")){
                    return true
                }
                if (Build.VERSION.SDK_INT < 26) {
                    lofter_webview.loadUrl(url)
                    return true
                } else {
                    return false
                }
            }
        }

        webUrl=intent.getStringExtra("url")
        lofter_webview.loadUrl(webUrl)

        toolbar.setNavigationOnClickListener { finish()}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent1 = Intent(Intent.ACTION_SEND)

        intent1.putExtra(Intent.EXTRA_TEXT, title+"\n"+webUrl)
        intent1.type = "text/plain"
        startActivity(Intent.createChooser(intent1, title))
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if(lofter_webview.canGoBack()){
            lofter_webview.goBack()
        }else{
            finish()
        }
    }
}

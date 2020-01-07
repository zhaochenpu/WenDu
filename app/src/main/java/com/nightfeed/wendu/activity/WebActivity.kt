package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.nightfeed.wendu.R
import com.nightfeed.wendu.view.ImagePopUpWindow
import com.nightfeed.wendu.view.NestedScrollWebView
import kotlinx.android.synthetic.main.activity_lofter.*


class WebActivity : AppCompatActivity() {
    val instance by lazy { this }

    var webUrl=""
    var title=""
    var isTitleGone=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lofter)

        title=intent.getStringExtra("title")
        toolbar.title=""
        setSupportActionBar(toolbar)

        lofter_webview.settings.javaScriptEnabled = true// enable navigator.geolocation
        lofter_webview.settings.domStorageEnabled = true
        lofter_webview.settings.useWideViewPort = true;
        lofter_webview.settings.loadWithOverviewMode = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lofter_webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        lofter_webview.addJavascriptInterface( JavascriptInterface(instance),"imageListener")

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

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressbar.visibility=View.GONE
                addImageClickListener()
            }
        }

        lofter_webview.webChromeClient=object :WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress in 1..97 ) {
                    if(progressbar.visibility != View.VISIBLE){
                        progressbar.visibility = View.VISIBLE
                    }
                    progressbar.progress = newProgress
                }else if (newProgress>97&&progressbar.visibility == View.VISIBLE){
                    progressbar.visibility = View.GONE
                    addImageClickListener()
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        lofter_webview.setOnScrollChangeListener(object : NestedScrollWebView.OnScrollChangeListener{
            override fun onPageTop(l: Int, t: Int, oldl: Int, oldt: Int) {
                if(!isTitleGone){
                    supportActionBar!!.title=""
                    isTitleGone=true
                }
            }

            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                if (isTitleGone&&t>supportActionBar!!.height){
                    supportActionBar!!.title=title
                    isTitleGone=false
                }
            }
        })

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

    fun addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        lofter_webview.loadUrl("javascript:(function(){var imgs = document.getElementsByTagName(\"img\");var imgUrl='';var arr = new Array();for(var i=0;i<imgs.length;i++){  imgs[i].pos = i;  arr[i] = imgs[i].src; imgUrl+=imgs[i].src+',,,';   imgs[i].onclick=function(){     window.imageListener.openImage(this.src);     }  }})()")
    }

    class JavascriptInterface (activity :Activity){
        var activity=activity
        @android.webkit.JavascriptInterface
        fun openImage(arr: Array<String?>, postion: Int) {
            val imgUrlList: ArrayList<String?> = ArrayList()
            for (i in arr.indices) {
                imgUrlList.add(arr[i])
            }
            //实现自己的图片浏览页面
//            ImagePagerActivity.showActivity(context, imgsUrl, postion)
        }

        @android.webkit.JavascriptInterface
        fun openImage(img: String, chickUrl: String?, postion: Int) {
            val imgs = img.split(",").toTypedArray()
            val imgsUrl: ArrayList<String> = ArrayList()
            for (i in imgs.indices) {
                imgsUrl.add(imgs[i])
            }
            //实现自己的图片浏览页面
//            ImagePagerActivity.showActivity(context, imgsUrl, postion)
        }

        //注解 很重要，必不可少
        @android.webkit.JavascriptInterface
        fun openImage(img: String) {

            ImagePopUpWindow(activity, null,img)
        }
    }
}



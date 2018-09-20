package com.nightfeed.wendu.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Explode
import android.transition.Fade
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.webkit.WebSettings
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.ZhiHuDB
import com.nightfeed.wendu.model.ZhiHuDetail
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.CollapsingToolbarLayoutState
import com.nightfeed.wendu.utils.ScreenUtils
import com.nightfeed.wendu.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_zhihu.*



class ZhiHuActivity : AppCompatActivity() {

    val instance by lazy { this }

    private var collapsingState= CollapsingToolbarLayoutState.EXPANDED
    private var id=""
    private var title=""
    var detail :ZhiHuDetail?=null
    var distance=0
    var read=false
    var onOffsetChangedListenerby :AppBarLayout.OnOffsetChangedListener?=null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu)
        initView()

        setListener()

        RequestUtils.get(URLs.ZHIHU_CONTENT+id,object :RequestUtils.OnResultListener{
            override fun onSuccess(result: String) {
                 detail= Gson().fromJson(result, ZhiHuDetail::class.java)
                if(detail!=null){
                    if(TextUtils.isEmpty(detail!!.image)){
                        app_bar.removeOnOffsetChangedListener(onOffsetChangedListenerby)
                        zhihu_title.setTextColor(ContextCompat.getColor(instance, R.color.textPrimary))
                        toolbar.navigationIcon = getDrawable(R.drawable.back)
                        share.setImageResource(R.drawable.share_black)
                        StatusBarUtil.StatusBarLightMode(instance)

                        var layoutParams=collapsingtoolbar_layout.layoutParams
                        layoutParams.height=ScreenUtils.dip2px(instance,90f)
                        collapsingtoolbar_layout.layoutParams=layoutParams

                        collapsingtoolbar_layout.isNestedScrollingEnabled=false

                        image_layout.visibility=View.INVISIBLE

                        zhihu_title.text = title
                    }else{
                        Glide.with(instance).load(detail!!.image).into(imageview)
                        image_source.text=detail!!.image_source
                    }

                    if(TextUtils.isEmpty(detail!!.body)){
                        zhihu_webview.loadUrl(detail!!.share_url)
                    }else{
                        val css = "<link rel=\"stylesheet\" href=\"file:///android_asset/news.css\" type=\"text/css\">"
                        var html = "<html><head>" + css + "</head><body>" + detail!!.body + "</body></html>"
                        html = html.replace("<div class=\"img-place-holder\">", "")
                        zhihu_webview.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null)
                    }

                    ZhiHuDB(id).save()
                    read=true
                }
            }

            override fun onError() {

            }
        })
    }

    private fun initView() {
        StatusBarUtil.transparencyBar(instance)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        title = intent.getStringExtra("title")
        zhihu_title2.text = title

        id = intent.getStringExtra("id")

        val webSettings = zhihu_webview.settings
        webSettings.javaScriptEnabled = true// enable navigator.geolocation
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        TextViewCompat.setAutoSizeTextTypeWithDefaults(zhihu_title, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(zhihu_title, 14, 18, 1, TypedValue.COMPLEX_UNIT_DIP)
    }

    private fun setListener() {
        onOffsetChangedListenerby=AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if(distance==0){
                distance=(app_bar.totalScrollRange-ScreenUtils.dip2px(instance,56f))
            }
            if (verticalOffset == 0) {
                if (collapsingState !== CollapsingToolbarLayoutState.EXPANDED) {
                    collapsingState = CollapsingToolbarLayoutState.EXPANDED//修改状态标记为展开

//                    zhihu_title.setTextColor(Color.WHITE)
                    toolbar.navigationIcon = getDrawable(R.drawable.back_white)
                    share.setImageResource(R.drawable.share_white)
                    StatusBarUtil.StatusBarDarkMode(instance)
                    zhihu_title.text = ""
                }
            } else if (Math.abs(verticalOffset) >=distance ) {
                if (collapsingState !== CollapsingToolbarLayoutState.COLLAPSED) {
                    collapsingState = CollapsingToolbarLayoutState.COLLAPSED//修改状态标记为折叠

//                    zhihu_title.setTextColor(ContextCompat.getColor(instance, R.color.textPrimary))
                    toolbar.navigationIcon = getDrawable(R.drawable.back)
                    share.setImageResource(R.drawable.share_black)
                    StatusBarUtil.StatusBarLightMode(instance)

                    zhihu_title.text = title
                }
            } else {
                if (collapsingState !== CollapsingToolbarLayoutState.INTERNEDIATE) {

                    collapsingState = CollapsingToolbarLayoutState.INTERNEDIATE//修改状态标记为中间
                }
            }
        }

        app_bar.addOnOffsetChangedListener(onOffsetChangedListenerby)

        toolbar.setNavigationOnClickListener {
            finishActivity()
        }

        share.setOnClickListener {
            if(detail!=null) {
                val intent1 = Intent(Intent.ACTION_SEND)

                intent1.putExtra(Intent.EXTRA_TEXT, title+"\n"+detail!!.share_url)
                intent1.type = "text/plain"
                startActivity(Intent.createChooser(intent1, title))
            }
        }
    }


    override fun onBackPressed() {
        finishActivity()
    }

    fun finishActivity(){

        if (read){
            setResult(Activity.RESULT_OK)
        }
        finishAfterTransition()
    }

}

package com.nightfeed.wendu.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.nightfeed.wendu.R

import kotlinx.android.synthetic.main.activity_wei_bo.*
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.constant.WBConstants.Base.APP_KEY
import android.content.Intent
import android.text.TextUtils
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.SPUtils
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler







class WeiBoActivity : AppCompatActivity() {

    val instance by lazy { this }

    lateinit var mAuthInfo : AuthInfo

    lateinit var mSsoHandler : SsoHandler

    lateinit var weiboKey : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wei_bo)
        setSupportActionBar(toolbar)
        toolbar.title="微博"

        mAuthInfo= AuthInfo(this, "1767153228","https://api.weibo.com/oauth2/default.html",null)
        WbSdk.install(this, mAuthInfo)

        weiboKey= SPUtils.get(instance,"weibokey","") as String

        if(TextUtils.isEmpty(weiboKey)){
            mSsoHandler = SsoHandler(instance)
            mSsoHandler.authorize(object :WbAuthListener{
                override fun onSuccess(p0: Oauth2AccessToken?) {
                    SPUtils.put(instance,"weibokey",p0?.token ?: "")
                    weiboKey= p0?.token ?: ""
                }

                override fun onFailure(p0: WbConnectErrorMessage?) {
                }

                override fun cancel() {
                }

            })
        }

        weibo_list
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
        }
    }

    private fun initList(){
        RequestUtils.get(URLs.WEIBO_HOME_TIMELINE+weiboKey,object : RequestUtils.OnResultListener{
            override fun onSuccess(result: String) {

            var s=result;
            }

            override fun onError() {
            }
        })
    }
}

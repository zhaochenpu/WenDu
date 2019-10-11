package com.nightfeed.wendu

import android.app.Application
import android.content.Context
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import org.litepal.LitePal

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
        context=applicationContext

        OCR.getInstance(context).initAccessTokenWithAkSk(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                // 调用成功，返回AccessToken对象
                val token = accessToken.accessToken
            }

            override fun onError(error: OCRError) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, context, "9Ecc1h034wnTRyA5DXKmT3pH", "4PCcay5FPOTXbR0UwcayL81zT4FLNzbG")
    }

    companion object {
        lateinit var context : Context

    }
}
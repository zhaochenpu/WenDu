package com.nightfeed.wendu

import android.app.Application
import android.content.Context
import org.litepal.LitePal

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
        context=applicationContext
    }

    companion object {
        lateinit var context : Context

    }
}
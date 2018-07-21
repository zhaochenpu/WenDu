package com.nightfeed.wendu

import android.app.Application
import org.litepal.LitePal

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
    }
}
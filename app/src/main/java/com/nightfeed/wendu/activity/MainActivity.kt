package com.nightfeed.wendu.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.baidu.location.BDLocation
import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.MainMenuFragment
import com.nightfeed.wendu.model.Weather
import com.nightfeed.wendu.net.*
import com.nightfeed.wendu.utils.PermissionUtil
import com.nightfeed.wendu.utils.ToastUtil
import com.nightfeed.wendu.view.flowingdrawer.FlowingView
import com.nightfeed.wendu.view.flowingdrawer.LeftDrawerLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val instance by lazy { this }
    var mLeftDrawerLayout: LeftDrawerLayout?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= 23) run {
            //6.0以后系统对敏感权限进行动态权限申请
           var permissionUtil = PermissionUtil(instance)
            val lackPermissions = permissionUtil.getLacksPermissions(PermissionUtil.MUST_SECURITY_PERMISSIONS)

            if (lackPermissions != null && lackPermissions!!.size > 0) {
                // 缺少权限时, 进入权限配置页面
                ActivityCompat.requestPermissions(this, lackPermissions!!, PermissionUtil.REQUEST_PERMISSION_CODE)
            }else{
                initLocationClient()
            }
        }else{
            initLocationClient()
        }

        mLeftDrawerLayout = findViewById<View>(R.id.main_drawerlayout) as LeftDrawerLayout
        var mMenuFragment = MainMenuFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_menu, mMenuFragment ).commit()
        val mFlowingView = findViewById<View>(R.id.main_flowing) as FlowingView
        mLeftDrawerLayout!!.setFluidView(mFlowingView)
        mLeftDrawerLayout!!.setMenuFragment(mMenuFragment)
//
//        list_swipe_refresh.isRefreshing=true
        toolbar.setNavigationOnClickListener {     mLeftDrawerLayout!!.toggle() }
    }



    override fun onBackPressed() {

        if (mLeftDrawerLayout!!.isShownMenu) {
            mLeftDrawerLayout!!.toggle()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionUtil.REQUEST_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocationClient()
                } else {

                }
                return
            }
        }
    }

    private fun initLocationClient() {
        WLocationClient.get().setResultListener(object : WLocationClient.OnResultListener {
            override fun onSuccess(location: BDLocation) {
//                var s=location.district
                supportActionBar!!.title=location.district
                WeatherRequest.get(location.latitude,location.longitude,object:WeatherRequest.OnResultListener{
                    override fun onSuccess(weather: Weather) {

                        realtime_temp.text=weather.realtimeWeather.temperature+"°"
                        realtime_weather_des.text=weather.realtimeWeather.weatherDes

                        today_temp.text= weather.fifteenDailyWeather!!.dailies[1].tempMin+"°/"+ weather.fifteenDailyWeather!!.dailies[1].tempMax+"°"

                        realtime_wind.text=weather.realtimeWeather.windDirection+" "+weather.realtimeWeather.windLevel
                        realtime_humidity.text="湿度"+weather.realtimeWeather.humidity

                    }

                    override fun onError() {
                        ToastUtil.showNetError(instance)
                    }

                })
            }

            override fun onNetError() {
            }

            override fun onPermissionError() {
            }
        })
        WLocationClient.get().startLocationClient()

    }
}



package com.nightfeed.wendu.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.baidu.location.BDLocation
import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.MainMenuFragment
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.PermissionUtil
import com.nightfeed.wendu.net.WLocationClient
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
//        if(requestCode==122&&resultCode==RESULT_OK){
//            setRead()
//        }
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
                RequestUtils.get(URLs.WEATHER+location.latitude+"&lng="+location.longitude,object:RequestUtils.OnResultListener{
                    override fun onSuccess(result: String) {
                        Log.e("weather",result)

                    }

                    override fun onError() {
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



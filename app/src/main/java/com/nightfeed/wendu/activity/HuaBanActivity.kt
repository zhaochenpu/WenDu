package com.nightfeed.wendu.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightfeed.wendu.R
import com.nightfeed.wendu.adapter.ImageListAdapter
import com.nightfeed.wendu.adapter.LofterListAdapter
import com.nightfeed.wendu.model.HuaBan
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ScreenUtils
import com.nightfeed.wendu.view.ImagePopUpWindow
import kotlinx.android.synthetic.main.activity_hua_ban.*
import java.util.ArrayList

class HuaBanActivity : AppCompatActivity() {

    val instance by lazy { this }
//    private var adapter:ImageListAdapter?=null

    private var recommendNumber=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hua_ban)
        toolbar.title=intent.getStringExtra("raw_text")
        setSupportActionBar(toolbar)
        recommendNumber=intent.getIntExtra("recommend",0)
        if(recommendNumber!=0){
            huaban_detail.transitionName="huaban_recommend"+recommendNumber.toString()
        }

        huaban_detail.minimumHeight=intent.getIntExtra("height",0)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(instance,BrowsePictureActivity::class.java))
            finishAfterTransition()
        }
        Glide.with(instance).load(URLs.HUA_BAN_IM+intent.getStringExtra("key")).apply(RequestOptions.fitCenterTransform()).into(huaban_detail)

        huaban_detail.setOnLongClickListener { v ->
            ImagePopUpWindow(instance,v!!, URLs.HUA_BAN_IM+intent.getStringExtra("key"))
            true
        }

        huaban_recommend.layoutManager=StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        huaban_recommend.setLayerType(View.LAYER_TYPE_NONE, null)

        RequestUtils.get(URLs.HUA_BAN_RECOMMEND.replace("pin_id",intent.getStringExtra("pin_id")), object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                if (!TextUtils.isEmpty(result)) {
                    var recommend: ArrayList<HuaBan> = Gson().fromJson(result, object : TypeToken<List<HuaBan>>() {}.type)
                    if (recommend.size>0) {
                        huaban_recommend.adapter= ImageListAdapter(instance, recommend,true,object : ImageListAdapter.OnClickListener {
                            override fun onClick(v: View) {
                                v.transitionName="huaban_recommend"
                                var huaban=recommend.get(huaban_recommend.getChildAdapterPosition(v))
                                val intent = Intent(instance, HuaBanActivity::class.java)
                                //获取intent对象
                                intent.putExtra("pin_id",huaban.pin_id)
                                intent.putExtra("key",huaban.file.key)
                                intent.putExtra("raw_text",huaban.raw_text)
                                intent.putExtra("recommend",recommendNumber+1)
                                intent.putExtra("height", ScreenUtils.getScreenWidth(instance)*huaban.file.height/huaban.file.width)
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(instance, v, "huaban_recommend"+(recommendNumber+1).toString()).toBundle())
                            }
                        })
                    }
                }
            }
            override fun onError() {
            }
        })
    }
}

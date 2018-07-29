package com.nightfeed.wendu.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.nightfeed.wendu.R
import com.nightfeed.wendu.adapter.ZhiHuListAdapter
import com.nightfeed.wendu.fragment.MainMenuFragment
import com.nightfeed.wendu.model.ZhiHu
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.view.flowingdrawer.FlowingView
import com.nightfeed.wendu.view.flowingdrawer.LeftDrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    val instance by lazy { this }
    var mLeftDrawerLayout: LeftDrawerLayout?=null
    private var mLayoutManager : LinearLayoutManager= LinearLayoutManager(instance)
    private var date=""
    private var zhihuList :MutableList<ZhiHu> = ArrayList<ZhiHu>()
    private var mAdapter: ZhiHuListAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(instance,BrowsePictureActivity::class.java))
        }
        mLeftDrawerLayout = findViewById<View>(R.id.main_drawerlayout) as LeftDrawerLayout

        var mMenuFragment = MainMenuFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_menu, mMenuFragment ).commit()
        val mFlowingView = findViewById<View>(R.id.main_flowing) as FlowingView

        mLeftDrawerLayout!!.setFluidView(mFlowingView)
        mLeftDrawerLayout!!.setMenuFragment(mMenuFragment)

        toolbar.setNavigationOnClickListener { mLeftDrawerLayout!!.toggle() }

        zhihu_list.layoutManager=mLayoutManager

        RequestUtils.get(URLs.ZHIHU_LATEST,object:RequestUtils.OnResultListener{
            override fun onSuccess(result: String) {

                if(!TextUtils.isEmpty(result)){
                    var jsonObject= JSONObject(result)
                    date=MyJSON.getString(jsonObject,"date")
                    var list=jsonObject.getJSONArray("stories")
                    if(list!=null&&list.length()>0){
                        for (i in 0..(list.length()-1)){
                            var z=list.getJSONObject(i)
                            var ims=MyJSON.getJSONArray(z,"images")
                            var im=""
                            if(ims!=null&&ims.length()>0){
                                im=ims.getString(0)
                            }
                            zhihuList.add(ZhiHu(z.getString("id"),z.getString("title"),im))
                        }
                        if(mAdapter==null){
                            zhihu_list.adapter=ZhiHuListAdapter(instance,zhihuList,object : ZhiHuListAdapter.OnClickListener{
                                override fun onClick(v: ZhiHuListAdapter.ZhiHuViewHolder) {
                                    var zhihu=zhihuList.get(zhihu_list.getChildAdapterPosition(v.itemView))
                                    var toDetail=Intent(instance, ZhiHuActivity::class.java)

                                    toDetail.putExtra("image_url",zhihu.image)
                                    toDetail.putExtra("id",zhihu.id)
                                    toDetail.putExtra("title",zhihu.title)
                                    startActivity(toDetail)
                                }

                            })
                        }else{
                            mAdapter!!.notifyRangeInserted(zhihuList,zhihuList.size-list.length(),list.length())
                        }
                    }
                }
            }

            override fun onError() {
            }
        })
    }


    override fun onBackPressed() {

        if (mLeftDrawerLayout!!.isShownMenu()) {
            mLeftDrawerLayout!!.toggle()
        } else {
            super.onBackPressed()
        }
    }
}

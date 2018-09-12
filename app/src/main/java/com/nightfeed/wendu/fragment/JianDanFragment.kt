package com.nightfeed.wendu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.ZhiHuActivity
import com.nightfeed.wendu.adapter.JianDanListAdapter
import com.nightfeed.wendu.adapter.ZhiHuListAdapter
import com.nightfeed.wendu.model.ZhiHu
import com.nightfeed.wendu.model.ZhiHuDB
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import org.litepal.LitePal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class JianDanFragment : BaseFragment() {
    val instance by lazy { this }

    private var viewJianDan: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var jiandanList :MutableList<String> = ArrayList<String>()
    private var mAdapter: JianDanListAdapter?=null

    private var themes =""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewJianDan==null){
            viewJianDan=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewJianDan
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState?.getSerializable("themes") != null) {


            themes = savedInstanceState.getSerializable("themes") as String
        }
        if(image_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_list.layoutManager=mLayoutManager

            image_list_swipe_refresh.setOnRefreshListener {
                jiandanList.clear()
                if(mAdapter!=null){
                    mAdapter?.notifyDataSetChanged()
                }
                getLatest()
            }
        }
        isPrepared=true
        lazyLoad()
    }

    public fun setThemes(t:String):JianDanFragment{
        themes=t
        val bundle = Bundle()
        bundle.putSerializable("themes", themes)
        return instance
    }

    private fun getLatest() {
        RequestUtils.get(URLs.JIANDAN_HOT+themes, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {

                if (!TextUtils.isEmpty(result)) {
                    var jsonObject = JSONObject(result)

                    var list = jsonObject.getJSONArray("comments")
                    if (list != null && list.length() > 0) {
                        if(TextUtils.equals("joke",themes)){
                            for (i in 0..(list.length() - 1)) {
                                jiandanList.add(list.getJSONObject(i).getString("text_content"))
                            }
                            updateList(0)
                        }else{
                            for (i in 0..(list.length() - 1)) {
                                var pics=list.getJSONObject(i).getJSONArray("pics")
                                for (i in 0..(pics.length() - 1)) {
                                    jiandanList.add(pics.getString(i))
                                }
                            }
                            updateList(1)
                        }
                    }
                }
            }

            override fun onError() {
            }
        })
    }


    private fun updateList(type :Int) {
        if (mAdapter == null) {
            mAdapter= JianDanListAdapter(context,type, jiandanList)
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyDataSetChanged()
        }

        image_list_swipe_refresh?.isRefreshing=false
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||jiandanList.size>0) {
            return
        }

        image_list_swipe_refresh?.isRefreshing=true

        getLatest()
    }
}


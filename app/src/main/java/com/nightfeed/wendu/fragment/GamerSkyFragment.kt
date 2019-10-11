package com.nightfeed.wendu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.WebActivity
import com.nightfeed.wendu.adapter.FunnyListAdapter
import com.nightfeed.wendu.adapter.PMListAdapter
import com.nightfeed.wendu.model.GameSkyFunny
import com.nightfeed.wendu.model.WoShiPM
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONObject
import java.util.*


class GamerSkyFragment : BaseFragment() {

    val instance by lazy { this }

    private var view1: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var funnyList :MutableList<GameSkyFunny> = ArrayList<GameSkyFunny>()
    private var mAdapter: FunnyListAdapter?=null
    private var clickPosition=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(view1==null){
            view1=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return view1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(image_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_list.layoutManager=mLayoutManager

            image_list_swipe_refresh.setOnRefreshListener {
                funnyList.clear()
                if(mAdapter!=null){
                    mAdapter?.notifyDataSetChanged()
                }
                getList()
            }
        }
        isPrepared=true
        lazyLoad()
    }

    private fun getList() {
        var  body = JSONObject("{\"os\":\"android\",\"app\":\"GSAPP\",\"osVersion\":\"6.0.1\",\"deviceType\":\"A0001\",\"appVersion\":\"4.8.7\",\"deviceId\":\"864587029916013\",\"request\":{\"nodeIds\":81,\"elementsCountPerPage\":20,\"parentNodeId\":\"dingyue\",\"type\":\"dingyueList\",\"pageIndex\":1}}")

        RequestUtils.post(URLs.GAMESKY_FUNNY,body, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                funnyList = Gson().fromJson( MyJSON.getString(result, "result"), object : TypeToken<List<GameSkyFunny>>() {}.type)
                funnyList.removeAt(0)
                if(funnyList.size>0){
                    updateList()
                }
                image_list_swipe_refresh?.isRefreshing=false
            }

            override fun onError() {
                image_list_swipe_refresh?.isRefreshing=false
            }
        })
    }



    private fun updateList() {
        if (mAdapter == null) {
            mAdapter= FunnyListAdapter(context, funnyList, object : FunnyListAdapter.OnClickListener {
                override fun onClick(v: FunnyListAdapter.FunnyViewHolder) {

                    clickPosition=image_list.getChildAdapterPosition(v.itemView)

                    var funny = funnyList.get(clickPosition)
                    var toDetail = Intent(context, WebActivity::class.java)
                    toDetail.putExtra("url",URLs.GAMESKY_FUNNY_CONTENT+funny.contentId+".html")
                    toDetail.putExtra("title", funny.title)

                    activity!!.startActivityForResult(toDetail,122)
                }
            })
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyDataSetChanged()
        }

        image_list_swipe_refresh?.isRefreshing=false
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||funnyList.size>0) {
            return
        }

        image_list_swipe_refresh?.isRefreshing=true

        getList()
    }

}


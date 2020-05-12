package com.nightfeed.wendu.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.WebActivity
import com.nightfeed.wendu.adapter.Amusepage3dmListAdapter
import com.nightfeed.wendu.adapter.FunnyListAdapter
import com.nightfeed.wendu.model.Amusepage3dm
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONObject
import java.util.*


class Game3DMFragment : BaseFragment() {

    val instance by lazy { this }

    private var view1: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var funnyList :MutableList<Amusepage3dm> = ArrayList<Amusepage3dm>()
    private var mAdapter: Amusepage3dmListAdapter?=null
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
        var  body = JSONObject("{\"type\":\"65\",\"pagesize\":10,\"page\":1,\"time\":1578294173662,\"sign\":\"01f61d2ee72ceebc54043aea82ad0a59\"}")
        RequestUtils.post(URLs.GAME_3DM_AMUSEPAGE,body, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                funnyList = Gson().fromJson( MyJSON.getJSONObject(result, "data")?.getString("list"), object : TypeToken<List<Amusepage3dm>>() {}.type)
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
            mAdapter= Amusepage3dmListAdapter(context, funnyList, object : Amusepage3dmListAdapter.OnClickListener {
                override fun onClick(v: Amusepage3dmListAdapter.AmusepageViewHolder) {

                    clickPosition=image_list.getChildAdapterPosition(v.itemView)

                    var funny = funnyList.get(clickPosition)
                    var toDetail = Intent(context, WebActivity::class.java)
                    toDetail.putExtra("url",funny.webviewurl)
                    toDetail.putExtra("title", funny.title)

                    activity!!.startActivity(toDetail)
                }
            })
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyDataSetChanged()
        }

        image_list_swipe_refresh?.isRefreshing=false
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||funnyList.size>0||image_list_swipe_refresh==null) {
            return
        }

        image_list_swipe_refresh.isRefreshing=true

        getList()
    }

}


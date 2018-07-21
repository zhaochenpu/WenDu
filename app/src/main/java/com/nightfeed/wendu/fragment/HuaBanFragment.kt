package com.nightfeed.wendu.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.nightfeed.wendu.R
import com.nightfeed.wendu.adapter.ImageListAdapter
import com.nightfeed.wendu.model.HuaBan
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.view.MyStaggeredGridLayoutManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.image_fragment.*
import java.util.ArrayList


class HuaBanFragment : BaseFragment() {

    private var isPrepared=false
    private var viewHuaban : View? =null
    private var mAdapter: ImageListAdapter?=null
    private var huaBanList :MutableList<HuaBan> = ArrayList<HuaBan>()
    private var lastVisibleItem: Int = 0
    var mLayoutManager :StaggeredGridLayoutManager?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewHuaban==null){
            viewHuaban=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewHuaban
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if( image_list.layoutManager==null){
            mLayoutManager = MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mLayoutManager?.gapStrategy=StaggeredGridLayoutManager.GAP_HANDLING_NONE
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
//                    mLayoutManager.invalidateSpanAssignments()
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&huaBanList.size>0) {
                        getListDataMore()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val positions = mLayoutManager!!.findLastVisibleItemPositions(null)
                    lastVisibleItem = Math.max(positions[0], positions[1])
                }
            })

            image_list_swipe_refresh.setOnRefreshListener { getListData() }

        }
        isPrepared=true

        lazyLoad()
    }


    override fun lazyLoad() {
        if (!isPrepared || !isVisible||huaBanList.size>0) {
            return
        }
        image_list_swipe_refresh.isRefreshing=true
        getListData()
    }

    private fun getListDataMore() {
        if(huaBanList.size>0){
            RequestUtils.get(URLs.HUA_BAN_BEAUTY_LIST+"&max="+huaBanList.last().pin_id, object : RequestUtils.OnResultListener {
                override fun onSuccess(result: String) {
                    var pins = MyJSON.getString(result, "pins")
                    if (!TextUtils.isEmpty(pins)) {
                        var huaBanListMore:ArrayList<HuaBan> = Gson().fromJson(pins, object : TypeToken<List<HuaBan>>() {}.type)
                        if (huaBanListMore.size>0) {
                            var start=huaBanList.size
                            huaBanList.addAll(huaBanList)
                            mAdapter!!.notifyRangeInserted(huaBanList,start,huaBanListMore.size)
                        }
                    }

                }

                override fun onError() {
                }
            })
        }
    }

    fun getListData(){
        RequestUtils.get(URLs.HUA_BAN_BEAUTY_LIST, object : RequestUtils.OnResultListener{
            override fun onSuccess(result: String) {
                var pins= MyJSON.getString(result ,"pins")
                if(!TextUtils.isEmpty(pins)){
                    huaBanList = Gson().fromJson(pins, object : TypeToken<List<HuaBan>>(){}.type)
                    if(mAdapter==null){
                        mAdapter= ImageListAdapter(context, huaBanList)
                        image_list.adapter=mAdapter
                    }else{
                        mAdapter!!.notifyDataChanged(huaBanList)
                    }
                }
                image_list_swipe_refresh.isRefreshing=false
            }

            override fun onError() {
                image_list_swipe_refresh.isRefreshing=false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }


}


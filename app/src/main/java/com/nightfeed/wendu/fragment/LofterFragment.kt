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
import com.nightfeed.wendu.adapter.LofterListAdapter
import com.nightfeed.wendu.model.Lofter
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.image_fragment.*
import java.util.ArrayList


class LofterFragment : BaseFragment() {


    val instance by lazy { this }

    private var isPrepared=false
    private var mAdapter: LofterListAdapter?=null
    private var lofterList :MutableList<Lofter> = ArrayList<Lofter>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    private var label:String?=null
    private var page=1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        isPrepared=true
        return inflater.inflate(R.layout.image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            label = savedInstanceState.getSerializable("label") as String
        }

        image_list.layoutManager=mLayoutManager

        image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager.itemCount&&lofterList.size>0) {
                    getListData()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val positions = mLayoutManager.findLastVisibleItemPositions(null)
                lastVisibleItem = Math.max(positions[0], positions[1])
            }
        })

        image_list_swipe_refresh.setOnRefreshListener {
            page=1
            lofterList.clear()
            getListData() }

        lazyLoad()
    }

    public fun setLabel (l:String) :LofterFragment{
        label=l
        return instance
    }

    private fun getListData() {
        RequestUtils.get(URLs.LOFTER_LEST + page+"&labelId="+label, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var value = MyJSON.getString(result, "value")
                if (!TextUtils.isEmpty(value)) {
                   var lsit :ArrayList<Lofter> = Gson().fromJson(value, object : TypeToken<List<Lofter>>() {}.type)
                    if(lsit!=null&&lsit.size>0){
                        lofterList.addAll(lsit)
                        if (mAdapter == null) {
                            mAdapter = LofterListAdapter(context, lofterList)
                            image_list.adapter = mAdapter
                        } else {
                            if(page==1){
                                mAdapter!!.notifyDataChanged(lofterList)
                            }else{
                                mAdapter!!.notifyRangeInserted(lofterList,lofterList.size-lsit.size,lsit.size)
                            }
                        }

                        page++
                    }
                }
                image_list_swipe_refresh.isRefreshing=false
            }

            override fun onError() {
                image_list_swipe_refresh.isRefreshing=false
            }
        })
    }


    override fun lazyLoad() {
        if (!isPrepared || !isVisible||lofterList.size>0) {
            return
        }
        image_list_swipe_refresh.isRefreshing=true
        page=1
        getListData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }


}

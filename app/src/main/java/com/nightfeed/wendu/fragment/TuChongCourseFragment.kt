package com.nightfeed.wendu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.WebActivity
import com.nightfeed.wendu.adapter.TuChongCourseListAdapter
import com.nightfeed.wendu.model.OneSentence
import com.nightfeed.wendu.model.TuChongCourse
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_fragment.*
import java.util.*


class TuChongCourseFragment : BaseFragment() {

    val instance by lazy { this }

    private var viewPM: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var lastVisibleItem: Int = 0
    private var tcList :MutableList<TuChongCourse> = ArrayList<TuChongCourse>()
    private var mAdapter: TuChongCourseListAdapter?=null
    private var clickPosition=0
    private var page=1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewPM==null){
            viewPM=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewPM
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(image_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&tcList.size>0) {
                        page++
                        getList()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    lastVisibleItem = mLayoutManager!!.findLastVisibleItemPosition()
                }
            })

            image_list_swipe_refresh.setOnRefreshListener {
                page=1
                tcList.clear()
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
        RequestUtils.get(URLs.TUCHONG_COURSE+page, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var posts =MyJSON.getJSONArray(result, "posts")
                var list =ArrayList<TuChongCourse>()
                if(posts==null){
                    return
                }
                for (i in 0 until posts.length()){
                    var data=posts.getJSONObject(i)

                    list.add(TuChongCourse(data.getString("url"),data.getString("title"),data.getJSONObject("title_image").getString("url")))
                }

                if(list.size>0){
                    tcList.addAll(list)

                    updateList(list)
                }
                image_list_swipe_refresh?.isRefreshing=false
            }

            override fun onError() {
                image_list_swipe_refresh?.isRefreshing=false
            }
        })
    }



    private fun updateList(list :ArrayList<TuChongCourse>) {
        if (mAdapter == null) {
            mAdapter= TuChongCourseListAdapter(context, tcList, object : TuChongCourseListAdapter.OnClickListener {
                override fun onClick(v: TuChongCourseListAdapter.TViewHolder) {

                    clickPosition=image_list.getChildAdapterPosition(v.itemView)

                    var pm = tcList.get(clickPosition)
                    var toDetail = Intent(context, WebActivity::class.java)

                    toDetail.putExtra("url", pm.link)
                    toDetail.putExtra("title", pm.title)

                    activity!!.startActivityForResult(toDetail,122)
                }

            })
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyRangeInserted(tcList, tcList.size - list.size, list.size)
        }

        image_list_swipe_refresh?.isRefreshing=false
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||tcList.size>0) {
            return
        }

        image_list_swipe_refresh?.isRefreshing=true

        getList()
    }
}


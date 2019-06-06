package com.nightfeed.wendu.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.WebActivity
import com.nightfeed.wendu.activity.ZhiHuActivity
import com.nightfeed.wendu.adapter.PMListAdapter
import com.nightfeed.wendu.adapter.ZhiHuListAdapter
import com.nightfeed.wendu.model.JuDu
import com.nightfeed.wendu.model.WoShiPM
import com.nightfeed.wendu.model.ZhiHu
import com.nightfeed.wendu.model.ZhiHuDB
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import org.litepal.LitePal
import java.util.*


class WoShiPMHotFragment : BaseFragment() {

    val instance by lazy { this }

    private var viewPM: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var lastVisibleItem: Int = 0
    private var pmList :MutableList<WoShiPM> = ArrayList<WoShiPM>()
    private var mAdapter: PMListAdapter?=null
    private var clickPosition=0
    private var page=1
    private var url=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewPM==null){
            viewPM=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewPM
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState?.getSerializable("url") != null) {

            url = savedInstanceState.getSerializable("url") as String
        }

        if(image_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&pmList.size>0) {
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
                pmList.clear()
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
        RequestUtils.get(url+page, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var news =MyJSON.getString(MyJSON.getJSONObject(result,"RESULT")!!, "news")
                var list :ArrayList<WoShiPM> = Gson().fromJson( news, object : TypeToken<List<WoShiPM>>() {}.type)

                if(list.size>0){
                    pmList.addAll(list)

                    updateList(list)
                }
                image_list_swipe_refresh?.isRefreshing=false
            }

            override fun onError() {
                image_list_swipe_refresh?.isRefreshing=false
            }
        })
    }



    private fun updateList(list :ArrayList<WoShiPM>) {
        if (mAdapter == null) {
            mAdapter= PMListAdapter(context, pmList, object : PMListAdapter.OnClickListener {
                override fun onClick(v: PMListAdapter.PMViewHolder) {

                    clickPosition=image_list.getChildAdapterPosition(v.itemView)

                    var pm = pmList.get(clickPosition)
                    var toDetail = Intent(context, WebActivity::class.java)

                    toDetail.putExtra("url", pm.link)
                    toDetail.putExtra("title", pm.title)

                    activity!!.startActivityForResult(toDetail,122)
                }

            })
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyRangeInserted(pmList, pmList.size - list.size, list.size)
        }

        image_list_swipe_refresh?.isRefreshing=false
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||pmList.size>0) {
            return
        }

        image_list_swipe_refresh?.isRefreshing=true

        getList()
    }

    public fun setThemes(id:Int):WoShiPMHotFragment{
        if(id==1){
            url=URLs.WOSHIPM_WEEKHOT
        }else{
            url=URLs.WOSHIPM_MONTHHOT
        }
        val bundle = Bundle()
        bundle.putSerializable("url", url)
        return instance
    }

}


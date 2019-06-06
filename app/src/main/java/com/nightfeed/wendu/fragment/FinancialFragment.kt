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
import com.nightfeed.wendu.adapter.FinancialListAdapter
import com.nightfeed.wendu.adapter.PMListAdapter
import com.nightfeed.wendu.model.Finance
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONObject
import java.util.*


class FinancialFragment : BaseFragment() {

    val instance by lazy { this }

    private var viewFinancial: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var lastVisibleItem: Int = 0
    private var financeList :MutableList<Finance> = ArrayList<Finance>()
    private var mAdapter: FinancialListAdapter?=null
    private var clickPosition=0
    private var condition=""
    private var type = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewFinancial==null){
            viewFinancial=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewFinancial
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState?.getSerializable("type") != null) {

            type = savedInstanceState.getSerializable("type") as Int
        }

        if(image_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&financeList.size>0) {
                        getList()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    lastVisibleItem = mLayoutManager!!.findLastVisibleItemPosition()
                }
            })

            image_list_swipe_refresh.setOnRefreshListener {
                condition=""
                financeList.clear()
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
        var  body : JSONObject= JSONObject()
        body.put("appKey","")
        body.put("client","android")
        body.put("clientType","cft")
        body.put("clientVersion","8.0.2")

        body.put("reserve","{deviceId=2d40d2346d4ff24227e0ff71f160c17c, deviceSysVersion=ONEPLUS A0001 6.0.1, uToken=, cToken=, pi=}")
        body.put("timeStamp", System.currentTimeMillis().toString())

        var  args : JSONObject= JSONObject()
        args.put("condition",condition)
        args.put("pageSize",20)

        var url=""
        if(type==0){
            url=URLs.FINANCIAL_NEWS

            body.put("method","importantNews")
            body.put("randomCode","1982684799")

        }else{
            url=URLs.FINANCIAL_OPTIONAL

            body.put("randomCode","1341524076")
            body.put("method","advise.portfolioInfo")

            args.put("fixedTime",true)
            args.put("infoType",0)
            args.put("securities","000725_0,002594_0,002230_0,000063_0,000333_0")
        }

        body.put("args",args)

        RequestUtils.post(url,body, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var data=MyJSON.getJSONObject(result,"data")
                condition=MyJSON.getString(data!!, "condition")

                var list :ArrayList<Finance> = Gson().fromJson( MyJSON.getString(data!!, "items"), object : TypeToken<List<Finance>>() {}.type)

                if(list.size>0){
                    financeList.addAll(list)

                    updateList(list)
                }
                image_list_swipe_refresh?.isRefreshing=false
            }

            override fun onError() {
                image_list_swipe_refresh?.isRefreshing=false
            }
        })
    }



    private fun updateList(list :ArrayList<Finance>) {
        if (mAdapter == null) {
            mAdapter= FinancialListAdapter(context, financeList, object : FinancialListAdapter.OnClickListener {
                override fun onClick(v: FinancialListAdapter.FinancialViewHolder) {

                    clickPosition=image_list.getChildAdapterPosition(v.itemView)

                    var finance = financeList.get(clickPosition)
                    var toDetail = Intent(context, WebActivity::class.java)

                    toDetail.putExtra("url", URLs.FINANCIAL_DETAIL+finance.code)
                    toDetail.putExtra("title", finance.title)

                    activity!!.startActivityForResult(toDetail,122)
                }

            })
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyRangeInserted(financeList, financeList.size - list.size, list.size)
        }

        image_list_swipe_refresh?.isRefreshing=false
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||financeList.size>0) {
            return
        }

        image_list_swipe_refresh?.isRefreshing=true

        getList()
    }

    public fun setType(type:Int):FinancialFragment{
        this.type=type

        val bundle = Bundle()
        bundle.putSerializable("type", type)
        return instance
    }

}


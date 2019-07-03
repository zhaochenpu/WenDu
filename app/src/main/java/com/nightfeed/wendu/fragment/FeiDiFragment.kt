package com.nightfeed.wendu.fragment

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
import com.nightfeed.wendu.activity.ImageWordActivity
import com.nightfeed.wendu.adapter.JuDuListAdapter
import com.nightfeed.wendu.adapter.OneSentenceListAdapter
import com.nightfeed.wendu.model.JuDu
import com.nightfeed.wendu.model.OneSentence
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_word_fragment.*
import java.util.ArrayList


class FeiDiFragment : BaseFragment() {

    val instance by lazy { this }

    private var viewSebtence: View? =null
    private var isPrepared=false
    private var mAdapter: OneSentenceListAdapter?=null
    private var sentenceList :MutableList<OneSentence> = ArrayList<OneSentence>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null
    private var page=1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewSebtence==null){
            viewSebtence=inflater.inflate(R.layout.image_word_fragment, container, false)
        }
        return viewSebtence
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(image_word_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_word_list.layoutManager=mLayoutManager

            image_word_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 3  >= mLayoutManager!!.itemCount&&sentenceList.size>0) {
                        getListData()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    lastVisibleItem = mLayoutManager!!.findLastVisibleItemPosition()
                }
            })
        }
        isPrepared=true
        lazyLoad()
    }


    private fun getListData() {

        RequestUtils.get(URLs.FEI_DI+page, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var datas=MyJSON.getJSONArray(MyJSON.getJSONObject(result,"result"), "data")

                if(datas!=null){
                    for (i in 0 until datas.length()){
                        var data=datas.getJSONObject(i)
                        sentenceList.add(OneSentence(data.getString("thumb"),data.getString("content"),data.getString("author")))
                    }

                    if(mAdapter==null){
                        mAdapter= OneSentenceListAdapter(context,sentenceList)
                        image_word_list.adapter=mAdapter

                        (context as ImageWordActivity).setHeadImage(sentenceList[0].img_url)
                    }else{
                        mAdapter!!.notifyRangeInserted(sentenceList,sentenceList.size-datas.length(),datas.length())
                    }

                    page++
                }

            }

            override fun onError() {

            }
        })

    }

    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible) {
            return
        }

        if(context!=null&&sentenceList.size>0){
            (context as ImageWordActivity).setHeadImage(sentenceList[0].img_url)
        }

        if(sentenceList.size<=0){
            getListData()
        }
    }

}


package com.nightfeed.wendu.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.ImageWordActivity
import com.nightfeed.wendu.adapter.LofterListAdapter
import com.nightfeed.wendu.adapter.OneSentenceListAdapter
import com.nightfeed.wendu.model.OneSentence
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.image_word_fragment.*
import java.util.ArrayList


class OneSentenceFragment : BaseFragment() {


    private var viewSebtence: View? =null
    private var isPrepared=false
    private var mAdapter: OneSentenceListAdapter?=null
    private var sentenceList :MutableList<OneSentence> = ArrayList<OneSentence>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null
    private var newest:Int=0
    private val pageItem=10
    private var get=0


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

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&sentenceList.size>0) {
                        getItem()
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

    private fun getNewest() {
        RequestUtils.get(URLs.ONE_NEW, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var value = MyJSON.getJSONArray(result, "data")
                if(value!=null&&value.length()>0){
                    try {
                        newest=Integer.parseInt(value.getString(0))
                        getItem()
                    }catch (e :Exception ){

                    }
                }
            }

            override fun onError() {

            }
        })
    }

    private fun getItem() {
        var i=0
        var itemNow=sentenceList.size
        while( i< pageItem) {
            var hp_id=newest-itemNow-i
            if(hp_id==1){
                get=pageItem-2
            }else if(hp_id<1){
                ToastUtil.showShort(context,"厉害了！都被你看完了")
            }
            RequestUtils.get(URLs.ONE_HP.replace("hp_id",(newest-itemNow-i).toString()), object : RequestUtils.OnResultListener {
                override fun onSuccess(result: String) {
                    get++
                    var data = MyJSON.getString(result, "data")
                    if(!TextUtils.isEmpty(data)){
                        var oneSentence= Gson().fromJson(data,OneSentence::class.java)
                        if(oneSentence!=null){
                            sentenceList.add(oneSentence)
                        }
                    }

                    if(get==pageItem-1){
                        if(mAdapter==null){
                            (context as ImageWordActivity).setHeadImage(sentenceList.get(0).hp_img_url)

                            mAdapter= OneSentenceListAdapter(context,sentenceList)
                            image_word_list.adapter=mAdapter
                        }else{
                            mAdapter!!.notifyRangeInserted(sentenceList,itemNow,sentenceList.size-itemNow)
                        }
                        get=0
                    }
                }
                override fun onError() {
                    get++
                    if(get==pageItem-1){
                        if(mAdapter==null){
                            mAdapter= OneSentenceListAdapter(context,sentenceList)
                            image_word_list.adapter=mAdapter
                        }else{
                            mAdapter!!.notifyRangeInserted(sentenceList,itemNow,sentenceList.size-itemNow)
                        }
                        get=0
                    }
                }
            })
            i++
        }
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible) {
            return
        }

        if(context!=null&&sentenceList.size>0){
            (context as ImageWordActivity).setHeadImage(sentenceList.get(0).hp_img_url)
        }

        if(sentenceList.size<=0){
            if(newest==0){
                getNewest()
            }else{
                getItem()
            }
        }
    }

}


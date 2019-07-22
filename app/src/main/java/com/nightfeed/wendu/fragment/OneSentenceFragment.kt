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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class OneSentenceFragment : BaseFragment() {


    private var viewSebtence: View? =null
    private var isPrepared=false
    private var mAdapter: OneSentenceListAdapter?=null
    private var sentenceList :MutableList<OneSentence> = ArrayList<OneSentence>()
    private var newList :MutableList<OneSentence> = ArrayList<OneSentence>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null
    private val pageItem=10
    private var get=0
//    private var itemNow=0
    var dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
    private var lastDate = Date()
    var calendar = GregorianCalendar.getInstance()


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


    private fun getItem() {
        var i=0

        while( i< pageItem) {

            RequestUtils.get(URLs.ONE_HP.replace("TIME",dateFormat.format(calendar.time)), object : RequestUtils.OnResultListener {
                override fun onSuccess(result: String) {
                    get++
                    var data = MyJSON.getString(result, "data")
                    if(!TextUtils.isEmpty(data)){
                        var oneSentence= Gson().fromJson(data,OneSentence::class.java)
                        if(oneSentence!=null){
                            newList.add(oneSentence)
                        }
                    }

                    if(get==pageItem-1){
                        sentenceList.addAll(newList)

                        if(mAdapter==null){
                            (context as ImageWordActivity).setHeadImage(sentenceList.get(0).img_url)

                            mAdapter= OneSentenceListAdapter(context,sentenceList)
                            image_word_list.adapter=mAdapter
                        }else{
                            mAdapter!!.notifyRangeInserted(sentenceList,sentenceList.size-newList.size,newList.size)
                        }
                        newList.clear()
                        get=0
                    }
                }
                override fun onError() {
                    get++
                    if(get==pageItem-1){
                        sentenceList.addAll(newList)
                        if(mAdapter==null){
                            mAdapter= OneSentenceListAdapter(context,sentenceList)
                            image_word_list.adapter=mAdapter
                        }else{
                            mAdapter!!.notifyRangeInserted(sentenceList,sentenceList.size-newList.size,newList.size)
                        }
                        newList.clear()
                        get=0
                    }
                }
            })

            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)-1)

            i++
        }
        lastDate=calendar.time
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible) {
            return
        }

        if(context!=null&&sentenceList.size>0){
            (context as ImageWordActivity).setHeadImage(sentenceList.get(0).img_url)
        }

        if(sentenceList.size<=0){
            getItem()
        }
    }

}


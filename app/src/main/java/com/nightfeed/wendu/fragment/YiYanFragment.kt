package com.nightfeed.wendu.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.ImageWordActivity
import com.nightfeed.wendu.adapter.OneSentenceListAdapter
import com.nightfeed.wendu.model.OneSentence
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_word_fragment.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class YiYanFragment : BaseFragment() {


    val instance by lazy { this }

    private var viewHuaban: View? =null
    private var isPrepared=false
    private var mAdapter: OneSentenceListAdapter?=null
    private var sentenceList :MutableList<OneSentence> = ArrayList<OneSentence>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null
    private var page=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewHuaban==null){
            viewHuaban=inflater.inflate(R.layout.image_word_fragment, container, false)
        }
        return viewHuaban
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(image_word_list.layoutManager==null){
            mLayoutManager= LinearLayoutManager(context)
            image_word_list.layoutManager=mLayoutManager

            image_word_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 4 >= mLayoutManager!!.itemCount&&sentenceList.size>0) {
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
        var header:Array<String> = arrayOf("Cookie", "JSESSIONID=330D194E67991831377CBF632911F658")
        var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var calendar=Calendar.getInstance()
        calendar.add(Calendar. DATE,1-page)

        var url=URLs.YIYAN_SENTENCE+ dateFormat.format(calendar.time)+"%2000%3A00%3A00"
        RequestUtils.get(url, header,object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                page++
                var text_list = MyJSON.getJSONArray(result, "textcardlist")
                if (text_list!=null&&text_list.length()>0) {
                    var i=0
                    var newList=ArrayList<OneSentence>()
                   while (i<text_list.length()){
                       var meow=text_list.getJSONObject(i)
                       var content=MyJSON.getString(meow,"content")
                       if (!TextUtils.isEmpty(content.trim())){
                           var sentence=OneSentence(meow.getString("picpath"),content,MyJSON.getString(meow,"from"))
                           newList.add(sentence)
                       }
                       i++
                   }
                    sentenceList.addAll(newList)

                    if(image_word_list.adapter==null){
                        (context as ImageWordActivity).setHeadImage(sentenceList[0].img_url)
                        mAdapter= OneSentenceListAdapter(context,sentenceList)
                        image_word_list.adapter=mAdapter
                    }else{
                        mAdapter!!.notifyRangeInserted(sentenceList,sentenceList.size-newList.size,newList.size)
                    }
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

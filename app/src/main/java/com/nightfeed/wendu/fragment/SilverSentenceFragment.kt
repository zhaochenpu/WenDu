package com.nightfeed.wendu.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


class SilverSentenceFragment : BaseFragment() {


    private var viewSebtence: View? =null
    private var isPrepared=false
    private var mAdapter: OneSentenceListAdapter?=null
    private var sentenceList :MutableList<OneSentence> = ArrayList<OneSentence>()

    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null


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
        RequestUtils.get(URLs.SOLVER_SENTENCE+sentenceList.size, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {

                var data = MyJSON.getJSONObject(result, "data")?.getJSONObject("feed_list")?.getJSONArray("list")

                if(data!=null){
                    for ( i in 0 until data.length()){
                        var item=data.getJSONObject(i)
                        var content=item.getString("content").split("——")
                        var images=""
                        try {
                            images=item.getJSONArray("images").getJSONObject(0).getString("url")
                        }catch (e:Exception){

                        }
                        if (content.size>1){
                            sentenceList.add(OneSentence(images,content[0],content[1]))
                        }else{
                            sentenceList.add(OneSentence(images,content[0],""))
                        }
                    }

                    if(mAdapter==null){
                        (context as ImageWordActivity).setHeadImage(sentenceList.get(0).img_url)

                        mAdapter= OneSentenceListAdapter(context,sentenceList)
                        image_word_list.adapter=mAdapter
                    }else{
                        mAdapter!!.notifyRangeInserted(sentenceList,sentenceList.size-data.length(),data.length())
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
            (context as ImageWordActivity).setHeadImage(sentenceList.get(0).img_url)
        }

        if(sentenceList.size<=0){
            getItem()
        }
    }

}


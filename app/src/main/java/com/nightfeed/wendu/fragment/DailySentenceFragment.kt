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
import com.nightfeed.wendu.activity.ENSentenceActivity
import com.nightfeed.wendu.activity.ImageWordActivity
import com.nightfeed.wendu.adapter.ENSentenceListAdapter
import com.nightfeed.wendu.model.ENSentence
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_word_fragment.*
import java.util.ArrayList


class DailySentenceFragment : BaseFragment() {


    private var viewSebtence: View? =null
    private var isPrepared=false
    private var mAdapter: ENSentenceListAdapter?=null
    private var sentenceList :MutableList<ENSentence> = ArrayList<ENSentence>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null

    private var url=""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewSebtence==null){
            viewSebtence=inflater.inflate(R.layout.image_word_fragment, container, false)
        }
        return viewSebtence
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState?.getSerializable("url") != null) {

            url = savedInstanceState.getSerializable("url") as String
        }
        if(image_word_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_word_list.layoutManager=mLayoutManager

            image_word_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&sentenceList.size>0) {
                        getListData(url+sentenceList.last().id)
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

    public fun setURL (l:String) :DailySentenceFragment{
        url=l
        val bundle = Bundle()
        bundle.putSerializable("url", l)
        return this
    }

    private fun getListData(api:String) {
        RequestUtils.get(api, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var lsit :ArrayList<ENSentence> = Gson().fromJson( MyJSON.getString(result, "cards"), object : TypeToken<List<ENSentence>>() {}.type)
                if(lsit.size>0){
                    sentenceList.addAll(lsit)
                    if(mAdapter==null){
                        mAdapter=ENSentenceListAdapter(context,sentenceList)
                        image_word_list.adapter=mAdapter

                        if(TextUtils.equals(url,URLs.YOUDAO_DAILY_WORD)){
                            mAdapter?.setOnClickListener(object : ENSentenceListAdapter.OnClickListener{
                                override fun onClick(v: View) {
                                    val intent = Intent(context, ENSentenceActivity::class.java)
                                    //获取intent对象
                                    intent.putExtra("url",sentenceList.get(image_word_list.getChildAdapterPosition(v)).url)
                                    startActivity(intent)
                                }
                            })
                        }

                        (context as ImageWordActivity).setHeadImage(sentenceList[0].gif[0])
                    }else{
                        mAdapter!!.notifyRangeInserted(sentenceList,sentenceList.size-lsit.size,lsit.size)
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
            (context as ImageWordActivity).setHeadImage(sentenceList[0].gif[0])
        }

        if(sentenceList.size<=0){
            getListData(url+"0")
        }
    }

}


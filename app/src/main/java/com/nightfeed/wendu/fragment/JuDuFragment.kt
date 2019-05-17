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
import com.nightfeed.wendu.adapter.JuDuListAdapter
import com.nightfeed.wendu.model.ENSentence
import com.nightfeed.wendu.model.JuDu
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.image_word_fragment.*
import java.util.ArrayList


class JuDuFragment : BaseFragment() {

    val instance by lazy { this }

    private var viewSebtence: View? =null
    private var isPrepared=false
    private var mAdapter: JuDuListAdapter?=null
    private var sentenceList :MutableList<JuDu> = ArrayList<JuDu>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null
    private var type=0;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewSebtence==null){
            viewSebtence=inflater.inflate(R.layout.image_word_fragment, container, false)
        }
        return viewSebtence
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState?.getSerializable("label") != null) {

            type = savedInstanceState.getSerializable("label") as Int
        }

        if(image_word_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_word_list.layoutManager=mLayoutManager
        }
        isPrepared=true
        lazyLoad()
    }


    private fun getListData() {

        if(type==0){
            RequestUtils.get(URLs.JU_DU, object : RequestUtils.OnResultListener {
                override fun onSuccess(result: String) {
                    var s=MyJSON.getString(result, "data")
                    var lsit :ArrayList<JuDu> = Gson().fromJson( s, object : TypeToken<List<JuDu>>() {}.type)
                    if(lsit.size>0){
                        sentenceList.addAll(lsit)
                        if(mAdapter==null){
                            mAdapter=JuDuListAdapter(context,sentenceList)
                            image_word_list.adapter=mAdapter

                            (context as ImageWordActivity).setHeadImage(sentenceList[0].image.url)
                        }else{
                            mAdapter!!.notifyRangeInserted(sentenceList,0,lsit.size)
                        }
                    }
                }

                override fun onError() {

                }
            })
        }else{
            RequestUtils.get(URLs.JU_TUI, object : RequestUtils.OnResultListener {
                override fun onSuccess(result: String) {
                    var lsit :ArrayList<JuDu> = Gson().fromJson( MyJSON.getString(result, "data"), object : TypeToken<List<JuDu>>() {}.type)
                    if(lsit.size>0){
                        var i=0;
                        for (juDu in lsit) {
                            if (!TextUtils.isEmpty(juDu.image?.url)){
                               if (i==0){
                                    break
                                }else{
                                    lsit.add(0,juDu)
                                    lsit.removeAt(i+1)
                                    break
                                }
                            }
                            i++
                        }

                        for (juDu in lsit) {
                            if (TextUtils.isEmpty(juDu.content)){
                                lsit.remove(juDu)
                                break
                            }
                        }


                        sentenceList.addAll(lsit)
                        if(mAdapter==null){
                            mAdapter=JuDuListAdapter(context,sentenceList)
                            image_word_list.adapter=mAdapter

                            (context as ImageWordActivity).setHeadImage(sentenceList[0].image.url)
                        }else{
                            mAdapter!!.notifyRangeInserted(sentenceList,0,lsit.size)
                        }
                    }
                }

                override fun onError() {

                }
            })
        }

    }

    public fun setLabel (t:Int) :JuDuFragment{
        type=t
        val bundle = Bundle()
        bundle.putSerializable("label", t)
        return instance
    }

    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible) {
            return
        }

        if(context!=null&&sentenceList.size>0){
            (context as ImageWordActivity).setHeadImage(sentenceList[0].image.url)
        }

        if(sentenceList.size<=0){
            getListData()
        }
    }

}


package com.nightfeed.wendu.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.ImageWordActivity
import com.nightfeed.wendu.adapter.PoetryListAdapter
import com.nightfeed.wendu.model.Poetry
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.image_word_fragment.*
import java.util.ArrayList


class MonoFragment : BaseFragment() {


    val instance by lazy { this }

    private var viewHuaban: View? =null
    private var isPrepared=false
    private var mAdapter: PoetryListAdapter?=null
    private var poetryList :MutableList<Poetry> = ArrayList<Poetry>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : LinearLayoutManager?= null


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
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 4 >= mLayoutManager!!.itemCount&&poetryList.size>0) {
                        getListData(URLs.MONO_POETRY_MORE+poetryList.last().create_time)
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

    private fun getListData(url:String) {
        var header:Array<String> = arrayOf("HTTP-AUTHORIZATION", "2e5ccb3d7f5211e8a6e55254006fe942")
        RequestUtils.get(url, header,object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var meow_list = MyJSON.getJSONArray(result, "meow_list")
                if (meow_list!=null&&meow_list.length()>0) {
                    var i=0
                   while (i<meow_list.length()){
                       var meow=meow_list.getJSONObject(i)
                       try {

                           var poetry=Poetry(meow.getString("text"),meow.getString("create_time"),MyJSON.getString(meow,"author"),meow.getJSONObject("thumb").getString("raw"))
                           poetryList.add(poetry)
                           i++
                       }catch (e:Exception){
                           meow_list.remove(i)
                       }
                   }

                    if(image_word_list.adapter==null){
                        (context as ImageWordActivity).setHeadImage(poetryList.get(0).im_raw)
                        mAdapter= PoetryListAdapter(context,poetryList)
                        image_word_list.adapter=mAdapter
                    }else{
                        mAdapter!!.notifyRangeInserted(poetryList,poetryList.size-meow_list.length(),meow_list.length())
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

        if(context!=null&&poetryList.size>0){
            (context as ImageWordActivity).setHeadImage(poetryList.get(0).im_raw)
        }

        if(poetryList.size<=0){
            getListData(URLs.MONO_POETRY)
        }
    }

}

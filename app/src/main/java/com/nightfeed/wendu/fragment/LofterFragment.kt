package com.nightfeed.wendu.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.LofterActivity
import com.nightfeed.wendu.adapter.ImageListAdapter
import com.nightfeed.wendu.adapter.LofterListAdapter
import com.nightfeed.wendu.model.Lofter
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ScreenUtils
import com.nightfeed.wendu.view.MyStaggeredGridLayoutManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.image_fragment.*
import java.util.ArrayList


class LofterFragment : BaseFragment() {


    val instance by lazy { this }

    private var viewHuaban: View? =null
    private var isPrepared=false
    private var mAdapter: LofterListAdapter?=null
    private var lofterList :MutableList<Lofter> = ArrayList<Lofter>()
    private var lastVisibleItem: Int = 0
    private var mLayoutManager : MyStaggeredGridLayoutManager?= null

    private var label:String?=null
    private var page=1
    private var imageWidth=""



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewHuaban==null){
            viewHuaban=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewHuaban
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState?.getSerializable("label") != null) {

            label = savedInstanceState.getSerializable("label") as String
        }

        if(image_list.layoutManager==null){
            mLayoutManager= MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mLayoutManager?.gapStrategy=StaggeredGridLayoutManager.GAP_HANDLING_NONE
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 4 >= mLayoutManager!!.itemCount&&lofterList.size>0) {
                        getListData()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val positions = mLayoutManager!!.findLastVisibleItemPositions(null)
                    lastVisibleItem = Math.max(positions[0], positions[1])
                }
            })

            image_list_swipe_refresh.setOnRefreshListener {
                page=1
                lofterList.clear()
                if(mAdapter!=null){
                    mAdapter!!.notifyDataChanged(lofterList)
                }
                getListData()
            }

        }
        isPrepared=true

        lazyLoad()
    }

    public fun setLabel (l:String) :LofterFragment{
        label=l
        val bundle = Bundle()
        bundle.putSerializable("label", label)
        return instance
    }

    public fun getLabel () :String?{

        return label
    }

    private fun getListData() {
        RequestUtils.get(URLs.LOFTER_LEST + page+"&labelId="+label, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var value = MyJSON.getString(result, "value")
                if (!TextUtils.isEmpty(value)) {
                   var lsit :ArrayList<Lofter> = Gson().fromJson(value, object : TypeToken<List<Lofter>>() {}.type)
                    if(lsit.size>0){
                        lsit.forEach {it.imagesUrl= it.imagesUrl.replace("164y164",imageWidth) }
                        lofterList.addAll(lsit)
                        if (mAdapter == null) {
                            mAdapter = LofterListAdapter(context, lofterList,object : LofterListAdapter.OnClickListener{
                                override fun onClick(v: View) {
                                    val intent = Intent(context,LofterActivity::class.java)
                                    //获取intent对象
                                    intent.putExtra("url",URLs.LOFTER_DETAILS+lofterList.get(image_list.getChildAdapterPosition(v)).permalink)
                                    intent.putExtra("label",label)
                                    activity?.startActivityForResult(intent,1111)
                                }

                            })
                            image_list.adapter = mAdapter
                        } else {
                            if(page==1){
                                mAdapter!!.notifyDataChanged(lofterList)
                            }else{
                                mAdapter!!.notifyRangeInserted(lofterList,lofterList.size-lsit.size,lsit.size)
                            }
                        }
                        page++
                    }
                }
                image_list_swipe_refresh.isRefreshing=false
            }

            override fun onError() {
                if(image_list_swipe_refresh!=null){
                    image_list_swipe_refresh.isRefreshing=false
                }
            }
        })
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||lofterList.size>0) {
            return
        }
        if(TextUtils.isEmpty(imageWidth)){
           var width:Int=(ScreenUtils.getScreenWidth(context)-ScreenUtils.dip2px(context,30f))/2
            imageWidth=width.toString()+"w"+width.toString()
        }

        image_list_swipe_refresh?.isRefreshing=true
        page=1
        getListData()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        clearFindViewByIdCache()
//
//    }


}

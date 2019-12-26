package com.nightfeed.wendu.fragment

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.WebActivity
import com.nightfeed.wendu.adapter.MagazineListAdapter
import com.nightfeed.wendu.model.QingMang
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*


class QingMangFragment : BaseFragment() {

    private var viewZhiHu: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var lastVisibleItem: Int = 0

    private var qingMangList :MutableList<QingMang> = ArrayList<QingMang>()
    private var mAdapter: MagazineListAdapter?=null
    private var clickPosition=0

    private var nextUrl=""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewZhiHu==null){
            viewZhiHu=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewZhiHu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(image_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&qingMangList.size>0) {
                        getMagazineList()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    lastVisibleItem = mLayoutManager!!.findLastVisibleItemPosition()
                }
            })

            image_list_swipe_refresh.setOnRefreshListener {
                qingMangList.clear()
                if(mAdapter!=null){
                    mAdapter?.notifyDataSetChanged()
                }
                nextUrl=""
                getMagazineList()
            }
        }
        isPrepared=true
        lazyLoad()
    }

    private fun getMagazineList() {
        var url=nextUrl
        if(TextUtils.isEmpty(url)){
            url=URLs.QING_MANG_LIST
        }
        RequestUtils.get(url, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {

                if (!TextUtils.isEmpty(result)) {
                    var jsonObject = JSONObject(result)
                    nextUrl=jsonObject.getString("nextUrl")

                    var events = jsonObject.getJSONArray("events")
                    if (events != null && events.length() > 0) {
                        var addSzie=0
                        for (i in 0 until events.length()) {
                            var type=events.getJSONObject(i).getString("type")
                            if(TextUtils.equals(type,"image")){
                                type="imageItem"
                            }
                            var typeObject=MyJSON.getJSONObject(events.getJSONObject(i),type)
                            if(typeObject!=null&&typeObject.has("mid")){
                                try {
                                    qingMangList.add(Gson().fromJson(typeObject.toString(),QingMang::class.java))
                                    addSzie++
                                }catch (e:Exception){

                                }
                            }
                        }
                        updateList(addSzie)
                    }
                }
            }

            override fun onError() {
            }
        })
    }

    private fun updateList(addSzie:Int) {
        if (mAdapter == null) {
            mAdapter= MagazineListAdapter(context, qingMangList, object : MagazineListAdapter.OnClickListener {
                override fun onClick(v: MagazineListAdapter.MagazineViewHolder) {

                    clickPosition=image_list.getChildAdapterPosition(v.itemView)

                    var magazine = qingMangList.get(clickPosition)

                    if(TextUtils.isEmpty(magazine.webUrl)){
                        RequestUtils.get(URLs.QING_MANG_CONTENT+magazine.docId+"&mid="+magazine.mid, object : RequestUtils.OnResultListener {
                            override fun onSuccess(result: String) {
                               var events= MyJSON.getJSONArray(result,"events")
                                if (events!=null&&events.length()>0){
                                    var type=events.getJSONObject(0).getString("type")
                                    if(TextUtils.equals(type,"image")){
                                        type="imageItem"
                                    }
                                    try {
                                        var webUrl= events.getJSONObject(0).getJSONObject(type).getString("webUrl")
                                        goWebActivity(magazine.title,webUrl)
                                        return
                                    }catch (e:Exception){

                                    }
                                }
                                ToastUtil.showError(context,"无网页链接")
                            }

                            override fun onError() {
                                ToastUtil.showError(context,"无网页链接")
                            }
                        })
                    }else{
                        goWebActivity(magazine.title,magazine.webUrl)
                    }
                }

            })
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyItemRangeInserted(qingMangList.size - addSzie, addSzie)
        }

        image_list_swipe_refresh?.isRefreshing=false
    }

    private fun goWebActivity(title: String,url:String) {
        var toDetail = Intent(context, WebActivity::class.java)
        toDetail.putExtra("title", title)
        toDetail.putExtra("url",url)
        activity!!.startActivity(toDetail)
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||qingMangList.size>0) {
            return
        }

        image_list_swipe_refresh?.isRefreshing=true

        getMagazineList()
    }

}


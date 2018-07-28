package com.nightfeed.wendu.fragment

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.HuaBanActivity
import com.nightfeed.wendu.activity.LofterActivity
import com.nightfeed.wendu.activity.TuChongActivity
import com.nightfeed.wendu.adapter.LofterListAdapter
import com.nightfeed.wendu.adapter.TuChongListAdapter
import com.nightfeed.wendu.model.Lofter
import com.nightfeed.wendu.model.TuChong
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ScreenUtils
import com.nightfeed.wendu.view.MyStaggeredGridLayoutManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONArray
import java.util.ArrayList


class TuChongFragment : BaseFragment() {


    val instance by lazy { this }

    private var viewHuaban: View? =null
    private var isPrepared=false
    private var mAdapter: TuChongListAdapter?=null
    private var tuchongList :MutableList<TuChong> = ArrayList<TuChong>()
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

        if (savedInstanceState != null&&savedInstanceState.getSerializable("label")!=null) {

            label = savedInstanceState.getSerializable("label") as String
        }

        if(image_list.layoutManager==null){
            mLayoutManager= MyStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            mLayoutManager?.gapStrategy=StaggeredGridLayoutManager.GAP_HANDLING_NONE
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&tuchongList.size>0) {
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
                tuchongList.clear()
                if(mAdapter!=null){
                    mAdapter!!.notifyDataChanged(tuchongList)
                }
                getListData()
            }

        }
        isPrepared=true

        lazyLoad()
    }

    public fun setLabel (l:String) :TuChongFragment{
        label=l
        val bundle = Bundle()
        bundle.putSerializable("label", label)
        return instance
    }

    private fun getListData() {
        var url=""
        if(TextUtils.equals("图虫",label)){
            url=URLs.TUCHONG_RECOMMEND+page
            if(page==1){
                url=url+"&type=refresh"
            }else{
                url=url+"&type=loadmore&post_id="+tuchongList.last().post_id
            }
        }else {
            url=URLs.TUCHONG_SEARCH+label+"&page="+page
        }
        RequestUtils.get(url, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {
                var value :JSONArray?
                if(TextUtils.equals("图虫",label)){
                    value = MyJSON.getJSONArray(result, "feedList")
                }else{
                    value = MyJSON.getJSONObject(result, "data")?.getJSONArray("post_list")
                }

                if (value!=null&&value.length()>0) {
                    var i=0
                    var additem=0
                    while (i<value.length()){
                        var images=value.getJSONObject(i).getJSONArray("images")
                        if(images!=null&&images.length()>0){
                            var ims= ArrayList<TuChong.image>()
                            for(p in 0..(images.length()-1)){
                                var im=images.getJSONObject(p)
                                ims.add(TuChong.image(im.getString("user_id"), im.getString("img_id"), im.getInt("width"), im.getInt("height")))
                            }
                            tuchongList.add(TuChong(value.getJSONObject(i).getString("post_id"),value.getJSONObject(i).getString("title"),ims))
                            additem++
                        }
                        i++
                    }

                    if (mAdapter == null) {
                        mAdapter = TuChongListAdapter(context, tuchongList,object : TuChongListAdapter.OnClickListener{
                            override fun onClick(holder: RecyclerView.ViewHolder) {
                                val intent = Intent(context, TuChongActivity::class.java)
                                holder.itemView.transitionName="tuchong"
                                intent.putExtra("bean",tuchongList[image_list.getChildAdapterPosition(holder.itemView)])
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, holder.itemView, "tuchong").toBundle())

                            }
                        })
                        image_list.adapter = mAdapter
                        page++

                    } else {
                        if(page==1){
                            mAdapter!!.notifyDataChanged(tuchongList)
                            page++

                        }else if(additem>0){
                            mAdapter!!.notifyRangeInserted(tuchongList,tuchongList.size-additem,additem)
                            page++

                        }
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
        if (!isPrepared || !isFragmentVisible||tuchongList.size>0) {
            return
        }
        if(TextUtils.isEmpty(imageWidth)){
           var width:Int=(ScreenUtils.getScreenWidth(context)-ScreenUtils.dip2px(context,30f))/2
            imageWidth=width.toString()+"w"+width.toString()
        }

        image_list_swipe_refresh.isRefreshing=true
        page=1
        getListData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }


}

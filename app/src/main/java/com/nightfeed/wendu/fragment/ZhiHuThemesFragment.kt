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
import com.nightfeed.wendu.R
import com.nightfeed.wendu.activity.ZhiHuActivity
import com.nightfeed.wendu.adapter.ZhiHuListAdapter
import com.nightfeed.wendu.model.ZhiHu
import com.nightfeed.wendu.model.ZhiHuDB
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.image_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import org.litepal.LitePal
import java.util.*


class ZhiHuThemesFragment : BaseFragment() {

    val instance by lazy { this }

    private var viewZhiHu: View? =null
    private var isPrepared=false
    private var mLayoutManager : LinearLayoutManager?= null
    private var lastVisibleItem: Int = 0
    private var zhihuList :MutableList<ZhiHu> = ArrayList<ZhiHu>()
    private var mAdapter: ZhiHuListAdapter?=null
    private var clickPosition=0

    private var themes =0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(viewZhiHu==null){
            viewZhiHu=inflater.inflate(R.layout.image_fragment, container, false)
        }
        return viewZhiHu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null&&savedInstanceState.getSerializable("themes")!=null) {

            themes = savedInstanceState.getSerializable("themes") as Int
        }

        if(image_list.layoutManager==null){
            mLayoutManager=LinearLayoutManager(context)
            image_list.layoutManager=mLayoutManager

            image_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 2 >= mLayoutManager!!.itemCount&&zhihuList.size>0) {
                        getListData(URLs.ZHIHU_THEME+themes+"/before/"+zhihuList.last().id)
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    lastVisibleItem = mLayoutManager!!.findLastVisibleItemPosition()
                }
            })

            image_list_swipe_refresh.setOnRefreshListener {
                zhihuList.clear()
                if(mAdapter!=null){
                    mAdapter?.notifyDataSetChanged()
                }
                getListData(URLs.ZHIHU_THEME+themes)
            }
        }
        isPrepared=true
        lazyLoad()
    }

    public fun setThemes(id:Int):ZhiHuThemesFragment{
        themes=id
        val bundle = Bundle()
        bundle.putSerializable("themes", id)
        return instance
    }

    private fun getListData(url:String) {
        RequestUtils.get(url, object : RequestUtils.OnResultListener {
            override fun onSuccess(result: String) {

                if (!TextUtils.isEmpty(result)) {
                    var list = JSONObject(result).getJSONArray("stories")
                    if (list != null && list.length() > 0) {
                        for (i in 0..(list.length() - 1)) {
                            var z = list.getJSONObject(i)
                            var ims = MyJSON.getJSONArray(z, "images")
                            var im = ""
                            if (ims != null && ims.length() > 0) {
                                im = ims.getString(0)
                            }

                            var id=z.getString("id")

                            zhihuList.add(ZhiHu(id, z.getString("title"), im, LitePal.where("zid like ?", id).find(ZhiHuDB::class.java).size>0))
                        }

                        updateList(list)
                    }
                }
            }

            override fun onError() {
            }
        })
    }


    private fun updateList(list: JSONArray) {
        if (mAdapter == null) {
            mAdapter= ZhiHuListAdapter(context, zhihuList, object : ZhiHuListAdapter.OnClickListener {
                override fun onClick(v: ZhiHuListAdapter.ZhiHuViewHolder) {
                    clickPosition=image_list.getChildAdapterPosition(v.itemView)

                    var zhihu = zhihuList.get(clickPosition)
                    var toDetail = Intent(context, ZhiHuActivity::class.java)

                    toDetail.putExtra("id", zhihu.id)
                    toDetail.putExtra("title", zhihu.title)
                    activity!!.startActivityForResult(toDetail,122)
                }

            })
            image_list.adapter=mAdapter
        } else {
            mAdapter!!.notifyRangeInserted(zhihuList, zhihuList.size - list.length(), list.length())
        }
        image_list_swipe_refresh?.isRefreshing=false
    }


    override fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible||zhihuList.size>0) {
            return
        }

        image_list_swipe_refresh?.isRefreshing=true

        getListData(URLs.ZHIHU_THEME+themes)
    }


    public fun setRead(){
        zhihuList.get(clickPosition).read=true
        mAdapter!!.notifyItemChanged(clickPosition)
    }

}


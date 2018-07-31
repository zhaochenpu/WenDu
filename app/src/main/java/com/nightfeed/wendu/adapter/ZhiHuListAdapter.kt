package com.nightfeed.wendu.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.OneSentence
import com.nightfeed.wendu.model.ZhiHu

class ZhiHuListAdapter (context: Context?, datas:List<ZhiHu>, val onClickListener:OnClickListener): RecyclerView.Adapter<ZhiHuListAdapter.ZhiHuViewHolder>(){
    private var mContext  =  context
    private var datas=datas

    interface OnClickListener{
        fun onClick(v:ZhiHuViewHolder)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):ZhiHuListAdapter.ZhiHuViewHolder {

        return ZhiHuViewHolder(LayoutInflater.from(mContext).inflate(R.layout.zhihu_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:ZhiHuViewHolder, p1: Int) {
        var zhihu=datas[p1]

        if(zhihu.read){
            p0.zhihu_title.setTextColor(ContextCompat.getColor(mContext!!, R.color.textNotEnable))
        }else{
            p0.zhihu_title.setTextColor(ContextCompat.getColor(mContext!!, R.color.textPrimary))
        }

        p0.zhihu_title.text=zhihu.title

        if(!TextUtils.isEmpty(zhihu.image)){
            p0.zhihu_iv.visibility=View.VISIBLE
            Glide.with(mContext!!).load(zhihu.image).into(p0.zhihu_iv)
        }else{
            p0.zhihu_iv.visibility=View.GONE
        }

        p0.itemView.setOnClickListener {
            onClickListener.onClick(p0)
        }
    }

    fun notifyRangeInserted(list:List<ZhiHu>,start:Int,count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class ZhiHuViewHolder:RecyclerView.ViewHolder{
        val zhihu_iv:ImageView
        val zhihu_title :TextView

        constructor(view:View) : super(view) {
            zhihu_iv=view.findViewById(R.id.zhihu_iv)
            zhihu_title=view.findViewById(R.id.zhihu_title)
        }
    }
}
package com.nightfeed.wendu.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.Amusepage3dm

class Amusepage3dmListAdapter (context: Context?, datas:List<Amusepage3dm>, val onClickListener:OnClickListener): RecyclerView.Adapter<Amusepage3dmListAdapter.AmusepageViewHolder>(){
    private var mContext  =  context
    private var datas=datas

    interface OnClickListener{
        fun onClick(v:AmusepageViewHolder)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):Amusepage3dmListAdapter.AmusepageViewHolder {

        return AmusepageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.zhihu_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:AmusepageViewHolder, p1: Int) {
        var amusepage3dm=datas[p1]

        p0.zhihu_title.text=amusepage3dm.title

        if(!TextUtils.isEmpty(amusepage3dm.litpic)){
            p0.zhihu_iv.visibility=View.VISIBLE
            Glide.with(mContext!!).load(amusepage3dm.litpic).into(p0.zhihu_iv)
        }else{
            p0.zhihu_iv.visibility=View.GONE
        }

        p0.itemView.setOnClickListener {
            onClickListener.onClick(p0)
        }
    }

    class AmusepageViewHolder:RecyclerView.ViewHolder{
        val zhihu_iv:ImageView
        val zhihu_title :TextView

        constructor(view:View) : super(view) {
            zhihu_iv=view.findViewById(R.id.zhihu_iv)
            zhihu_title=view.findViewById(R.id.zhihu_title)
        }
    }
}
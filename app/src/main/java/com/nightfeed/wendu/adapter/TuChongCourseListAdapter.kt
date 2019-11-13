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
import com.nightfeed.wendu.model.TuChongCourse
import com.nightfeed.wendu.model.WoShiPM

class TuChongCourseListAdapter (context: Context?, datas:List<TuChongCourse>, val onClickListener:OnClickListener): RecyclerView.Adapter<TuChongCourseListAdapter.TViewHolder>(){
    private var mContext  =  context
    private var datas=datas

    interface OnClickListener{
        fun onClick(v:TViewHolder)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):TuChongCourseListAdapter.TViewHolder {

        return TViewHolder(LayoutInflater.from(mContext).inflate(R.layout.zhihu_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:TViewHolder, p1: Int) {
        var pm=datas[p1]


        p0.zhihu_title.text=pm.title

        if(!TextUtils.isEmpty(pm.image)){
            p0.zhihu_iv.visibility=View.VISIBLE
            Glide.with(mContext!!).load(pm.image).into(p0.zhihu_iv)
        }else{
            p0.zhihu_iv.visibility=View.GONE
        }

        p0.itemView.setOnClickListener {
            onClickListener.onClick(p0)
        }
    }

    fun notifyRangeInserted(list:List<TuChongCourse>,start:Int,count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class TViewHolder:RecyclerView.ViewHolder{
        val zhihu_iv:ImageView
        val zhihu_title :TextView

        constructor(view:View) : super(view) {
            zhihu_iv=view.findViewById(R.id.zhihu_iv)
            zhihu_title=view.findViewById(R.id.zhihu_title)
        }
    }
}
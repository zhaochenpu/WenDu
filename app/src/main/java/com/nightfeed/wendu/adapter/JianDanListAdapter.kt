package com.nightfeed.wendu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nightfeed.wendu.R


/**
 * 0文本 1图片
 */
class JianDanListAdapter (context: Context?,type:Int,datas:List<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mContext  =  context
    private var datas=datas
    private var type=type

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):RecyclerView.ViewHolder {

        if (type==0){
            return JianDanTextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.tv_item, p0, false))
        }else{
            return JianDanImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_item, p0, false))
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:RecyclerView.ViewHolder, p1: Int) {

        if(p0 is JianDanTextViewHolder){
            p0.tv.text=datas[p1]
        }else if(p0 is JianDanImageViewHolder){
            Glide.with(mContext!!).load(datas[p1]).apply(RequestOptions.fitCenterTransform()).into( (p0.iv))
        }
    }

    fun notifyRangeInserted(list:List<String>,start:Int,count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class JianDanTextViewHolder:RecyclerView.ViewHolder{
        val tv :TextView

        constructor(view:View) : super(view) {
            tv=view.findViewById(R.id.tv)
        }
    }

    class JianDanImageViewHolder:RecyclerView.ViewHolder{
        val iv :ImageView

        constructor(view:View) : super(view) {
            iv=view.findViewById(R.id.iv)
        }
    }
}
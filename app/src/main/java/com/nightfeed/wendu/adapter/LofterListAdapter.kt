package com.nightfeed.wendu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.Lofter

class LofterListAdapter (context: Context?, datas:List<Lofter>,val onClickListener:OnClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mContext  =  context
    private var datas=datas

    interface OnClickListener{
        fun onClick(v:View)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.grid_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

        Glide.with(mContext!!).load(datas[p1].imagesUrl).apply(RequestOptions.fitCenterTransform()).into( (p0 as MyViewHolder).iv)
        p0.itemView.setOnClickListener { onClickListener.onClick(it) }
    }

    fun  notifyDataChanged(list:List<Lofter> ){
        datas=list
        notifyDataSetChanged()
    }


    fun notifyRangeInserted(list:List<Lofter>,start:Int,count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class MyViewHolder:RecyclerView.ViewHolder{
        val iv:ImageView
        constructor(view:View) : super(view) {
            iv=view.findViewById(R.id.iv)
        }
    }
}
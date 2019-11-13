package com.nightfeed.wendu.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.TuChong
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.view.ImagePopUpWindow

class TuChongListAdapter (context: Context?, datas:List<TuChong>, val onClickListener:OnClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
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

        var image=datas[p1].images[0]
        var url=""
        if(image.width>image.height){
            url=URLs.TUCHONG_IMAGE+image.user_id+"/m/"+image.img_id+".webp"
        }else{
            url=URLs.TUCHONG_IMAGE+image.user_id+"/ft640/"+image.img_id+".webp"
        }
        Glide.with(mContext!!).load(url).into( (p0 as MyViewHolder).iv)
        p0.itemView.setOnClickListener { onClickListener.onClick(it) }

        p0.iv.setOnLongClickListener { v ->
            ImagePopUpWindow((mContext as Activity?)!!,v!!, URLs.TUCHONG_IMAGE+image.user_id+"/f/"+image.img_id+".jpg")
            true
        }
    }

    fun  notifyDataChanged(list:List<TuChong> ){
        datas=list
        notifyDataSetChanged()
    }


    fun notifyRangeInserted(list:List<TuChong>,start:Int,count:Int){
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
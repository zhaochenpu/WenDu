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
import com.nightfeed.wendu.model.TuChong
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ScreenUtils

class TuChongDetailsAdapter (context: Context?, datas:List<TuChong.image>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mContext  =  context
    private var datas=datas
    private var screenWidth=0


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        var iv=(p0 as MyViewHolder).iv
        var image=datas[p1]
        if(datas[p1].width!=0){
            var layoutParams= iv.layoutParams
            if(screenWidth==0){
                screenWidth=ScreenUtils.getScreenWidth(mContext)
            }
            layoutParams.height=screenWidth*image.height/image.width
            iv.layoutParams=layoutParams
        }

        if(p1==0){
            iv.transitionName="tuchong"
        }else{
            iv.transitionName=""
        }

        Glide.with(mContext!!).load(URLs.TUCHONG_IMAGE+image.user_id+"/f/"+image.img_id+".webp").apply(RequestOptions.fitCenterTransform()).into( iv)
    }

    fun  notifyDataChanged(list:List<TuChong.image> ){
        datas=list
        notifyDataSetChanged()
    }


    fun notifyRangeInserted(list:List<TuChong.image>,start:Int,count:Int){
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
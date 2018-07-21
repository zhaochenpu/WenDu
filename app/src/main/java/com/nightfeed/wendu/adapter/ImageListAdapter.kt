package com.nightfeed.wendu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.HuaBan
import com.nightfeed.wendu.net.URLs

class ImageListAdapter (context: Context?, datas:List<HuaBan>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mContext  =  context
    private var datas=datas
    private var  defaultH= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150f,mContext!!.resources.displayMetrics)


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.grid_meizi_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        var iv=(p0 as MyViewHolder).iv
        if(iv.width!=0&&datas[p1].file.width!=0){
            var layoutParams= iv.layoutParams
            layoutParams.height=iv.width*datas[p1].file.height/datas[p1].file.width
            iv.layoutParams=layoutParams
        }
        Glide.with(mContext!!).load(URLs.HUA_BAN_IM+ datas[p1].file.key).into(iv)
    }

    fun  notifyDataChanged(list:List<HuaBan> ){
        datas=list
        notifyDataSetChanged()
    }

    fun notifyRangeInserted(list:List<HuaBan>,start:Int,count:Int){
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
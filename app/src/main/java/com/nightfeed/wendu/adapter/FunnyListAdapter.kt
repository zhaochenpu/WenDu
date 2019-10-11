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
import com.nightfeed.wendu.model.GameSkyFunny

class FunnyListAdapter (context: Context?, datas:List<GameSkyFunny>, val onClickListener:OnClickListener): RecyclerView.Adapter<FunnyListAdapter.FunnyViewHolder>(){
    private var mContext  =  context
    private var datas=datas

    interface OnClickListener{
        fun onClick(v:FunnyViewHolder)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):FunnyListAdapter.FunnyViewHolder {

        return FunnyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.funny_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:FunnyViewHolder, p1: Int) {
        var funny=datas[p1]


        p0.funny_title.text=funny.title

        Glide.with(mContext!!).load(funny.thumbnailURLs[0]).into(p0.funny_iv1)
        Glide.with(mContext!!).load(funny.thumbnailURLs[1]).into(p0.funny_iv2)
        Glide.with(mContext!!).load(funny.thumbnailURLs[2]).into(p0.funny_iv3)

        p0.itemView.setOnClickListener {
            onClickListener.onClick(p0)
        }
    }

    fun notifyRangeInserted(list:List<GameSkyFunny>,start:Int,count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class FunnyViewHolder:RecyclerView.ViewHolder{
        val funny_iv1:ImageView
        val funny_iv2:ImageView
        val funny_iv3:ImageView
        val funny_title :TextView

        constructor(view:View) : super(view) {
            funny_iv1=view.findViewById(R.id.funny_iv1)
            funny_iv2=view.findViewById(R.id.funny_iv2)
            funny_iv3=view.findViewById(R.id.funny_iv3)
            funny_title=view.findViewById(R.id.funny_title)
        }
    }
}
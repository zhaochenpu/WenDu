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
import com.nightfeed.wendu.model.WoShiPM

class PMListAdapter (context: Context?, datas:List<WoShiPM>, val onClickListener:OnClickListener): RecyclerView.Adapter<PMListAdapter.PMViewHolder>(){
    private var mContext  =  context
    private var datas=datas

    interface OnClickListener{
        fun onClick(v:PMViewHolder)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):PMListAdapter.PMViewHolder {

        return PMViewHolder(LayoutInflater.from(mContext).inflate(R.layout.pm_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:PMViewHolder, p1: Int) {
        var pm=datas[p1]


        p0.pm_title.text=pm.title
        p0.pm_type.text=pm.cName

        if(!TextUtils.isEmpty(pm.image)){
            p0.pm_iv.visibility=View.VISIBLE
            Glide.with(mContext!!).load(pm.image).into(p0.pm_iv)
        }else{
            p0.pm_iv.visibility=View.GONE
        }

        p0.itemView.setOnClickListener {
            onClickListener.onClick(p0)
        }
    }

    fun notifyRangeInserted(list:List<WoShiPM>,start:Int,count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class PMViewHolder:RecyclerView.ViewHolder{
        val pm_iv:ImageView
        val pm_title :TextView
        val pm_type :TextView

        constructor(view:View) : super(view) {
            pm_iv=view.findViewById(R.id.pm_iv)
            pm_title=view.findViewById(R.id.pm_title)
            pm_type=view.findViewById(R.id.pm_type)
        }
    }
}
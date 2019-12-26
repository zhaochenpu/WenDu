package com.nightfeed.wendu.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.QingMang

class MagazineListAdapter (context: Context?, data:List<QingMang>, val onClickListener:OnClickListener): RecyclerView.Adapter<MagazineListAdapter.MagazineViewHolder>(){
    private var mContext  =  context
    private var datas=data

    interface OnClickListener{
        fun onClick(v:MagazineViewHolder)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):MagazineListAdapter.MagazineViewHolder {

        return MagazineViewHolder(LayoutInflater.from(mContext).inflate(R.layout.pm_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:MagazineViewHolder, p1: Int) {
        var pm=datas[p1]


        p0.title.text=pm.title
        p0.type.text=pm.magazineName

        if(!TextUtils.isEmpty(pm.cover)){
            p0.iv.visibility=View.VISIBLE
            Glide.with(mContext!!).load(pm.cover).into(p0.iv)
        }else{
            p0.iv.visibility=View.GONE
        }

        p0.itemView.setOnClickListener {
            onClickListener.onClick(p0)
        }
    }

    class MagazineViewHolder:RecyclerView.ViewHolder{
        val iv:ImageView
        val title :TextView
        val type :TextView

        constructor(view:View) : super(view) {
            iv=view.findViewById(R.id.pm_iv)
            title=view.findViewById(R.id.pm_title)
            type=view.findViewById(R.id.pm_type)
        }
    }
}
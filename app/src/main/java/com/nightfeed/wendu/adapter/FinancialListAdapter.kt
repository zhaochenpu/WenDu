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
import com.nightfeed.wendu.model.Finance


class FinancialListAdapter (context: Context?, datas:List<Finance>, val onClickListener:OnClickListener): RecyclerView.Adapter<FinancialListAdapter.FinancialViewHolder>(){
    private var mContext  =  context
    private var datas=datas

    interface OnClickListener{
        fun onClick(v:FinancialViewHolder)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):FinancialListAdapter.FinancialViewHolder {

        return FinancialViewHolder(LayoutInflater.from(mContext).inflate(R.layout.pm_item, p0, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0:FinancialViewHolder, p1: Int) {
        var finance=datas[p1]


        p0.pm_title.text=finance.title
        p0.pm_type.text=finance.source

        if(!TextUtils.isEmpty(finance.imgUrl)){
            if(p0.pm_iv.visibility!=View.VISIBLE){
                p0.pm_iv.visibility=View.VISIBLE
            }
            Glide.with(mContext!!).load(finance.imgUrl).into(p0.pm_iv)
        }else if(p0.pm_iv.visibility!=View.GONE){
            p0.pm_iv.visibility=View.GONE
        }

        p0.itemView.setOnClickListener {
            onClickListener.onClick(p0)
        }
    }

    fun notifyRangeInserted(list:List<Finance>, start:Int, count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class FinancialViewHolder:RecyclerView.ViewHolder{
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
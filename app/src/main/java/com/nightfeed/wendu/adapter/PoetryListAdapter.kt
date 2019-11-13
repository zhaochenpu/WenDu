package com.nightfeed.wendu.adapter

import android.content.Context
import android.graphics.Color
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
import com.nightfeed.wendu.model.Poetry

class PoetryListAdapter (context: Context?, datas:List<Poetry>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mContext  =  context
    private var datas=datas


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):RecyclerView.ViewHolder {
        if(p1==0) {
            return TextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.head_text_item, p0, false))
        }
        return ImageTextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.poetry_item, p0, false))

    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0||TextUtils.isEmpty(datas.get(position).im_raw)){
            return 0
        }
        return 1
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        var poetry=datas[p1]

        if(p0 is ImageTextViewHolder){
            p0.hp_content.text=poetry.text
            p0.hp_authors.text="——"+poetry.author

            if(p1!=0&&!TextUtils.isEmpty(poetry.im_raw)){
                p0.hp_iv.visibility=View.VISIBLE
                Glide.with(mContext!!).load(poetry.im_raw).into(p0.hp_iv)
            }else{
                p0.hp_iv.visibility=View.GONE
            }
        }else if(p0 is TextViewHolder){
            p0.hp_content.setTextColor(ContextCompat.getColor(mContext!!, R.color.textPrimary))
            p0.hp_authors.setTextColor(ContextCompat.getColor(mContext!!, R.color.textSecondary))

            p0.hp_content.text=poetry.text
            p0.hp_authors.text="——"+poetry.author
        }

    }

    fun notifyRangeInserted(list:List<Poetry>, start:Int, count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class ImageTextViewHolder:RecyclerView.ViewHolder{
        val hp_iv:ImageView
        val hp_content :TextView
        val hp_authors:TextView

        constructor(view:View) : super(view) {
            hp_iv=view.findViewById(R.id.hp_iv)
            hp_authors=view.findViewById(R.id.hp_authors)
            hp_content=view.findViewById(R.id.hp_content)
        }
    }

    class TextViewHolder:RecyclerView.ViewHolder{
        val hp_content :TextView
        val hp_authors:TextView

        constructor(view:View) : super(view) {
            hp_authors=view.findViewById(R.id.hp_authors)
            hp_content=view.findViewById(R.id.hp_content)
        }
    }
}
package com.nightfeed.wendu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.JuDu

class JuDuListAdapter (context: Context?, datas:List<JuDu>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mContext  =  context
    private var datas=datas
    private var onClickListener: OnClickListener?=null

    interface OnClickListener{
        fun onClick(v:View)
    }

    public fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener=onClickListener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):RecyclerView.ViewHolder {
        if(p1==0) {
            return TextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.head_text_item, p0, false))
        }
        return ImageTextViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_word_item, p0, false))

    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        var sentence=datas[p1]

        if(p0 is ImageTextViewHolder){

            if(!sentence.content.contains("\n")){
                p0.hp_content.text="\t\t"+sentence.content
            }else{
                p0.hp_content.text=sentence.content
            }

            if(TextUtils.isEmpty(sentence.subheading)){
                p0.hp_authors.visibility=View.GONE
            }else{
                p0.hp_authors.visibility=View.VISIBLE
                p0.hp_authors.text="——"+sentence.subheading
            }

            Glide.with(mContext!!).load(sentence.image.url).into(p0.hp_iv)

        }else if(p0 is TextViewHolder){

            p0.hp_content.text=sentence.content

            if(TextUtils.isEmpty(sentence.subheading)){
                p0.hp_authors.visibility=View.GONE
            }else{
                p0.hp_authors.visibility=View.VISIBLE
                p0.hp_authors.text="——"+sentence.subheading
            }
        }

        if(onClickListener!=null){
            p0.itemView.setOnClickListener { onClickListener!!.onClick(it) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0||TextUtils.isEmpty(datas[position].image?.url)){
            return 0
        }
        return 1
    }

    fun notifyRangeInserted(list:List<JuDu>,start:Int,count:Int){
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
            hp_content=view.findViewById(R.id.hp_content)
            hp_authors=view.findViewById(R.id.hp_authors)
        }
    }
}
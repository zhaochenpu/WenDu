package com.nightfeed.wendu.adapter

import android.content.Context
import android.support.transition.Visibility
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nightfeed.wendu.R
import com.nightfeed.wendu.model.OneSentence
import com.nightfeed.wendu.net.URLs

class OneSentenceListAdapter (context: Context?, datas:List<OneSentence>): RecyclerView.Adapter<OneSentenceListAdapter.MyViewHolder>(){
    private var mContext  =  context
    private var datas=datas


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):OneSentenceListAdapter.MyViewHolder {

        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.image_word_item, p0, false))

    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        var oneSentence=datas[p1]

        if(!oneSentence.hp_content.contains("\n")){
            p0.hp_content.text="\t\t"+oneSentence.hp_content

        }else{
            p0.hp_content.text=oneSentence.hp_content
        }
        p0.hp_authors.text="——"+oneSentence.text_authors

        if(p1!=0&&!TextUtils.isEmpty(oneSentence.hp_img_url)){
            p0.hp_iv.visibility=View.VISIBLE
            Glide.with(mContext!!).load(oneSentence.hp_img_url).into(p0.hp_iv)
        }else{
            p0.hp_iv.visibility=View.GONE
        }
    }

    fun notifyRangeInserted(list:List<OneSentence>,start:Int,count:Int){
        datas=list
        notifyItemRangeInserted(start,count)
    }

    class MyViewHolder:RecyclerView.ViewHolder{
        val hp_iv:ImageView
        val hp_content :TextView
        val hp_authors:TextView

        constructor(view:View) : super(view) {
            hp_iv=view.findViewById(R.id.hp_iv)
            hp_authors=view.findViewById(R.id.hp_authors)
            hp_content=view.findViewById(R.id.hp_content)
        }
    }
}
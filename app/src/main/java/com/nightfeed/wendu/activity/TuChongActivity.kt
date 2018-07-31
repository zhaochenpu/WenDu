package com.nightfeed.wendu.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nightfeed.wendu.R
import com.nightfeed.wendu.adapter.TuChongDetailsAdapter
import com.nightfeed.wendu.model.TuChong
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_tuchong.*

class TuChongActivity : AppCompatActivity() {

    val instance by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuchong)
        var tuchong=intent.getSerializableExtra("bean") as TuChong
        toolbar.title=tuchong.title
        setSupportActionBar(toolbar)

        var layoutParams= image1.layoutParams
        var  screenWidth= ScreenUtils.getScreenWidth(instance)

        var image=tuchong.images.get(0)

        layoutParams.height=screenWidth*image.height/image.width
        image1.layoutParams=layoutParams
        var thumbnailurl=""
        if(image.height>image.width){
            thumbnailurl=URLs.TUCHONG_IMAGE+image.user_id+"/m/"+image.img_id+".webp"
        }else{
            thumbnailurl=URLs.TUCHONG_IMAGE+image.user_id+"/ft640/"+image.img_id+".webp"
        }
        Glide.with(instance).load(URLs.TUCHONG_IMAGE+image.user_id+"/f/"+image.img_id+".webp").thumbnail(Glide.with(instance!!).load(thumbnailurl)).into( image1)


        toolbar.setNavigationOnClickListener {
            if(scrollView.scrollY>image.height){
                image1.transitionName=""
            }
            finishAfterTransition()

        }

        if(tuchong.images.size>1){
            tuchong_images.layoutManager= LinearLayoutManager(instance)
            var list=ArrayList<TuChong.image>()

            for (i in 1..(tuchong.images.size-1)){
                list.add(tuchong.images[i])
            }
            list.removeAt(0)
            tuchong_images.adapter=TuChongDetailsAdapter(instance,list)
        }

    }

    override fun onBackPressed() {
        finishAfterTransition()
    }
}

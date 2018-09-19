package com.nightfeed.wendu.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nightfeed.wendu.R
import com.nightfeed.wendu.adapter.TuChongDetailsAdapter
import com.nightfeed.wendu.model.TuChong
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ScreenUtils
import com.nightfeed.wendu.view.ImagePopUpWindow
import kotlinx.android.synthetic.main.activity_tuchong.*

class TuChongActivity : AppCompatActivity() {

    val instance by lazy { this }

    lateinit var firstImage:TuChong.image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuchong)
        var tuchong=intent.getSerializableExtra("bean") as TuChong
        toolbar.title=tuchong.title
        setSupportActionBar(toolbar)

        var layoutParams= image1.layoutParams
        var  screenWidth= ScreenUtils.getScreenWidth(instance)

         firstImage=tuchong.images.get(0)

        layoutParams.height=screenWidth*firstImage.height/firstImage.width
        image1.layoutParams=layoutParams
        var thumbnailurl=""
        if(firstImage.height>firstImage.width){
            thumbnailurl=URLs.TUCHONG_IMAGE+firstImage.user_id+"/m/"+firstImage.img_id+".webp"
        }else{
            thumbnailurl=URLs.TUCHONG_IMAGE+firstImage.user_id+"/ft640/"+firstImage.img_id+".webp"
        }
        Glide.with(instance).load(URLs.TUCHONG_IMAGE+firstImage.user_id+"/f/"+firstImage.img_id+".webp").thumbnail(Glide.with(instance!!).load(thumbnailurl)).into( image1)

        image1.setOnLongClickListener { v ->
            ImagePopUpWindow(instance,v!!, URLs.TUCHONG_IMAGE+firstImage.user_id+"/f/"+firstImage.img_id+".jpg")
            true
        }

        toolbar.setNavigationOnClickListener {
            finishActivity()

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

    private fun finishActivity() {
        if (scrollView.scrollY > firstImage.height) {
            image1.transitionName = ""
        }
        finishAfterTransition()
    }

    override fun onBackPressed() {
        finishActivity()
    }
}

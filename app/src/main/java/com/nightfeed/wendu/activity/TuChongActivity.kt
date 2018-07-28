package com.nightfeed.wendu.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.nightfeed.wendu.R
import com.nightfeed.wendu.adapter.TuChongDetailsAdapter
import com.nightfeed.wendu.model.TuChong
import kotlinx.android.synthetic.main.activity_tuchong.*

class TuChongActivity : AppCompatActivity() {

    val instance by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuchong)
        var tuchong=intent.getSerializableExtra("bean") as TuChong
        toolbar.title=tuchong.title
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(instance,BrowsePictureActivity::class.java))
        }

        tuchong_images.layoutManager= LinearLayoutManager(instance)
        tuchong_images.adapter=TuChongDetailsAdapter(instance,tuchong.images)
    }
}

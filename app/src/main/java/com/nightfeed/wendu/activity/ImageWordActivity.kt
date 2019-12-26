package com.nightfeed.wendu.activity

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nightfeed.wendu.R
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_image_word.*
import androidx.viewpager.widget.PagerAdapter
import com.nightfeed.wendu.utils.StatusBarUtil
import com.google.android.material.appbar.AppBarLayout
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.transition.Explode
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nightfeed.wendu.fragment.*
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.CollapsingToolbarLayoutState
import com.nightfeed.wendu.utils.ScreenUtils

class ImageWordActivity : AppCompatActivity() {

    val instance by lazy { this }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("一句","飞地","句读","银句子","诗+歌","英句","单词")
    private var collapsingState= CollapsingToolbarLayoutState.EXPANDED
    private var  distance=0
    private var lastImage=""
    private var lastPosition=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_word)
        StatusBarUtil.transparencyBar(instance)
        initView()
    }

    private fun initView() {
        viewList.add(OneSentenceFragment())
        viewList.add(FeiDiFragment())
        viewList.add(JuDuFragment().setLabel(0))
        viewList.add(SilverSentenceFragment())
        viewList.add(MonoFragment())
        viewList.add(DailySentenceFragment().setURL(URLs.YOUDAO_DAILY_SENTENCE))
        viewList.add(DailySentenceFragment().setURL(URLs.YOUDAO_DAILY_WORD))

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(ViewpagerOnPageChangeListener(tabs))
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if(distance==0){
                    distance=(app_bar.totalScrollRange- ScreenUtils.dip2px(instance,96f))
                }
                if (collapsingState !== CollapsingToolbarLayoutState.EXPANDED) {
                    collapsingState = CollapsingToolbarLayoutState.EXPANDED//修改状态标记为展开

                    tabs.setTabTextColors(ContextCompat.getColor(instance, R.color.translucent_white_20),Color.WHITE)
                    toolbar.setTitleTextColor(Color.WHITE)
                    toolbar.navigationIcon=getDrawable(R.drawable.back_white)

                    StatusBarUtil.StatusBarDarkMode(instance)
                }
            } else if (Math.abs(verticalOffset) >= distance) {
                if (collapsingState !== CollapsingToolbarLayoutState.COLLAPSED) {
                    collapsingState = CollapsingToolbarLayoutState.COLLAPSED//修改状态标记为折叠

                    tabs.setTabTextColors(ContextCompat.getColor(instance, R.color.textSecondary),ContextCompat.getColor(instance, R.color.textPrimary))
                    toolbar.setTitleTextColor(ContextCompat.getColor(instance, R.color.textPrimary))
                    toolbar.navigationIcon=getDrawable(R.drawable.back)

                    StatusBarUtil.StatusBarLightMode(instance)
                }
            } else {
                if (collapsingState !== CollapsingToolbarLayoutState.INTERNEDIATE) {

                    collapsingState = CollapsingToolbarLayoutState.INTERNEDIATE//修改状态标记为中间
                }
            }
        })

        var explode= Explode()
        explode.duration=300
        window.enterTransition = explode
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return viewList[position]
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return viewList.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }

    inner class ViewpagerOnPageChangeListener(tabs:TabLayout) : TabLayout.TabLayoutOnPageChangeListener(tabs){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    public fun setHeadImage(url:String){
        if(!TextUtils.isEmpty(lastImage)){
            imageview.visibility=View.VISIBLE
            Glide.with(instance).load(lastImage).into(imageview)
        }else{
            imageview2.visibility= View.VISIBLE
        }

        Glide.with(instance).load(url).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                lastImage=url

                 var finalRadius : Float = Math.hypot(imageview.getWidth().toDouble(), imageview.getHeight().toDouble()).toFloat()
                var centerX=0
                if(lastPosition>container.currentItem){
                    centerX=imageview.width
                }

                lastPosition=container.currentItem

                var anim = ViewAnimationUtils.createCircularReveal(imageview2,centerX,imageview.height, (imageview.width/4).toFloat(), finalRadius)
                anim.setDuration(300L)
                anim.setInterpolator( AccelerateDecelerateInterpolator())
                anim.addListener(object :Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        imageview.visibility=View.GONE
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }
                })
                anim.start()
                return false
            }
        }).into(imageview2)
    }
 }

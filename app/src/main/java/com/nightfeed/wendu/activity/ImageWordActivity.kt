package com.nightfeed.wendu.activity

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import com.nightfeed.wendu.R
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_image_word.*
import android.support.v4.view.PagerAdapter
import com.nightfeed.wendu.fragment.OneSentenceFragment
import com.nightfeed.wendu.utils.StatusBarUtil
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.nightfeed.wendu.fragment.MonoFragment


class ImageWordActivity : AppCompatActivity() {

    val instance by lazy { this }

    private enum class CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("一句","诗+歌")
    private var collapsingState=CollapsingToolbarLayoutState.EXPANDED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_word)
        StatusBarUtil.transparencyBar(instance)
        initView()
    }

    private fun initView() {
        viewList.add(OneSentenceFragment())
        viewList.add(MonoFragment())

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(ViewpagerOnPageChangeListener(tabs))
        tabs.tabMode = TabLayout.MODE_FIXED
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        toolbar.setNavigationOnClickListener {
            finish()
        }

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (collapsingState !== CollapsingToolbarLayoutState.EXPANDED) {
                    collapsingState = CollapsingToolbarLayoutState.EXPANDED//修改状态标记为展开

                    tabs.setTabTextColors(ContextCompat.getColor(instance, R.color.translucent_white_20),Color.WHITE)
                    toolbar.setTitleTextColor(Color.WHITE)
                    toolbar.navigationIcon=getDrawable(R.drawable.back_white)

                    StatusBarUtil.StatusBarDarkMode(instance)
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
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
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE
        }
    }

    inner class ViewpagerOnPageChangeListener(tabs:TabLayout) : TabLayout.TabLayoutOnPageChangeListener(tabs){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    public fun setHeadImage(url:String){
        Glide.with(instance).load(url).into(imageview)
    }
 }

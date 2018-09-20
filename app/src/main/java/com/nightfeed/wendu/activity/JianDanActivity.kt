package com.nightfeed.wendu.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Explode
import android.view.View
import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.JianDanFragment
import com.nightfeed.wendu.view.flowingdrawer.LeftDrawerLayout
import kotlinx.android.synthetic.main.activity_jiandan.*


class JianDanActivity : AppCompatActivity() {

    val instance by lazy { this }
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("无聊图","段子","妹子")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jiandan)
        setSupportActionBar(toolbar)

        viewList.add(JianDanFragment().setThemes("picture"))
        viewList.add(JianDanFragment().setThemes("joke"))
        viewList.add(JianDanFragment().setThemes("ooxx"))

       var mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.tabMode = TabLayout.MODE_FIXED
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        var explode= Explode()
        explode.duration=300
        window.enterTransition = explode

        setListener()

    }

    private fun setListener() {

        toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

    }

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

}

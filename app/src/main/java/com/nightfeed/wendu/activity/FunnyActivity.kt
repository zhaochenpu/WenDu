package com.nightfeed.wendu.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Explode
import android.view.View
import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.Game3DMFragment
import com.nightfeed.wendu.fragment.GamerSkyFragment
import com.nightfeed.wendu.fragment.JianDanFragment
import com.nightfeed.wendu.net.URLs
import kotlinx.android.synthetic.main.activity_funny.*


class FunnyActivity : AppCompatActivity() {

    val instance by lazy { this }
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("3dm囧图","游民囧图","无聊图")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funny)
        setSupportActionBar(toolbar)

        viewList.add(Game3DMFragment())
        viewList.add(GamerSkyFragment())
        viewList.add(JianDanFragment().setThemes(URLs.JIANDAN_HOT))
//        viewList.add(JianDanFragment().setThemes(URLs.JIANDAN_MEIZI))

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

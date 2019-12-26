package com.nightfeed.wendu.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.transition.Explode
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.QingMangFragment
import com.nightfeed.wendu.fragment.ZhiHuHomeFragment
import kotlinx.android.synthetic.main.activity_woshipm.*
import java.util.*

class MagazineActivity : AppCompatActivity() {

    val instance by lazy { this }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("知乎日报","轻芒杂志")
    private var zhiHuHomeFragment=ZhiHuHomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woshipm)
        initView()
    }

    private fun initView() {
        toolbar.title="杂志"
        viewList.add(zhiHuHomeFragment)
        viewList.add(QingMangFragment())

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(ViewpagerOnPageChangeListener(tabs))
        tabs.tabMode = TabLayout.MODE_FIXED
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        toolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

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

    inner class ViewpagerOnPageChangeListener(tabs: TabLayout) : TabLayout.TabLayoutOnPageChangeListener(tabs){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==122&&resultCode==RESULT_OK){
            if(container.currentItem==0){
                zhiHuHomeFragment.setRead()
            }
        }
    }

}



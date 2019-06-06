package com.nightfeed.wendu.activity


import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import com.nightfeed.wendu.R
import com.nightfeed.wendu.utils.ToastUtil
import android.support.v4.view.PagerAdapter
import com.nightfeed.wendu.utils.StatusBarUtil
import android.transition.Explode
import android.view.Menu
import com.nightfeed.wendu.fragment.FinancialFragment
import com.nightfeed.wendu.fragment.WoShiPMHotFragment
import kotlinx.android.synthetic.main.activity_woshipm.*

class FinancialMActivity : AppCompatActivity() {

    val instance by lazy { this }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("要闻","自选")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woshipm)
        initView()
    }

    private fun initView() {
        toolbar.title="财经资讯"
        viewList.add(FinancialFragment().setType(0))
        viewList.add(FinancialFragment().setType(1))

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

    inner class ViewpagerOnPageChangeListener(tabs:TabLayout) : TabLayout.TabLayoutOnPageChangeListener(tabs){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }
 }

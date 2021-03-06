package com.nightfeed.wendu.activity


import android.content.Intent
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nightfeed.wendu.R
import com.nightfeed.wendu.utils.ToastUtil
import androidx.viewpager.widget.PagerAdapter
import com.nightfeed.wendu.utils.StatusBarUtil
import android.transition.Explode
import com.nightfeed.wendu.fragment.WoShiPMHotFragment
import kotlinx.android.synthetic.main.activity_woshipm.*

class WoShiPMActivity : AppCompatActivity() {

    val instance by lazy { this }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("周热门","月热门")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_woshipm)
        initView()
    }

    private fun initView() {
        toolbar.title="人人都是产品经理"
        viewList.add(WoShiPMHotFragment().setThemes(1))
        viewList.add(WoShiPMHotFragment().setThemes(2))

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

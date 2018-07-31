package com.nightfeed.wendu.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.MainMenuFragment
import com.nightfeed.wendu.fragment.ZhiHuHomeFragment
import com.nightfeed.wendu.fragment.ZhiHuThemesFragment
import com.nightfeed.wendu.view.flowingdrawer.FlowingView
import com.nightfeed.wendu.view.flowingdrawer.LeftDrawerLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val instance by lazy { this }
    var mLeftDrawerLayout: LeftDrawerLayout?=null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= arrayOf("首页","推荐","设计","财经","电影","心理学","大公司","不许无聊","互联网安全")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mLeftDrawerLayout = findViewById<View>(R.id.main_drawerlayout) as LeftDrawerLayout
        var mMenuFragment = MainMenuFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_menu, mMenuFragment ).commit()
        val mFlowingView = findViewById<View>(R.id.main_flowing) as FlowingView
        mLeftDrawerLayout!!.setFluidView(mFlowingView)
        mLeftDrawerLayout!!.setMenuFragment(mMenuFragment)

        viewList.add(ZhiHuHomeFragment())
        viewList.add(ZhiHuThemesFragment().setThemes(12))
        viewList.add(ZhiHuThemesFragment().setThemes(4))
        viewList.add(ZhiHuThemesFragment().setThemes(6))
        viewList.add(ZhiHuThemesFragment().setThemes(3))
        viewList.add(ZhiHuThemesFragment().setThemes(13))
        viewList.add(ZhiHuThemesFragment().setThemes(5))
        viewList.add(ZhiHuThemesFragment().setThemes(11))
        viewList.add(ZhiHuThemesFragment().setThemes(10))

       var mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))


        setListener()

    }

    private fun setListener() {

        toolbar.setNavigationOnClickListener { mLeftDrawerLayout!!.toggle() }

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
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE
        }
    }


    override fun onBackPressed() {

        if (mLeftDrawerLayout!!.isShownMenu()) {
            mLeftDrawerLayout!!.toggle()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==122&&resultCode==RESULT_OK){
            var f=viewList[container.currentItem]
            if(f is ZhiHuHomeFragment){
                f.setRead()
            }else if(f is ZhiHuThemesFragment){
                f.setRead()
            }
        }
    }
}

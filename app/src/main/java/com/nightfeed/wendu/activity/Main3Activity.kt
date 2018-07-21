package com.nightfeed.wendu.activity

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.HuaBanFragment
import com.nightfeed.wendu.fragment.LofterFragment
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {

    val instance by lazy { this }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        initView()
    }

    private fun initView() {
        viewList.add(HuaBanFragment())
        tabTitles.add("花瓣")

        viewList.add(LofterFragment().setLabel("女神"))
        tabTitles.add("女神")

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.tabMode = TabLayout.MODE_FIXED
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        search_image.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                var label=search_image.text
                if (!TextUtils.isEmpty(label)&&(p1 == EditorInfo.IME_ACTION_SEND || p2 != null && p2.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchLofterImage(label.toString().trim())
                    return true
                }
                return false
            }
        })
    }

    fun searchLofterImage(label:String){
        if (!TextUtils.isEmpty(label)){
            RequestUtils.get(URLs.LOFTER_LEST+"1&labelId="+label,object :RequestUtils.OnResultListener{
                override fun onSuccess(result: String) {
                    var list=MyJSON.getJSONArray(result, "value")
                    if(list!=null&&list.length()>0){
                        viewList.add(LofterFragment().setLabel(label))
                        tabTitles.add(label)
                        mSectionsPagerAdapter?.notifyDataSetChanged()
                        container.currentItem=viewList.size-1
                    }else{
                        ToastUtil.showError(instance,"没有找到该标签...")
                    }
                }

                override fun onError() {
                    ToastUtil.showError(instance,"槽糕，请求失败了...")
                }
            })
        }

    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

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
    }

}

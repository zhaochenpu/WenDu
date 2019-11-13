package com.nightfeed.wendu.activity

import android.content.Intent
import android.os.Bundle

import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.nightfeed.wendu.R
import com.nightfeed.wendu.fragment.HuaBanFragment
import com.nightfeed.wendu.fragment.LofterFragment
import com.nightfeed.wendu.model.PictureItem
import com.nightfeed.wendu.net.MyJSON
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.net.URLs
import com.nightfeed.wendu.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_browse_picture.*
import org.litepal.LitePal
import android.transition.Explode
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.nightfeed.wendu.fragment.TuChongCourseFragment
import com.nightfeed.wendu.fragment.TuChongFragment
import com.nightfeed.wendu.model.TuChongCourse


class BrowsePictureActivity : AppCompatActivity() {

    val instance by lazy { this }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var viewList=ArrayList<Fragment>()
    private var tabTitles= ArrayList<String>()
    private var searchLabel=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_picture)

        initView()
    }

    private fun initView() {
        viewList.add(TuChongFragment().setLabel("图虫"))
        tabTitles.add("图虫")
        viewList.add(TuChongCourseFragment())
        tabTitles.add("教程")
        viewList.add(HuaBanFragment())
        tabTitles.add("花瓣")

        viewList.add(LofterFragment().setLabel("女神"))
        tabTitles.add("女神")

        LitePal.findAll(PictureItem::class.java).forEach {
            if(it.source==1){
                viewList.add(LofterFragment().setLabel(it.name))
                tabTitles.add(it.name)
            }else{
                viewList.add(TuChongFragment().setLabel(it.name))
                tabTitles.add(it.name)
            }
        }

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(ViewpagerOnPageChangeListener(tabs))
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        tabs.setupWithViewPager(container)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        search_source.setOnClickListener {
            if(TextUtils.equals(search_source.text,"Lofter")){
                search_source.text="图虫"
            }else{
                search_source.text="Lofter"
            }
        }

        search_image.setOnEditorActionListener(TextView.OnEditorActionListener { p0, p1, p2 ->
            var label=search_image.text.toString()
            if (!TextUtils.isEmpty(label)&&(p1 == EditorInfo.IME_ACTION_SEND || p2 != null && p2.getKeyCode() == KeyEvent.KEYCODE_ENTER)&&!TextUtils.equals(searchLabel,label)) {
                searchLabel=label
                if(TextUtils.equals(search_source.text,"Lofter")){
                    searchLofterImage(label.trim())
                }else{
                    searchTuChongImage(label.trim())
                }

                return@OnEditorActionListener true
            }
            false
        })

        back.setOnClickListener {
            finishAfterTransition()
        }
       more.setOnClickListener {
            val popupMenu = PopupMenu(this, more)
            //通过MenuInflater进行填充数据
            val mInflater = popupMenu.getMenuInflater()
            //把定义好的menuXML资源文件填充到popupMenu当中
            mInflater.inflate(R.menu.main, popupMenu.getMenu())
           popupMenu.setOnMenuItemClickListener(object:PopupMenu.OnMenuItemClickListener{
               override fun onMenuItemClick(p0: MenuItem?): Boolean {
                   when (p0?.itemId) {
                       R.id.action_delete ->{
                           LitePal.deleteAll(PictureItem::class.java, "name = ?",tabTitles.get(container.currentItem))
                           tabTitles.removeAt(container.currentItem)
                           viewList.removeAt(container.currentItem)
                           mSectionsPagerAdapter?.notifyDataSetChanged()
                           container.currentItem=0
                       }
                   }
                   return true
               }
           })
            popupMenu.show()
        }

        var explode= Explode()
        explode.duration=300
        window.enterTransition = explode
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
                        val item = PictureItem(label, 1)
                        item.save()
                    }else{
                        ToastUtil.showError(instance,"没有找到该标签...")
                    }
                    searchLabel=""
                }

                override fun onError() {
                    ToastUtil.showError(instance,"槽糕，请求失败了...")
                    searchLabel=""
                }
            })
        }
    }

    fun searchTuChongImage(label:String){
        if (!TextUtils.isEmpty(label)){
            RequestUtils.get(URLs.TUCHONG_SEARCH+label+"&page=1",object :RequestUtils.OnResultListener{
                override fun onSuccess(result: String) {
                    var list=MyJSON.getJSONObject(result, "data")?.getJSONArray("post_list")
                    if(list!=null&&list.length()>0){
                        viewList.add(TuChongFragment().setLabel(label))
                        tabTitles.add(label)
                        mSectionsPagerAdapter?.notifyDataSetChanged()
                        container.currentItem=viewList.size-1
                        val item = PictureItem(label, 2)
                        item.save()
                    }else{
                        ToastUtil.showError(instance,"没有找到该标签...")
                    }
                    searchLabel=""
                }

                override fun onError() {
                    ToastUtil.showError(instance,"槽糕，请求失败了...")
                    searchLabel=""
                }
            })
        }
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
            if(viewList[position] is LofterFragment){
                search_source.text="Lofter"
            }else if(viewList[position] is TuChongFragment){
                search_source.text="图虫"
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode==1111&&resultCode==RESULT_OK&&data!=null){
//            var label=data.getStringExtra("label")
//            for ( i in 0..(viewList.size-1)){
//                var f=viewList[i]
//                if( f is LofterFragment&&TextUtils.equals(f.getLabel(),label)){
//                    container.currentItem=i
//                    return
//                }
//            }
//
//            viewList.add(LofterFragment().setLabel(label))
//            tabTitles.add(label)
//            mSectionsPagerAdapter?.notifyDataSetChanged()
//            container.currentItem=viewList.size-1
//            val item = PictureItem(label, 1)
//            item.save()
//        }
//    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        var newLabel= intent?.getStringExtra("label")
        if(!TextUtils.isEmpty(newLabel)){
            for ( i in 0..(viewList.size-1)){
                var f=viewList[i]
                if( f is LofterFragment&&TextUtils.equals(f.getLabel(),newLabel)){
                    container.currentItem=i
                    return
                }
            }

            viewList.add(LofterFragment().setLabel(newLabel!!))
            tabTitles.add(newLabel)
            mSectionsPagerAdapter?.notifyDataSetChanged()
            container.currentItem=viewList.size-1
            val item = PictureItem(newLabel, 1)
            item.save()
        }
    }
}

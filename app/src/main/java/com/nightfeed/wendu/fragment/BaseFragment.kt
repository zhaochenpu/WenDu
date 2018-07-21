package com.nightfeed.wendu.fragment

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment()  {

    var isFragmentVisible:Boolean = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint){
            isFragmentVisible=true
            onVisible()
        }else{
            isFragmentVisible=false
            onInvisible()
        }
    }

    abstract fun lazyLoad()

    fun  onInvisible(){}

    fun onVisible() {
        lazyLoad()
    }
}
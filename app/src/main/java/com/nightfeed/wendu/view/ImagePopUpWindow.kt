package com.nightfeed.wendu.view

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.nightfeed.wendu.R
import com.nightfeed.wendu.net.RequestUtils
import com.nightfeed.wendu.utils.SystemUtil
import com.nightfeed.wendu.utils.ToastUtil
import java.net.URL

class ImagePopUpWindow (activity: Activity,parentView: View?,url: String){

//    interface OnEvenListener{
//        fun onShare() {}
//
//        fun onDownload(){}
//    }

    init {

        var contentView = LayoutInflater.from(activity).inflate(R.layout.image_item_popwindow, null)
        var mPopWindow = PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        contentView.findViewById<View>(R.id.item_pop_share).setOnClickListener {
            RequestUtils.getPic(url,object :RequestUtils.OnResultListener{
                override fun onError() {
                }

                override fun onSuccess(result: String) {
                    SystemUtil.shareBitmap(activity, Uri.parse(result))
                }
            })
            mPopWindow.dismiss()
        }

        contentView.findViewById<View>(R.id.item_pop_down).setOnClickListener {
            RequestUtils.getPic(url,object :RequestUtils.OnResultListener{
                override fun onError() {
                    ToastUtil.showError(activity,"保存失败")
                }

                override fun onSuccess(result: String) {
                   ToastUtil.showSuccess(activity,"保存成功")
                }
            })
            mPopWindow.dismiss()
        }



        mPopWindow.isOutsideTouchable = true
        mPopWindow.animationStyle = R.style.pop_anim_style
        mPopWindow.setBackgroundDrawable(BitmapDrawable())

        if(parentView!=null){
            val xy = IntArray(2)
            parentView.getLocationOnScreen(xy)
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            mPopWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, xy[0] + (parentView.width - contentView.measuredWidth) / 2, xy[1]-contentView.measuredHeight)
        }else{
            mPopWindow.showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0)
        }

    }
}
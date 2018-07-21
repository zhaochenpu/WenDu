package com.nightfeed.wendu.utils

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.TextView
import com.nightfeed.wendu.R


class ToastUtil{
    companion object {
        fun showError(context:Context,text:String){
            var toast=Toast(context)
            toast.view= LayoutInflater.from(context).inflate(R.layout.toast, null)
            var textview=toast.view.findViewById(R.id.toast) as TextView
            textview.text=text
            textview.setTextColor(Color.WHITE)
            toast.view.setBackgroundResource(R.drawable.toast_error)
            toast.duration=Toast.LENGTH_SHORT
            toast.show()
        }

        /**
         * 设置Toast字体及背景
         * @param messageColor
         * @param background
         * @return
         */
        fun setToastBackground(toast:Toast,messageColor: Int, background: Int): Toast {
            val view = toast.getView()
            if (view != null) {
                val message = view!!.findViewById(android.R.id.message) as TextView
                view.setBackgroundResource(background)
                message.setTextColor(messageColor)
            }
            return toast
        }
    }


}
package com.nightfeed.wendu.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK

class NoADAccessibilityService  : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val source = event.source ?: return
        for (i in 0 until source.childCount) {
            if (source.getChild(i)?.text?.contains("跳过") == true) {
                source.getChild(i).performAction(ACTION_CLICK)
            }
        }
        source.recycle()
    }

    override fun onInterrupt() {
    }


}
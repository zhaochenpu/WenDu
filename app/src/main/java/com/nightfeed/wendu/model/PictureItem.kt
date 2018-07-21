package com.nightfeed.wendu.model

import org.litepal.crud.LitePalSupport

//source 0 花瓣 1lofter 2 图虫
class PictureItem(val name: String, val source: Int) : LitePalSupport() {
    val id: Long = 0
}
package com.nightfeed.wendu.net

class URLs {

    companion object {
        val  HUA_BAN_BEAUTY_LIST="http://api.huaban.com/favorite/beauty?limit=20"
        val HUA_BAN_IM="http://img.hb.aicdn.com/"
        val HUA_BAN_RECOMMEND="https://api.huaban.com/pins/pin_id/recommend/?page=1&limit=10"
        val HUA_BAN_SEARCH="http://api.huaban.com/search/favorite/?q="//&page=1&per_page=20

        val LOFTER_LEST="http://reader.meizu.com/android/unauth/img/labelImgList?page=" //1&labelId=
        val LOFTER_DETAILS="http://www.lofter.com/meizu/singlepost.do?permalink="

        //page：翻页值。从 1 开始。需要搭配 json 中的 pose_id 字段使用。可为空
//        post_id：如果是第一页则不需要添加该字段。否则，需要加上该字段，该字段的值为上一页最后一个 json 中的 post_id 值
//        type：如果是第一页则是 refresh，如果是加载更多，则是 loadmore。可为空
        val TUCHONG_RECOMMEND="https://api.tuchong.com/feed-app?"//page=1&type=refresh
        val TUCHONG_IMAGE="https://photo.tuchong.com/ "//+ user_id +/f/ + img_id+.webp
        val TUCHONG_SEARCH="https://tuchong.com/rest/search/posts?count=20&query="//绝对领域&page=2  m小图 f大图

        val ONE_NEW="http://v3.wufazhuce.com:8000/api/hp/idlist/0?version=3.5.0&platform=android"
        val ONE_HP="http://v3.wufazhuce.com:8000/api/hp/detail/hp_id?version=4.0.2&platform=android"

        val MONO_POETRY="http://mmmono.com/api/v3/group/100051/content/kind/1"  //HTTP-AUTHORIZATION 2e5ccb3d7f5211e8a6e55254006fe942 需要加头
        val MONO_POETRY_MORE="http://mmmono.com/api/v3/group/100051/content/kind/1/?start="

        val HIMAWERI8_LATES="https://himawari8-dl.nict.go.jp/himawari8/img/D531106/latest.json?uid="
        val HIMAWERI8_IMAGE4="https://himawari8-dl.nict.go.jp/himawari8/img/D531106/2d/550/"//2018/07/02/120000_1_1.png


    }

}
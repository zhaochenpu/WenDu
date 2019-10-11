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
        val TUCHONG_RECOMMEND="https://api.tuchong.com/feed-app?page="//1&type=refresh
       //图虫热门 https://api.tuchong.com/discover/-4/category?language=zh&resolution=1080*1920&device_type=A0001&device_platform=android&os_api=23&device_brand=ONEPLUS&openudid=61ddb2437c596909&_rticket=1565335276064&version_code=5090&version_name=5.9.0&ac=wifi&aid=1130&dpi=480&iid=82124606830&page=1&uuid=864587029916013&device_id=42110213649&count=20&ssmix=a&before_timestamp=0&os_version=6.0.1&channel=tuchong&app_name=tuchong&update_version_code=5090&manifest_version_code=5090
       val TUCHONG_COURSE="https://tuchong.com/rest/sites/1615439,1615432,1615443,1615461,1615437/posts?count=10&page="
        val TUCHONG_IMAGE="https://photo.tuchong.com/"//+ user_id +/f/ + img_id+.webp
        val TUCHONG_SEARCH="https://tuchong.com/rest/search/posts?count=20&query="//&page=2  m小图 f大图

//        val ONE_NEW="http://v3.wufazhuce.com:8000/api/hp/idlist/0?version=3.5.0&platform=android"
        var ONE_HP="http://v3.wufazhuce.com:8000/api/hp/bydate/TIME?channel=mx&sign=f6b71effc73b6cec54a4643e54aaaafd&version=4.5.9&uuid=ffffffff-80e5-ca46-a71d-832d0033c587&platform=android"
//        val ONE_HP="http://v3.wufazhuce.com:8000/api/hp/detail/hp_id?version=4.0.2&platform=android"

        val MONO_POETRY="http://mmmono.com/api/v3/group/100051/content/kind/1"  //HTTP-AUTHORIZATION 2e5ccb3d7f5211e8a6e55254006fe942 需要加头
        val MONO_POETRY_MORE="http://mmmono.com/api/v3/group/100051/content/kind/1/?start="

        val ZHIHU_LATEST="https://news-at.zhihu.com/api/4/news/latest"
        val ZHIHU_BEFORE="https://news-at.zhihu.com/api/4/news/before/"//+日期
        val ZHIHU_CONTENT="https://news-at.zhihu.com/api/4/news/"//+id
        val ZHIHU_THEME="https://news-at.zhihu.com/api/4/theme/"//+id
//        https://news-at.zhihu.com/api/4/theme/#{theme_id}/before/#{story_id}

        val HIMAWERI8_LATES="https://himawari8-dl.nict.go.jp/himawari8/img/D531106/latest.json?uid="
        val HIMAWERI8_IMAGE4="https://himawari8-dl.nict.go.jp/himawari8/img/D531106/2d/550/"//2018/07/02/120000_1_1.png

        val YOUDAO_DAILY_SENTENCE="http://dict.youdao.com/infoline/style?client=mobile&style=daily&order=desc&size=15&keyfrom=mdict.7.7.5.android&screen=1080x2160&lastId="
        val YOUDAO_DAILY_WORD="http://dict.youdao.com/infoline/style?client=mobile&style=youdaodaily%401632&order=desc&size=10&keyfrom=mdict.7.7.5.android&screen=1080x1920&lastId="

        val JIANDAN_HOT="http://api.moyu.today/jandan/hot?category="// picture无聊图 joke段子 ooxx妹子

        val WEIBO_HOME_TIMELINE="https://api.weibo.com/2/statuses/home_timeline.json?access_token="


        val JU_DU="https://judouapp.com/api/v6/op/sentences/daily?page=1&per_page=30&timestamp=1543877307&app_key=2a438661-92c0-4a2d-b32e-3fd0c47a0a3c&platform=android&channel=ch_wandoujia&version_code=480&version=v3.7.1&system_version=23&device_type=A0001&device_id=61ddb2437c596909&signature=246288337e359948bc518d4adaa6e34d"
        val JU_TUI="https://judouapp.com/api/v6/op/channels/11?per_page=20&timestamp=1543877518&app_key=2a438661-92c0-4a2d-b32e-3fd0c47a0a3c&platform=android&channel=ch_wandoujia&version_code=480&version=v3.7.1&system_version=23&device_type=A0001&device_id=61ddb2437c596909&signature=6a93cdddfacab16c4eac58cc74107856&page=1"

        val WOSHIPM_HOME="http://api.woshipm.com/news/v4/index/timeline.html?PN=1&PS=20&LTime=&_CS=0&_cT=Android&_cV=4.1.6&_cP=1080*1920&_cA=PM"
        val WOSHIPM_WEEKHOT="http://api.woshipm.com/news/v4/hotList.html?type=7&PS=20&_cT=Android&_cV=4.1.6&_cP=1080*1920&_cA=PM&PN="
        val WOSHIPM_MONTHHOT="http://api.woshipm.com/news/v4/hotList.html?type=30&PS=20&_cT=Android&_cV=4.1.6&_cP=1080*1920&_cA=PM&PN="

        val FINANCIAL_NEWS="https://emdcnewsapp.eastmoney.com/infoService"
        val FINANCIAL_OPTIONAL=" https://emdcadvise.eastmoney.com/infoAdviseService"
        val FINANCIAL_DETAIL="https://emwap.eastmoney.com/info/detail/"

        val WEATHER_HUAFENG="https://mpv2.weather.com.cn/v2/?lat="//40.00968668619792&lng=116.35044596354167
        val FEI_DI="https://app.enclavebooks.cn/v1_1/sign?page="
    }

}
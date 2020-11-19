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

        val QING_MANG_LIST="https://api.qingmang.me/ng/v1/timeline.explore.get?group_article=true&need_hot_post=true&need_ops_magazine=true&token=ZTgwYTRkYjAtMjZlZC0xMWVhLWI3MWQtMDAxNjNlMzI2OTg3&app_id=wx05790b1180642960&apivc=3&timeline_style=flat&platform=life_mina"
        val QING_MANG_CONTENT="https://api.qingmang.me/ng/v1/article.get?template=raml&check_paid=false&app_id=wx05790b1180642960&token=ZTgwYTRkYjAtMjZlZC0xMWVhLWI3MWQtMDAxNjNlMzI2OTg3&pub_id=null&apivc=3&timeline_style=flat&platform=life_mina&docid="

        val SOLVER_SENTENCE="https://wowo.zero.qq.com/v2/feed/listbytag?page=10&tag_id=1050&filter=0&offset="

        val HIMAWERI8_LATES="https://himawari8-dl.nict.go.jp/himawari8/img/D531106/latest.json?uid="
        val HIMAWERI8_IMAGE4="https://himawari8-dl.nict.go.jp/himawari8/img/D531106/2d/550/"//2018/07/02/120000_1_1.png

        val YOUDAO_DAILY_SENTENCE="http://dict.youdao.com/infoline/style?client=mobile&style=daily&order=desc&size=15&keyfrom=mdict.7.7.5.android&screen=1080x2160&lastId="
        val YOUDAO_DAILY_WORD="http://dict.youdao.com/infoline/style?client=mobile&style=youdaodaily%401632&order=desc&size=10&keyfrom=mdict.7.7.5.android&screen=1080x1920&lastId="

        val JIANDAN_HOT="http://api.moyu.today/jandan/hot?category=picture"// 无聊图 joke段子 ooxx妹子
        val JIANDAN_MEIZI="https://i.jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=1"// 妹子图

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
        val WEATHER_MEIZU="http://aider.meizu.com/v5/weather/citylbs?country=%E4%B8%AD%E5%9B%BD&province=%E5%8C%97%E4%BA%AC%E5%B8%82&city=%E5%8C%97%E4%BA%AC%E5%B8%82&region=%E6%B5%B7%E6%B7%80%E5%8C%BA&street=%E5%AD%A6%E6%B8%85%E8%B7%AF&bizId=aider_app&timestamp=1559626262393&sign=60a104873eabb5e03e544f1736445a61&lat="//40.009898&lon=116.35057

        val FEI_DI="https://app.enclavebooks.cn/v1_1/sign?page="

        val GAMESKY_FUNNY="http://appapi2.gamersky.com/v2/AllChannelList"
        val GAMESKY_FUNNY_CONTENT="https://wap.gamersky.com/news/Content-"

        val GAME_3DM_AMUSEPAGE="https://my.3dmgame.com/app/amusepage2"

        val YIYAN_SENTENCE="http://115.28.168.103:8080/yiyan/crosstime?v=3.35&cross=1&datetime="//http://115.28.168.103:8080/yiyan/crosstime?v=3.35&cross=1&datetime=2020-05-11%2000%3A00%3A00
    }

}
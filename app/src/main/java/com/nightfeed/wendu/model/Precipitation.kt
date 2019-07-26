package com.nightfeed.wendu.model

data class Precipitation (var  rainTips:String,var datas:List<RainData>){
    data class RainData (var value :Int, var time :String )
}
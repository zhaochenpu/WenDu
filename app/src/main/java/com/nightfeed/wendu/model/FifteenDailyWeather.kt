package com.nightfeed.wendu.model

data class FifteenDailyWeather (var tips :String ,var dailies:List<DailyWeather>){

    data class DailyWeather (var tempMax :String ,var tempMin :String ,var dayWeather :String ,var nightWeather :String
                             ,var week :String,var date :String,var sunDown :String,var sunRise :String,var aqi :String,var aqiDes :String){
    }
}

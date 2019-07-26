package com.nightfeed.wendu.model

data class Weather(var realtimeWeather:RealtimeWeather, var air: Air,var precipitation:Precipitation?, var hourlyWeather: List<HourlyWeather>?, var fifteenDailyWeather:FifteenDailyWeather?
                   , var alarm:List< Alarm>?) {
}
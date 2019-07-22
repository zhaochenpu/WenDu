package com.nightfeed.wendu.model

data class Weather(var realtimeWeather:RealtimeWeather, var air: Air, var hourlyWeather: HourlyWeather?, var dailyWeather: DailyWeather?, var alarm: Alarm?) {
}
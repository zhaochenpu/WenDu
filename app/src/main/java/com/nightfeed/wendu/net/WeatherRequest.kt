package com.nightfeed.wendu.net

import com.nightfeed.wendu.model.*
import org.json.JSONObject

class WeatherRequest {
    interface OnResultListener{
        fun onSuccess(weather: Weather)
        fun onError()
    }

    companion object {
        fun get(latitude :Double,longitude :Double,onRequestListener:OnResultListener){
            RequestUtils.get(URLs.WEATHER_HUAFENG+latitude+"&lng="+longitude,object :RequestUtils.OnResultListener{
                override fun onSuccess(result: String) {
                    var data=JSONObject(result)
                    var observe=data.getJSONObject("observe")
                    var precipitation=data.getJSONObject("precipitation")

                    var realtimeWeather=RealtimeWeather(MyJSON.getString(observe,"weatherText"),MyJSON.getString(observe,"temperature")
                    ,"",MyJSON.getString(observe,"windlevel"),MyJSON.getString(observe,"wind")
                            ,MyJSON.getString(observe,"humidity"))

                    var rains=ArrayList<Precipitation.RainData>()
                    var rainValues=precipitation.getJSONArray("values")
                    var rainTimes=precipitation.getJSONArray("times")
                    for (i in 0 until rainValues.length()){
                        rains.add(Precipitation.RainData(rainValues.getInt(i),rainTimes.getString(i)))
                    }

                    var air=Air(MyJSON.getString(observe,"aqi"),MyJSON.getString(observe,"aqid"),MyJSON.getString(observe,"pm25")
                           ,MyJSON.getString(observe,"pm10"),MyJSON.getString(observe,"no2"),MyJSON.getString(observe,"o3")
                           ,MyJSON.getString(observe,"so2"),MyJSON.getString(observe,"co"))

                    var dailyResult=data.getJSONObject("daily")
                    var dailyWeather=ArrayList<FifteenDailyWeather.DailyWeather>()

                    dailyResult.keys().forEach {
                        var day=dailyResult.getJSONObject(it)
                        var dayTemp=day.getJSONArray("temperature")
                        var dayWeatherDes=day.getJSONArray("weatherDescription")
                        var sun=MyJSON.getJSONObject(day,"sun")
                        var air=MyJSON.getJSONObject(day,"air")

                        dailyWeather.add(FifteenDailyWeather.DailyWeather(dayTemp.getString(0),dayTemp.getString(1),dayWeatherDes.getString(0),dayWeatherDes.getString(1)
                        ,MyJSON.getString(day,"dayofweek"),MyJSON.getString(day,"shortday"),MyJSON.getString(sun,"rise"),MyJSON.getString(sun,"set")
                        ,MyJSON.getString(air,"aqi"),MyJSON.getString(air,"aqic")))
                    }

                    var fifteenDailyWeather= FifteenDailyWeather(MyJSON.getString(MyJSON.getJSONArray(data,"tips")?.getJSONObject(0),"tipsTrend"),dailyWeather)

                    onRequestListener.onSuccess(Weather(realtimeWeather,air,Precipitation(MyJSON.getString(precipitation,"msg"), rains)
                            ,null,fifteenDailyWeather,null))
                }

                override fun onError() {
                    onRequestListener.onError()
                }
            })
        }
    }

}
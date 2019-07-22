package com.nightfeed.wendu.net

import com.nightfeed.wendu.model.Air
import com.nightfeed.wendu.model.RealtimeWeather
import com.nightfeed.wendu.model.Weather
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
                    var realtimeWeather=RealtimeWeather(MyJSON.getString(observe,"weatherText"),MyJSON.getString(observe,"temperature")
                    ,"",MyJSON.getString(observe,"windlevel"),MyJSON.getString(observe,"wind"),MyJSON.getString(observe,"humidity"))

                   var air=Air(MyJSON.getString(observe,"aqi"),MyJSON.getString(observe,"aqid"),MyJSON.getString(observe,"pm25")
                           ,MyJSON.getString(observe,"pm10"),MyJSON.getString(observe,"no2"),MyJSON.getString(observe,"o3")
                           ,MyJSON.getString(observe,"so2"),MyJSON.getString(observe,"co"))

                    onRequestListener.onSuccess(Weather(realtimeWeather,air,null,null,null))
                }

                override fun onError() {
                    onRequestListener.onError()
                }
            })
        }
    }

}
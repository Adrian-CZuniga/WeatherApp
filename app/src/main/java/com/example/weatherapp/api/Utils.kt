package com.example.weatherapp.api

import com.example.weatherapp.BuildConfig

class Utils {
    companion object{
        var BASE_URL : String = "https://api.openweathermap.org/data/2.5/"
        var API_KEY: String = BuildConfig.WEATHER_API_KEY
    }
}
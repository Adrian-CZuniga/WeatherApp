package com.example.weatherapp.main

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.weatherapp.MyPreferences
import com.example.weatherapp.WeatherModel
import com.example.weatherapp.api.JsonResponse
import com.example.weatherapp.api.Utils
import com.example.weatherapp.api.service
import com.example.weatherapp.database.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainRepository (private val database: WeatherDatabase, private val context: Context){

    suspend fun fetchLastLocation(location: Location) {
        return withContext(Dispatchers.IO) {
            val weatherJson = service.getUserLastLocationWeather(lat = location.latitude, lon = location.longitude, apiKey = Utils.API_KEY)
            val weather = parseLastWeatherLocation(weatherJson)
            saveCountryCode(weather.country)

            database.dao.insertWeather(weather)
        }
    }

     suspend fun refreshWeathers(weatherList: MutableList<WeatherModel>){
        return withContext(Dispatchers.IO) {
            for (weather in weatherList) {
                val weatherJson = service.getUserLastLocationWeather(weather.lat, weather.lon, Utils.API_KEY)
                val weather = parseLastWeatherLocation(weatherJson)
                database.dao.insertWeather(weather)
            }
        }
    }



    private fun saveCountryCode(country:String) {
        val myPreferences = MyPreferences(context)
        myPreferences.saveCountry(country)
    }

    private fun parseLastWeatherLocation(weatherJsonResponse: Response<JsonResponse>): WeatherModel {
        val actualWeatherLocation = WeatherModel()

        if (weatherJsonResponse.body() != null) {
            actualWeatherLocation.id = weatherJsonResponse.body()?.id.toString()
            actualWeatherLocation.lat = weatherJsonResponse.body()!!.coord.lat
            actualWeatherLocation.lon = weatherJsonResponse.body()!!.coord.lon
            actualWeatherLocation.country = weatherJsonResponse.body()!!.sys.country
            actualWeatherLocation.city = weatherJsonResponse.body()!!.name
            actualWeatherLocation.temperature = weatherJsonResponse.body()!!.main.temp
            actualWeatherLocation.description = weatherJsonResponse.body()!!.weather[0].description
            actualWeatherLocation.tempMin = weatherJsonResponse.body()!!.main.temp_min
            actualWeatherLocation.tempMax = weatherJsonResponse.body()!!.main.temp_max
            actualWeatherLocation.humidity = weatherJsonResponse.body()!!.main.humidity
            actualWeatherLocation.icon = weatherJsonResponse.body()!!.weather[0].icon

        } else{
            actualWeatherLocation.id = "0"
        }
        return actualWeatherLocation
    }




}
package com.example.weatherapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weathers")
data class WeatherModel(
    @PrimaryKey var id: String = "",
    var country: String= "",
    var city: String= "",
    var temperature: Double = 0.0,
    var description: String = "",
    var tempMin: Double = 0.0,
    var tempMax:Double = 0.0,
    var lat: Double = 0.0, // Agrega latitud como atributo
    var lon: Double = 0.0, // Agrega longitud como atributo
    var humidity: Int = 0,
    var icon: String = ""
){

    fun kelvinToFarenheit(temp:Double): Double{
        return ((temp - 273.15)*1.8)+32
    }

    fun kelvinToCelsius(temp:Double): Double{
        return temp - 273.15
    }
}


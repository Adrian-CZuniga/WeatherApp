package com.example.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.WeatherModel

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherModel: WeatherModel)

    @Query("SELECT * FROM weathers")
    fun getWeathers(): LiveData<MutableList<WeatherModel>>

    @Query("SELECT * FROM weathers WHERE lat = :lat AND lon = :lon")
    fun getWeatherByLocation(lat: Double, lon: Double): LiveData<WeatherModel>

}
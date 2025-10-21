package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.WeatherModel

@Database(entities = [WeatherModel::class], version = 2)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val dao: WeatherDao
}

private lateinit var INSTANCE: WeatherDatabase

fun getDataBase(context: Context): WeatherDatabase {
    synchronized(WeatherDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
            "weather_db"
            ).fallbackToDestructiveMigration().build()
        }
        return INSTANCE
    }
}

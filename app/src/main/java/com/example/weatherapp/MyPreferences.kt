package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class MyPreferences(context:Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)

    fun saveLocation(value1: Double, value2: Double) {
        sharedPreferences.edit {
            putLong("latitude", value1.toRawBits())
            putLong("longitude", value2.toRawBits())
        }
    }

    fun saveCountry(country: String){
        sharedPreferences.edit {
            putString("country", country)
        }
    }

    fun getPrefsLat(): Double {
        val rawValue = sharedPreferences.getLong("latitude", 0L)
        return if (rawValue != 0L) java.lang.Double.longBitsToDouble(rawValue) else 0.0
    }

    fun getPrefsLon(): Double {
        val rawValue = sharedPreferences.getLong("longitude", 0L)
        return if (rawValue != 0L) java.lang.Double.longBitsToDouble(rawValue) else 0.0
    }

    fun getPrefsCountry(): String? {
        return sharedPreferences.getString("country", null)
    }

    fun deleteAllPreferences(){
        sharedPreferences.edit { clear() }
    }
}
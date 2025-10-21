package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences

class MyPreferences(context:Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)

    fun saveLocation(value1: Double, value2: Double) {
        val editor = sharedPreferences.edit()
        editor.putLong("latitude", value1.toRawBits())
        editor.putLong("longitude", value2.toRawBits())
        editor.apply()
    }

    fun saveCountry(country: String){
        val editor = sharedPreferences.edit()
        editor.putString("country", country)
        editor.apply()
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
        sharedPreferences.edit().clear().apply()
    }
}
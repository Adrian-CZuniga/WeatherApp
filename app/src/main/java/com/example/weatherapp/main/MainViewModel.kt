package com.example.weatherapp.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.AndroidUtil
import com.example.weatherapp.database.getDataBase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.net.UnknownHostException
import kotlinx.coroutines.*
import com.example.weatherapp.MyPreferences
import com.example.weatherapp.WeatherModel
import com.example.weatherapp.api.ApiRefreshStatus
import java.io.IOException
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.math.roundToLong


private val TAG = MainViewModel::class.java.simpleName
class MainViewModel(application: Application): AndroidViewModel(application) {
    private val context = application.applicationContext

    private val database = getDataBase(context)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val myPreferences = MyPreferences(context)
    private val repository = MainRepository(database, context)

    val coords: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val actualLocationWeather: LiveData<WeatherModel> =
        coords.switchMap() { (lat, lon) ->
            database.dao.getWeatherByLocation(lat, lon)
        }
    val weatherList: LiveData<MutableList<WeatherModel>> = database.dao.getWeathers()

    init {
        getLastLocation()
    }

    fun refreshLocation(weatherList:MutableList<WeatherModel>){
        viewModelScope.launch {
            repository.refreshWeathers(weatherList)
        }
    }

    private fun getLastLocation(){
        viewModelScope.launch {
            try {
                val location = getLocationSensor()
                myPreferences.saveLocation(location.latitude,location.longitude)
                coords.value = Pair(location.latitude, location.longitude)
                repository.fetchLastLocation(location)
            } catch (e: UnknownHostException){
                Log.d(TAG, "No internet conection: ${e}")
                Toast.makeText(context, "No hay internet para descargar nuevos datos.", Toast.LENGTH_SHORT).show()
                getLastLocationWithSharedPreferences()
            }
        }
    }

    private fun getLastLocationWithSharedPreferences(){
        viewModelScope.launch {
            val country = myPreferences.getPrefsCountry()
            if (country != null) {
                val latitude = myPreferences.getPrefsLat()
                val longitude = myPreferences.getPrefsLon()
                coords.value = Pair(latitude, longitude)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocationSensor(): Location {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val location = Location("provider")
        location.latitude = 25.6801
        location.longitude = -100.4103
        try {
            fusedLocationClient.lastLocation.addOnCompleteListener {
                if (it.isSuccessful) {
                    location.latitude = it.result?.latitude?: 25.6801
                    location.longitude = it.result?.longitude?: -100.4103
                }
            }
        }catch (e: UnknownError) {
            Toast.makeText(context, "$e", Toast.LENGTH_LONG).show()
        }
        return location
    }
}
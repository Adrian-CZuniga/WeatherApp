package com.example.weatherapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.WeatherModel
import com.example.weatherapp.databinding.LayoutWeatherItemRecyclerBinding
import kotlin.math.roundToInt

class WeatherAdapter (context:Context) : ListAdapter<WeatherModel, WeatherAdapter.WeatherViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<WeatherModel>() {
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.WeatherViewHolder{
        val binding = LayoutWeatherItemRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherAdapter.WeatherViewHolder, position: Int) {
        val weatherModel = getItem(position)
        holder.bind(weatherModel)
    }

    inner class WeatherViewHolder(private val binding: LayoutWeatherItemRecyclerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(weatherModel: WeatherModel){
            binding.countryTxt.text = weatherModel.country
            binding.cityTxt.text = weatherModel.city
            binding.temperatureTxt.text = weatherModel.kelvinToCelsius(weatherModel.temperature).roundToInt().toString() + "°"
            binding.temperatureMinMaxTxt.text = "${weatherModel.kelvinToCelsius(weatherModel.tempMin).roundToInt()}° - ${weatherModel.kelvinToCelsius(weatherModel.tempMax).roundToInt()}°"
            binding.descriptionWeatherTxt.text = weatherModel.description
            binding.imageWeatherReference.load("https://openweathermap.org/img/wn/${weatherModel.icon}@2x.png")

            binding.executePendingBindings()

        }
    }

}
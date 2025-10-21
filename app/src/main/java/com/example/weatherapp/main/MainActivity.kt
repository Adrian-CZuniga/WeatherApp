package com.example.weatherapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.AndroidUtil
import com.example.weatherapp.MyPreferences
import com.example.weatherapp.WeatherModel
import com.example.weatherapp.adapter.WeatherAdapter
import com.example.weatherapp.api.ApiRefreshStatus
import com.example.weatherapp.databinding.ActivityMainBinding

private val TAG = MainActivity::class.java.simpleName
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel= ViewModelProvider(this).get(MainViewModel::class.java)
        val adapter = WeatherAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.weatherList.observe(this, Observer {
            weatherList ->
            if (weatherList != null) {
                adapter.submitList(weatherList)
                handleEmptyView(weatherList)
            } else{
                val preferences = MyPreferences(this)
                preferences.deleteAllPreferences()
                Toast.makeText(this, "No se encontró el clima", Toast.LENGTH_SHORT).show()
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.weatherList.value?.let { viewModel.refreshLocation(weatherList = it) }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun handleEmptyView(weatherList: MutableList<WeatherModel>) {
        if (weatherList.isEmpty()) {
            binding.txtEmptyList.visibility = View.VISIBLE
        } else {
            binding.txtEmptyList.visibility = View.GONE
        }
    }


}
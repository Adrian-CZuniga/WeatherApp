package com.example.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.weatherapp.main.MainActivity
import com.example.weatherapp.permission.RequestPermission

class SplashScreen : AppCompatActivity() {
    private val permissionsToCheck = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val permissionsGranted = arePermissionsGranted(permissionsToCheck)

            if (permissionsGranted) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                val i = Intent(this, RequestPermission::class.java)
                i.putStringArrayListExtra("permissionsToRequest", ArrayList(permissionsToCheck))
                startActivity(i)
            }
            finish()
        }, 1500)
    }



    private fun arePermissionsGranted(permissions: List<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
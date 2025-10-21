package com.example.weatherapp.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.AndroidUtil
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityRequestPermissionBinding
import com.example.weatherapp.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class RequestPermission : AppCompatActivity() {
    private var CODE_REQUEST = 101
    private lateinit var binding: ActivityRequestPermissionBinding
    private var simpleNamePermission = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSkip.visibility = View.GONE
        val permissions = intent.getStringArrayListExtra("permissionsToRequest")
        simpleNamePermission = getSimpleNamePermission(permissions!![0])
        val imageCode = AndroidUtil.WeatherToImage[simpleNamePermission]

        val idDrawable = resources.getIdentifier(imageCode, "drawable", packageName)
        val drawable = ContextCompat.getDrawable(this, idDrawable)

        binding.requiredPermission.text = simpleNamePermission
        binding.imgPermissionRequest.setImageDrawable(drawable)

        binding.btnGrantPermission.setOnClickListener {
            val result = ContextCompat.checkSelfPermission(this, permissions[0]!!)
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permissions[0]!!), CODE_REQUEST)
            }
        }

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }
        binding.btnSkip.setOnClickListener {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
    //Metodo sobreescrito que permite controlar la respuesta del usuario a requestPermissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMainActivity()
            } else {
                binding.btnSkip.visibility = View.VISIBLE
                if (shouldShowRequestPermissionRationale(permissions[0])) {
                    AndroidUtil.showDialog(this, simpleNamePermission, "Para una mejor experiencia requerimos de este permiso.")
                } else {
                    AndroidUtil.showDialogYesOrNo(this, "Permisos", "¿Otorgar permiso desde configuración?", object: AndroidUtil.Companion.DialogClickListener{
                        override fun onPositiveButtonClick() {
                            AndroidUtil.showSettingsApp(this@RequestPermission)
                        }
                        override fun onNegativeButtonClick() {
                            AndroidUtil.showToast(this@RequestPermission, "Permiso denegado")
                        }
                    })
                }
            }
        }
    }

    private fun getSimpleNamePermission(permiso: String): String {
        val partes = permiso.split("_")
        return if (partes.size > 1) {
            partes[partes.size - 1]
        } else {
            permiso
        }
    }
}
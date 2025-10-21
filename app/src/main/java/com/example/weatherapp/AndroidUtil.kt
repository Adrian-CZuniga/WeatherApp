package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.location.LocationServices


class AndroidUtil {

    companion object{
        interface DialogClickListener {
            fun onPositiveButtonClick()
            fun onNegativeButtonClick()
        }
        //Dictionary to retrieve images by name using permissions.
        val WeatherToImage: Map<String, String> = mapOf(
            "LOCATION" to "image_permission_location",
        )

        fun showToast(context: Context, message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showDialog(context: Context, title: String, message: String){
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar") { dialog, _ ->
                    // Acción al hacer clic en Aceptar
                    dialog.dismiss()
                }
                .show()
        }

        fun showDialogYesOrNo(context: Context, title: String, message: String, listener: DialogClickListener) {
            val builder = AlertDialog.Builder(context)

            builder.setTitle(title)
            builder.setMessage(message)

            // Configuración del botón "Sí"
            builder.setPositiveButton("Sí") { _, _ ->
                listener.onPositiveButtonClick()
            }

            // Configuración del botón "No"
            builder.setNegativeButton("No") { _, _ ->
                listener.onNegativeButtonClick()
            }

            val dialog = builder.create()
            dialog.show()
        }

        fun showSettingsApp(context: Context){
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }

    }
}
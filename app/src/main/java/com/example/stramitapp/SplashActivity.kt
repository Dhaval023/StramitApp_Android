package com.example.stramitapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    private val requiredPermissions: Array<String>
        get() {
            val permissions = mutableListOf<String>()

            // Storage Permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            // Bluetooth Permissions (Android 12+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions.add(Manifest.permission.BLUETOOTH_SCAN)
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

            return permissions.toTypedArray()
        }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        if (hasRequiredPermissions()) {
            proceedToMain()
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show()
            proceedToMain()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (hasRequiredPermissions()) {
            Handler(Looper.getMainLooper()).postDelayed({
                proceedToMain()
            }, 2000)
        } else {
            requestPermissionLauncher.launch(requiredPermissions)
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        requiredPermissions.forEach { permission ->
            if (permission == Manifest.permission.READ_EXTERNAL_STORAGE || 
                permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                // On Android 13+, these will return DENIED. We check them only for older versions.
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            } else if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                // Location is satisfied if EITHER Coarse or Fine is granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            } else if (permission != Manifest.permission.ACCESS_COARSE_LOCATION) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    private fun proceedToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

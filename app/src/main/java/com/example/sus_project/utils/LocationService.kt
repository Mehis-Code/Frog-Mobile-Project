package com.example.sus_project.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


//Handy tools written by Raheem Jr. from ProAndroidDev.com for getting the user location

//data class to store the user Latitude and longitude
data class LatandLong(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var ready: Boolean = false,
)

//A callback for receiving notifications from the FusedLocationProviderClient.
lateinit var locationCallback: LocationCallback
//The main entry point for interacting with the Fused Location Provider
lateinit var locationProvider: FusedLocationProviderClient

//Function to get user latitude and longitude
@SuppressLint("MissingPermission")
@Composable
fun getUserLocation(context: Context, listen: Boolean): LatandLong {
    val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    var currentUserLocation by remember { mutableStateOf(LatandLong()) }
    DisposableEffect(key1 = listen) {
        if (listen == true) {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        currentUserLocation = LatandLong(location.latitude, location.longitude, true)
                        Log.d("LocationCallback", "Lat: ${location.latitude}, Lng: ${location.longitude}")
                    }
                }
            }
            val locationRequest = LocationRequest.create().apply {
                interval = 5000L
                fastestInterval = 500L
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = 5f
            }
            locationProvider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            onDispose {
                locationProvider.removeLocationUpdates(locationCallback)
            }
        } else {
            // If not listening, do nothing.
            onDispose {}
        }
    }
    return currentUserLocation
}

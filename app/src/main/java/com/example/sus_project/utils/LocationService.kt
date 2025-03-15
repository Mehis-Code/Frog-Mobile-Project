package com.example.sus_project.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val ready: Boolean = false,
)

//A callback for receiving notifications from the FusedLocationProviderClient.
lateinit var locationCallback: LocationCallback
//The main entry point for interacting with the Fused Location Provider
lateinit var locationProvider: FusedLocationProviderClient

//Function to get user latitude and longitude
@SuppressLint("MissingPermission")
@Composable
fun getUserLocation(context: Context): LatandLong {
    val locationProvider = LocationServices.getFusedLocationProviderClient(context)
    var currentUserLocation by remember { mutableStateOf(LatandLong()) }
    DisposableEffect(key1 = locationProvider) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    currentUserLocation = LatandLong(location.latitude, location.longitude, true)
                    Log.d("LocationCallback", "Lat: ${location.latitude}, Lng: ${location.longitude}")
                }
            }
        }
        val locationRequest = LocationRequest.create().apply {
            interval = 50000L // Request location updates every 2 seconds
            fastestInterval = 50000L // The fastest rate at which updates can occur (also 2 seconds)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Use high accuracy for location
        }
        locationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        onDispose {
            locationProvider.removeLocationUpdates(locationCallback)
        }
    }
    return currentUserLocation
}
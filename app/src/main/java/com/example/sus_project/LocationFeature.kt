package com.example.sus_project

import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import com.example.sus_project.utils.*




@Composable
fun LocationFeature(isPermissionGranted: Boolean) {
    var city by remember { mutableStateOf("") }
    var location by remember { mutableStateOf(LatandLong()) }
    location = getUserLocation(LocalContext.current)
    if (!isPermissionGranted) {
        Text(text = "Permissions needed for app to function")
    } else {
        if (city == "") {
            Loading()
        }
        val geocoder = Geocoder(LocalContext.current)
        // Get address from latitude and longitude
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        // Check if addresses are available
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            city = address.subAdminArea ?: "City not found"
        }
    Text(text = "Your city is: $city")
        Text(text = "Your location is: ${location.latitude}, ${location.longitude}")


    }
}
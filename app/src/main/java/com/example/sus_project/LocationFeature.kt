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
import androidx.compose.runtime.MutableState
import com.example.sus_project.utils.*
import kotlin.Double


@Composable
fun LocationFeature(isPermissionGranted: Boolean, upperLocation: MutableState<LatandLong>) {
    if (!isPermissionGranted) {
        Text(text = "Permissions needed for app to function")
    } else {
        var city by remember { mutableStateOf("") }
        // Activate callback on listen statechange
        var listen by remember { mutableStateOf(true) }
        // Fetching location in lower level component LocationFeature(), and passing to upper level Main()
        var location = getUserLocation(LocalContext.current, listen)
        if (listen) {
            upperLocation.value = location
        }
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
        Text(text = "Your location is: ${upperLocation.value.latitude}, ${upperLocation.value.longitude}")
        Text(text = "${if (listen) "Stop" else "Start"} listening")
    Button (onClick = {
        listen = !listen
        upperLocation.value = LatandLong()
    }) {

    }
    }
}
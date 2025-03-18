package com.example.sus_project

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sus_project.utils.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex

//Represents the core of the app -> All parts except the TopBar
@Composable
fun AppCore(isPermissionGranted: Boolean) {
    //Permission check. Message displayed if not granted
    if (!isPermissionGranted) {
        Text(text = "Permissions needed for app to function")
    } else {
        //Listen is the state to check whether to use a callback function to get user location
        var listen = remember { mutableStateOf(true) }
        var city = remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

        //Upperlocation is the state to hold the user location to display to the user
        var upperLocation = remember { mutableStateOf(LatandLong()) }
        //Location is a state from a callback function. It's callback is disabled when in input mode. Copied to Upperlocation when in use
        var location = getUserLocation(LocalContext.current, listen.value)
        upperLocation.value = location

        //Using Geocoder to get city name from coordinates
        //When in track mode (listen.value == true), get city name from user location
        //When in input mode (listen.value == false), get city coordinates from city name
        val geocoder = Geocoder(LocalContext.current)
        val addresses: List<android.location.Address>? = if (listen.value) {
            geocoder.getFromLocation(upperLocation.value.latitude, upperLocation.value.longitude, 1)
        } else {
            geocoder.getFromLocationName(city.value, 1)
        }

        //Handling of geocoder results. Ensuring that data has values
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            if (listen.value) {
                city.value = address.subAdminArea ?: "City not found"
                upperLocation.value.latitude = address.latitude
                upperLocation.value.longitude = address.longitude
                errorMessage = ""
            } else {
                upperLocation.value.latitude = address.latitude
                upperLocation.value.longitude = address.longitude
                upperLocation.value.ready = true
                errorMessage = ""
            }
        } else {
            errorMessage = "City not found. Please try again."
            upperLocation.value.latitude = 0.0
            upperLocation.value.longitude = 0.0
            upperLocation.value.ready = false
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxHeight()
            ) {
                //Display loading element if core app is not loaded yet
                if (city.value == "") {
                    Loading()
                } else {
                    Column(Modifier
                        .fillMaxHeight()
                        .padding(bottom = 80.dp)) {
                        //These two textfields display the location user is in/has selected and the relevant city/location
                        Text(text = "Your city is: ${city.value}")
                        Text(text = "Location is: ${"%.4f".format(upperLocation.value.latitude)}, ${
                                "%.4f".format(
                                    upperLocation.value.longitude
                                )
                            }"
                        )
                        //Show to user if a geolocation result could not be made with that location name
                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        //Section to house FrogRadar - the core functionality of the app
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                FrogRadar(
                                    upperLocation.value.latitude,
                                    upperLocation.value.longitude
                                )
                            }
                    }
                }
            }
            //Section to house the mode button and input field when in input mode
            Box(Modifier
                .align(BottomCenter)
                .clip(RoundedCornerShape(2.dp))) {
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(MaterialTheme.colorScheme.secondary).zIndex(10f))
                ButtonAndText(upperLocation, listen, city)
            }
        }
    }
}


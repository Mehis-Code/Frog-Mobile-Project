package com.example.sus_project

import android.graphics.Paint
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sus_project.utils.*

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.imePadding

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import kotlin.coroutines.ContinuationInterceptor.Key



@Composable
fun LocationFeature(isPermissionGranted: Boolean) {
    if (!isPermissionGranted) {
        Text(text = "Permissions needed for app to function")
    } else {
        var upperLocation = remember { mutableStateOf(LatandLong()) }
        var city = remember { mutableStateOf("") }  // Use MutableState here
        var listen = remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf("") }  // To hold error message
        var location = getUserLocation(LocalContext.current, listen.value)

        upperLocation.value = location

        val geocoder = Geocoder(LocalContext.current)
        val addresses: List<android.location.Address>? = if (listen.value) {
            geocoder.getFromLocation(upperLocation.value.latitude, upperLocation.value.longitude, 1)
        } else {
            geocoder.getFromLocationName(city.value, 1)
        }

        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            if (listen.value) {
                city.value = address.subAdminArea ?: "City not found"
                upperLocation.value.latitude = address.latitude
                upperLocation.value.longitude = address.longitude
                errorMessage = ""  // Clear error message if a valid address is found
            } else {
                upperLocation.value.latitude = address.latitude
                upperLocation.value.longitude = address.longitude
                upperLocation.value.ready = true
                errorMessage = ""  // Clear error message if a valid address is found
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
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
        ) {
            // Upper content (Frog Radar)
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxHeight()
            ) {
                if (city.value == "") {
                    Loading()
                } else {
                    Column(Modifier
                        .fillMaxHeight()
                        .padding(bottom = 80.dp)) {
                        Text(text = "Your city is: ${city.value}")
                        Text(
                            text = "Location is: ${"%.4f".format(upperLocation.value.latitude)}, ${
                                "%.4f".format(
                                    upperLocation.value.longitude
                                )
                            }"
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        // FrogRadar Section
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(2.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Frog sightings!")
                                FrogRadar(
                                    upperLocation.value.latitude,
                                    upperLocation.value.longitude
                                )
                            }
                        }
                    }
                }
            }

            Box(Modifier
                .align(BottomCenter)
                .clip(RoundedCornerShape(2.dp))) {
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(MaterialTheme.colorScheme.secondary).zIndex(10f))
                ButtonAndText(upperLocation, listen, city)
            }
        }
    }
}


@Composable
fun ButtonAndText(location: MutableState<LatandLong>, listen: MutableState<Boolean>, city: MutableState<String>) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var cityText by remember { mutableStateOf(city.value) }
    var selected by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
            .imePadding()
,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!listen.value) {

            Text(
                text = "*Only Finnish Frog data available",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 10.sp
            )

            // TextField with proper focus handling
            TextField(
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = Color.White,
                ),
                value = cityText,
                onValueChange = {
                    cityText = it
                },
                label = { Text("City Name") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // Set "Previous" action
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        selected = false
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        if (cityText.isNotEmpty()) {
                            city.value = cityText
                        }
                        cityText = "" // Clear text field
                    }
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        selected = focusState.isFocused
                    }

            )
        }

        // Show the button only when TextField is not focused
        if (!selected) {

            Button(
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.CenterHorizontally)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Background color of the button
                ),
                onClick = {
                    listen.value = !listen.value
                    if (!listen.value) {
                        location.value = LatandLong() // Reset location when switching to input mode
                    }
                }

            ) {
                Text(
                    text = if (listen.value) "Input mode" else "Track mode",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

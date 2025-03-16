package com.example.sus_project

import android.location.Geocoder
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
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.sp


@Composable
fun LocationFeature(isPermissionGranted: Boolean, upperLocation: MutableState<LatandLong>) {
    if (!isPermissionGranted) {
        Text(text = "Permissions needed for app to function")
    } else {
        var city = remember { mutableStateOf("") }  // Use MutableState here
        // Activate callback on listen statechange
        var listen = remember { mutableStateOf(true) }
        // Fetching location in lower level component LocationFeature(), and passing to upper level Main()
        var errorMessage by remember { mutableStateOf("") }  // To hold error message
        var location = getUserLocation(LocalContext.current, listen.value)
        upperLocation.value = location

        val geocoder = Geocoder(LocalContext.current)
        // Get address from latitude and longitude
        val addresses: List<android.location.Address>? = if (listen.value) {
            // Get the address from current location's latitude and longitude
            geocoder.getFromLocation(upperLocation.value.latitude, upperLocation.value.longitude, 1)
        } else {
            // Get the address for a hardcoded location ("Stockholm" in this case)
            geocoder.getFromLocationName(city.value, 1)
        }
        // Check if addresses are available

        // Check if addresses are available
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
                errorMessage = ""  // Clear error message if a valid address is found
            }
        } else {
            // Set error message if no address found
            errorMessage = "City not found. Please try again."
            // Reset coordinates to 0.0 if city search fails
            upperLocation.value.latitude = 0.0
            upperLocation.value.longitude = 0.0
            upperLocation.value.ready = false
        }

        Box(Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(8.dp)
        ) {
            if (city.value == "") {
                Loading()
            } else {
                Column(modifier = Modifier.align(Alignment.TopCenter)) {
                    Text(text = "Your city is: ${city.value}")
                    Text(text = "Location is: ${upperLocation.value.latitude}, ${upperLocation.value.longitude}")

                    // Display error message if there is one
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                // Pass the MutableState city to ButtonAndText
                ButtonAndText(upperLocation, listen, city)
            }
            //Column end
        }
    }
}
@Composable
fun ButtonAndText(location: MutableState<LatandLong>, listen: MutableState<Boolean>, city: MutableState<String>) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.ime),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var cityText by remember { mutableStateOf(city.value) }
            var selected by remember { mutableStateOf(false) }

            // Detect focus state
            val focusRequester = remember { FocusRequester() }

            if (!listen.value) {
                Text(text = "*Only Finnish Frog data available",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp)

                // TextField with proper focus handling
                TextField(
                    value = cityText,
                    onValueChange = {
                        cityText = it
                        selected = true
                    },
                    label = { Text("City Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()

                            if (cityText.isNotEmpty()) {
                                city.value = cityText // Update the city if not empty
                            }
                            cityText = "" // Clear the text field
                        }
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            selected = focusState.isFocused
                        }
                        .clickable {
                            focusRequester.requestFocus()
                            keyboardController?.show() // Show keyboard when clicked
                        }
                )

            }

            // Show the button only when TextField is not focused
            if (!selected) {
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        listen.value = !listen.value
                        if (!listen.value) {
                            location.value = LatandLong() // Reset location when switching to input mode
                        }
                    }
                ) {
                    Text(
                        text = if (listen.value) "Input mode" else "Track mode",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}



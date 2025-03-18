package com.example.sus_project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sus_project.utils.LatandLong


//Button and Textfield components, and their logic
@Composable
fun ButtonAndText(location: MutableState<LatandLong>, listen: MutableState<Boolean>, city: MutableState<String>) {
    //Management helpers for keyboard focus.
    //Otherwise Textfield may get pushed outside of view when focused
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
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Display Textfield only when in input mode
        if (!listen.value) {
            Text(
                text = "*Only Finnish Frog data available",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 10.sp
            )
            TextField(
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White
                ),
                value = cityText,
                onValueChange = {
                    cityText = it
                },
                label = { Text("City Name") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        selected = false
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        if (cityText.isNotEmpty()) {
                            city.value = cityText
                        }
                        cityText = ""
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
        // Show the button only when TextField is not focused (user writing)
        if (!selected) {
            Button(
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.CenterHorizontally)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                onClick = {
                    listen.value = !listen.value
                    if (!listen.value) {
                        //Reset location if swapped back to track mode. This refreshes the component
                        location.value = LatandLong()
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

package com.example.sus_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.* // Optimized import
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
import com.example.sus_project.ui.theme.Sus_projectTheme
import androidx.compose.runtime.*
import com.example.sus_project.utils.*
import androidx.compose.material3.Text

//Initialization of app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        //Setup of transparent navigation bar
        transparentNavBar()
        super.onCreate(savedInstanceState)
        setContent {
            //Theme applied to the app
            Sus_projectTheme {
                Main()
            }
        }
    }
}

//The app components together
@Composable
fun Main() {
    var isPermissionGranted by remember { mutableStateOf(false) }
    val upperLocation = remember { mutableStateOf(LatandLong()) } // Keep state in parent

    RequestLocationPermissionUsingRememberLauncherForActivityResult(
        onPermissionGranted = { isPermissionGranted = true },
        onPermissionDenied = { isPermissionGranted = false }
    )

    Surface(Modifier.fillMaxSize()) {
        Column {
            TopBar()
            // Pass existing location state, NOT a new state
            // Button to reload the location

            LocationFeature(isPermissionGranted, upperLocation)
            Text(text = "Location: ${upperLocation.value.latitude}, ${upperLocation.value.longitude}")
        }
    }
}



@Preview
@Composable
fun MainPreview() {
    Main()
}

package com.example.sus_project

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.example.sus_project.utils.getUserLocation

@Composable
fun LocationFeature(isPermissionGranted: Boolean) {
    if (isPermissionGranted) {
        val location = getUserLocation(LocalContext.current)
        Text(text = "Location: ${location.latitude}, ${location.longitude}")
    } else {
        Text(text = "Permissions needed for app to function")
    }
}




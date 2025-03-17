package com.example.sus_project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex


@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize() // Ensures it takes up the whole screen
            .background(Color.Black.copy(alpha = 0.5f)) // Optional: Adds a translucent background
            .padding(16.dp), // Optional: space around the spinner
        contentAlignment = Alignment.Center // Centers the loading spinner and text
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Ensures everything is vertically centered
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(100.dp)
                    .zIndex(99f), // Ensures the spinner stays on top
                strokeWidth = 8.dp, // Adjust the thickness of the spinner's stroke
                color = MaterialTheme.colorScheme.tertiary, // Optional: Adjust the spinner color
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add some spacing between spinner and text
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge, // Optional: Adjust the text style
                color = MaterialTheme.colorScheme.tertiary // Optional: Adjust the text color
            )
        }
    }
}

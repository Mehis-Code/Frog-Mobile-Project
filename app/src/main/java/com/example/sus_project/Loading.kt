package com.example.sus_project

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center, // Corrected
        horizontalAlignment = Alignment.CenterHorizontally // Corrected
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(

                modifier = Modifier.width(200.dp)
                    .offset(y = (88).dp),
                strokeWidth = 8.dp,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = "Loading...",
                modifier = Modifier.offset(y = (164).dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

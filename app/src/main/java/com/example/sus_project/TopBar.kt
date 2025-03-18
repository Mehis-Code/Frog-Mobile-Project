package com.example.sus_project


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

//Simple topbar with logo and name
@Composable
fun TopBar() {
        TopBarSpacer()
        Surface(
            modifier = Modifier
                .height(60.dp)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            //The icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.frogradar),
                    contentDescription = "App logo",
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .padding(end = 5.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.radarname),
                    contentDescription = "Name of app",
                    modifier = Modifier
                        .padding( end = 5.dp, top = 5.dp)
                )
            }
        }
}

//Spacer for possible camera
@Composable
fun TopBarSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(MaterialTheme.colorScheme.primary),
    )
}


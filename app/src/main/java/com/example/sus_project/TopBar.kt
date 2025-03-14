package com.example.sus_project

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource


@Composable
fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary),

        ) {
        SpacerThing()
        Surface(
            modifier = Modifier
                .height(60.dp),

        ) {
            //The icons
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.sus),
                    contentDescription = "My Drawable",
                    modifier = Modifier.padding(start = 5.dp)
                )
                MyButton(text = "Home", onClick = { /*TODO*/ })
                MyButton(text = "Profile", onClick = { /*TODO*/ })
                MyButton(text = "Settings", onClick = { /*TODO*/ })
            }
        }
    }
}


@Composable
fun MyButton(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClick() },
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar()
}

@Composable
fun SpacerThing() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
    )
}
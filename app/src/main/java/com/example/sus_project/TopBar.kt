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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource


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
                    contentDescription = "My Drawable",
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .padding(end = 5.dp)
                )
                MyButton(text = "Pal", onClick = { /*TODO*/ })
                MyButton(text = "Smal", onClick = { /*TODO*/ })
                MyButton(text = "P I E M O D E", onClick = { /*TODO*/ })
            }
        }
}


@Composable
fun MyButton(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxHeight()
            .clip(RoundedCornerShape(5.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)


)   }
}

@Composable
fun TopBarSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(MaterialTheme.colorScheme.primary),
    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar()
}


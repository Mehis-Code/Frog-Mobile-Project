package com.example.sus_project.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sus_project.R


//A single Frog item rendered to the Lazylist in FrogRadar
@Composable
fun FrogComposable(info: GatheringInfo) {
    //Adapt image resource based on frog name
    var frogDrawable = R.drawable.bufobufo
    if (info.FrogName == "Rana arvalis") {
        frogDrawable = R.drawable.ranaarvalis
    }
    if (info.FrogName == "Rana temporaria") {
        frogDrawable = R.drawable.ranatemporaria
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = frogDrawable),
            contentDescription = "Frog Image",
            modifier = Modifier
                .height(30.dp)
                .width(70.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(
                text = "Frog Name: ${info.FrogName}",
            )
            Text(
                text = "Date: ${info.Date}",
            )
        }
    }
}


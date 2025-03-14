package com.example.sus_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.* // Optimized import

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sus_project.ui.theme.Sus_projectTheme
import androidx.compose.runtime.*
import androidx.compose.animation.*
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sus_projectTheme {
                Main()
            }


        }
    }
}

@Composable
fun Main() {
    Surface(Modifier.fillMaxSize()){
        Column {
            TopBar()
            Text("Hello world")
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    Main()
}

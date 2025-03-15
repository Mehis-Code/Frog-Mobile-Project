package com.example.sus_project.utils

import android.view.WindowManager
import androidx.activity.ComponentActivity

//Sets up transparent navigation bar, which improves the aesthetic
fun ComponentActivity.transparentNavBar() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}
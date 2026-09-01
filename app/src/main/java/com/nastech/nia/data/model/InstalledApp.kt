package com.nastech.nia.data.model

data class InstalledApp(
    val packageName: String,
    val appName: String,
    val icon: android.graphics.drawable.Drawable?
)
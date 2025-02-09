package com.example.perfectnumar.ui.theme

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import android.content.Context
import android.view.Window
import androidx.core.view.WindowCompat

object ThemeManager {
    fun applyTheme(activity: Activity, isDarkTheme: Boolean = false) {
        // Tema modunu ayarla
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Status bar rengini ayarla
        activity.window?.apply {
            statusBarColor = if (isDarkTheme) {
                AppColors.Purple80
            } else {
                AppColors.Purple40
            }

            // Status bar ikonlarının rengini ayarla
            WindowCompat.getInsetsController(this, activity.window.decorView)
                .isAppearanceLightStatusBars = !isDarkTheme
        }
    }
}
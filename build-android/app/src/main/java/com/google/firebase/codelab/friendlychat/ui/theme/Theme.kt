package com.example.campus.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    secondary = Color(142,142,147),//light grey
    tertiary = Color(220,240,111),//neon yellow
    onTertiary = Color(196,196,196),//grey msg bubel
    background = Color(0,0,0),
    onBackground = Color(28,29,30),//card
    outline = Color(41,41,41),//super light grey nästan vit devider line color

)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = Color(142,142,147),//light grey
    tertiary = Color(54,123,247),//blue
    onTertiary = Color(235,233,233),//grey msg bubel
    background = Color(242,242,247),// off withe bg
    onBackground = Color.White,//card
    outline = Color(244,243,243),//super light grey nästan vit devider line color

)

@Composable
fun CampusTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),//todo sät på
    darkTheme: Boolean,
    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
            Log.d("MarcusTAG", "dark them : "+darkTheme)
    val view = LocalView.current
    if (!view.isInEditMode) {

        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()


            val isBackgroundLight = colorScheme.background.luminance() > 0.5

            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isBackgroundLight

            window.navigationBarColor = colorScheme.background.toArgb()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = if (!darkTheme) {
                    window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                } else {
                    window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
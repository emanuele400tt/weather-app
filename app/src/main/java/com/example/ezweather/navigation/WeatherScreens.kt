package com.example.ezweather.navigation

import kotlinx.serialization.Serializable

sealed class WeatherScreens {
    @Serializable data object SplashScreen
    @Serializable data object MainScreen
    @Serializable data object SearchScreen
    @Serializable data object AboutScreen
    @Serializable data object SettingsScreen
    @Serializable data object FavoriteScreen

}
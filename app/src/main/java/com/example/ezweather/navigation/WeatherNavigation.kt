package com.example.ezweather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ezweather.screens.AboutScreen
import com.example.ezweather.screens.FavoriteScreen
import com.example.ezweather.screens.MainScreen
import com.example.ezweather.screens.SearchScreen
import com.example.ezweather.screens.SettingsScreen
import com.example.ezweather.screens.SplashScreen

@Composable
fun WeatherNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = WeatherScreens.SplashScreen
        ) {

        composable<WeatherScreens.SplashScreen> {
            SplashScreen(navController = navController)
        }

        composable<WeatherScreens.MainScreen> {
             MainScreen(navController = navController)
        }

        composable<WeatherScreens.SearchScreen> {
             SearchScreen(navController = navController)
        }

        composable<WeatherScreens.AboutScreen> {
             AboutScreen(navController = navController)
        }

        composable<WeatherScreens.SettingsScreen> {
             SettingsScreen(navController = navController)
        }

        composable<WeatherScreens.FavoriteScreen> {
             FavoriteScreen(navController = navController)
        }
    }
}
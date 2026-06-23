package com.example.ezweather.network

import com.example.ezweather.model.GeocodingResponse
import com.example.ezweather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCity(
        @Query("name") query: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "it"
    ): GeocodingResponse

    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}
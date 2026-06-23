package com.example.ezweather.model

import com.google.gson.annotations.SerializedName

// --- MODELLI PER LA RICERCA DELLA CITTA' (Geocoding) ---
data class GeocodingResponse(
    @SerializedName("results") val results: List<CityResult>?
)

data class CityResult(
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("country") val country: String
)

// --- MODELLI PER IL METEO (Forecast) ---
data class WeatherResponse(
    @SerializedName("current") val current: CurrentWeather,
    @SerializedName("daily") val daily: DailyWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m") val temperature2m: Double,
    @SerializedName("weather_code") val weatherCode: Int
)

data class DailyWeather(
    @SerializedName("time") val time: List<String>,
    @SerializedName("weather_code") val weatherCode: List<Int>,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double>,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double>
)
package com.example.ezweather.repository

import com.example.ezweather.model.CityResult
import com.example.ezweather.model.WeatherResponse
import com.example.ezweather.network.Resource
import com.example.ezweather.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
    suspend fun getWeather(cityQuery: String): Resource<WeatherResponse> {
        return try {
            // 1. Chiama l'API di Geocoding per trovare le coordinate della città
            val geoResponse = api.searchCity(query = cityQuery)

            // Prende il primo risultato (se esiste)
            val location = geoResponse.results?.firstOrNull()

            if (location == null) {
                return Resource.Error("Città non trovata")
            }

            // 2. Usa latitudine e longitudine per ottenere il meteo
            val weatherResponse = api.getWeather(
                lat = location.latitude,
                lon = location.longitude
            )

            // Restituisce i dati con successo!
            Resource.Success(weatherResponse)

        } catch (e: Exception) {
            // Gestisce errori di rete o crash
            Resource.Error(e.message ?: "Si è verificato un errore di rete")
        }
    }
    suspend fun getCitySuggestions(query: String): List<CityResult> {
        return try {
            val response = api.searchCity(query = query, count = 5)
            response.results ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}


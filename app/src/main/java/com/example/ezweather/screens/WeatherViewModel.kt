package com.example.ezweather.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezweather.model.CityResult
import com.example.ezweather.model.WeatherResponse
import com.example.ezweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.ezweather.network.Resource

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherData = MutableStateFlow<Resource<WeatherResponse>>(Resource.Loading())
    val weatherData = _weatherData.asStateFlow()

    private val _suggestions = MutableStateFlow<List<CityResult>>(emptyList())
    val suggestions = _suggestions.asStateFlow()

    private var searchJob: Job? = null

    init {
        getWeatherData("Roma")
    }

    fun getWeatherData(city: String) {
        viewModelScope.launch {
            _weatherData.value = Resource.Loading()
            _weatherData.value = repository.getWeather(city)
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()

        if (query.length < 2) {
            _suggestions.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)
            _suggestions.value = repository.getCitySuggestions(query)
        }
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}
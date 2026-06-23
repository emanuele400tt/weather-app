package com.example.ezweather.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ezweather.network.Resource
import com.example.ezweather.model.WeatherResponse
import com.example.ezweather.utils.formatDailyDate
import com.example.ezweather.utils.getIconResId
import com.example.ezweather.utils.getWeatherDescription
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherState by viewModel.weatherData.collectAsStateWithLifecycle()
    val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var displayedCity by remember { mutableStateOf("Roma") }
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EZ Weather") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.onSearchQueryChanged(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        placeholder = { Text("Cerca una città...", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color.DarkGray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (searchQuery.isNotBlank()) {
                                displayedCity = searchQuery.replaceFirstChar { it.uppercase() }
                                viewModel.clearSuggestions()
                                viewModel.getWeatherData(searchQuery)
                                focusManager.clearFocus()
                            }
                        }),
                        trailingIcon = {
                            IconButton(onClick = {
                                if (searchQuery.isNotBlank()) {
                                    displayedCity = searchQuery.replaceFirstChar { it.uppercase() }
                                    viewModel.clearSuggestions()
                                    viewModel.getWeatherData(searchQuery)
                                    focusManager.clearFocus()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Cerca",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                    AnimatedVisibility(visible = suggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                                items(suggestions.size) { index ->
                                    val city = suggestions[index]
                                    Text(
                                        text = "${city.name}, ${city.country}",
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                searchQuery = city.name
                                                displayedCity = city.name
                                                viewModel.clearSuggestions()
                                                viewModel.getWeatherData(city.name)
                                                focusManager.clearFocus()
                                            }
                                            .padding(16.dp)
                                    )
                                    HorizontalDivider(color = Color.DarkGray)
                                }
                            }
                        }
                    }
                }
            }
            when (val state = weatherState) {
                is Resource.Loading -> {
                    Spacer(modifier = Modifier.height(32.dp))
                    CircularProgressIndicator(color = Color(0xFF38BDF8))
                }
                is Resource.Success -> {
                    state.data?.let { weather ->
                        WeatherMainContent(weather, cityName = displayedCity)
                    }
                }
                is Resource.Error -> {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Impossibile trovare '${displayedCity}'.",
                        color = Color.Red,
                        fontSize = 18.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherMainContent(weather: WeatherResponse, cityName: String) {
    val currentCode = weather.current.weatherCode
    val currentTemp = weather.current.temperature2m
    val iconRes = getIconResId(currentCode)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = cityName,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        AsyncImage(
            model = iconRes,
            contentDescription = "Meteo Icon",
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = "${currentTemp.roundToInt()}°",
            fontSize = 80.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF38BDF8)
        )
        Text(
            text = getWeatherDescription(currentCode).uppercase(),
            fontSize = 18.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color.DarkGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Previsioni 7 giorni",
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(weather.daily.time.size) { index ->
                ForecastRow(
                    date = weather.daily.time[index],
                    maxTemp = weather.daily.temperature2mMax[index],
                    minTemp = weather.daily.temperature2mMin[index],
                    weatherCode = weather.daily.weatherCode[index]
                )
            }
        }
    }
}

@Composable
fun ForecastRow(date: String, maxTemp: Double, minTemp: Double, weatherCode: Int) {
    val iconRes = getIconResId(weatherCode)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = iconRes,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(
                    text = formatDailyDate(date),
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = getWeatherDescription(weatherCode),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(
                text = "${maxTemp.roundToInt()}° / ${minTemp.roundToInt()}°",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
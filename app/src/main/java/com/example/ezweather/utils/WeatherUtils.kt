package com.example.ezweather.utils

import com.example.ezweather.R


fun getIconResId(code: Int): Int {
    return when (code) {
        0 -> R.drawable.ic_sun
        1, 2 -> R.drawable.sun
        3 -> R.drawable.ic_cloud
        45, 48 -> R.drawable.ic_mist
        51, 53, 55 -> R.drawable.ic_rain
        56, 57 -> R.drawable.ic_rain_snow
        61, 63, 65 -> R.drawable.ic_rainy
        66, 67 -> R.drawable.ic_hail
        80, 81, 82 -> R.drawable.ic_heavy_rain
        71, 73, 75, 77, 85, 86 -> R.drawable.ic_snow
        95 -> R.drawable.ic_storm
        96, 99 -> R.drawable.ic_hail
        else -> R.drawable.sun
    }
}

// Converte i codici di Open-Meteo in una stringa leggibile
fun getWeatherDescription(code: Int): String {
    return when (code) {
        0 -> "Sereno"
        1 -> "Poco nuvoloso"
        2 -> "Parz. nuvoloso" // Abbreviato per non sballare troppo la UI
        3 -> "Coperto"
        45, 48 -> "Nebbia"
        51, 53, 55 -> "Pioviggine"
        56, 57 -> "Pioggia mista neve"
        61, 63, 65 -> "Pioggia"
        66, 67 -> "Pioggia gelata"
        71, 73, 75 -> "Neve"
        77 -> "Nevischio"
        80, 81, 82 -> "Acquazzone"
        85, 86 -> "Rovesci di neve"
        95 -> "Temporale"
        96, 99 -> "Tempesta con grandine"
        else -> "Sconosciuto"
    }
}

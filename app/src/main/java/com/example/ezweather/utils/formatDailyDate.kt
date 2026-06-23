package com.example.ezweather.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDailyDate(dateString: String): String {
    return try {
        val parsedDate = LocalDate.parse(dateString)
        val today = LocalDate.now()

        when (parsedDate) {
            today -> "Oggi"
            today.plusDays(1) -> "Domani"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("EEEE dd", Locale.ITALIAN)
                parsedDate.format(formatter).replaceFirstChar { it.uppercase() }
            }
        }
    } catch (e: Exception) {
        dateString
    }
}
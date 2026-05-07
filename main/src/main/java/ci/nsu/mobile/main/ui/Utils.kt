package ci.nsu.mobile.main.ui

import java.text.SimpleDateFormat
import java.util.*

// Одна функция форматирования даты для всего приложения
fun formatDate(timestamp: Long, withTime: Boolean = false): String {
    val pattern = if (withTime) "dd.MM.yyyy HH:mm" else "dd.MM.yyyy"
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(timestamp))
}

// Форматирование денег: 1000.5 -> "1 000,50 ₽"
fun formatMoney(value: Double): String {
    return String.format("%.2f ₽", value)
}
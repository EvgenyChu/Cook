package ru.churkin.cook.home

import java.text.SimpleDateFormat
import java.util.*


fun Date.format(pattern: String = "HH:mm:ss dd:MM:yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)}
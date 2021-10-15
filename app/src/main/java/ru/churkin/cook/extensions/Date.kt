package ru.churkin.cook.home

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND {
        override fun plurals(value: Int): String {
            var _timeS = "секунд"
            var n = 1
            for (i in 0..3) {
                n = i * 10 + 21
            }
            when (value) {
                1, n -> _timeS += "а"
                2, 3, 4, n + 1, n + 2, n + 3 -> _timeS += "ы"
                else -> _timeS
            }
            return "$value $_timeS"
        }
    },
    MINUTE {
        override fun plurals(value: Int): String {
            var _timeM = "минут"
            var n = 1
            for (i in 0..3) {
                n = i * 10 + 21
            }
            when (value) {
                1, n -> _timeM += "а"
                2, 3, 4, n + 1, n + 2, n + 3 -> _timeM += "ы"
                else -> _timeM
            }
            return "$value $_timeM"
        }
    },
    HOUR {
        override fun plurals(value: Int): String {
            var _timeH = "час"
            var n = 1
            for (i in 0..3) {
                n = i * 10 + 21
            }
            when (value) {
                1, n -> _timeH
                2, 3, 4, n + 1, n + 2, n + 3 -> _timeH += "а"
                else -> _timeH += "ов"
            }
            return "$value $_timeH"
        }
    },
    DAY {
        override fun plurals(value: Int): String {
            var _timeD = "д"
            for (value in 0..365) {
                when {
                    value==11 || value==111 || value==211 || value==311 -> _timeD +="ней"
                    value%10 == 1 -> _timeD +="ень"
                    value==2 || value==3|| value==4|| value%10 == 2 ||
                            value%10 == 3 || value%10 ==4 -> _timeD +="ня"
                    else -> _timeD +="ней"
                }
            }
            return "$value $_timeD"
        }
    };

    abstract fun plurals(value: Int): String
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.format(pattern: String = "dd:MM:yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)}
package com.santamaria.mycurrentweather

import java.util.*

/**
 * Created by Santamaria on 23/10/2017.
 */
class UtilityClass {

    companion object {

        fun getImage(iconName : String) : Int {

            var drawableToUse = 0

            when (iconName) {

                "clear-day" -> drawableToUse = R.drawable.clear_day
                "clear-night" -> drawableToUse = R.drawable.clear_night
                "cloudy" -> drawableToUse = R.drawable.cloudy
                "cloudy-night" -> drawableToUse = R.drawable.cloudy_night
                "fog" -> drawableToUse = R.drawable.fog
                "na" -> drawableToUse = R.drawable.na
                "partly_cloudy", "partly-cloudy-night", "partly-cloudy-day" -> drawableToUse = R.drawable.partly_cloudy
                "rain" -> drawableToUse = R.drawable.rain
                "sleet" -> drawableToUse = R.drawable.sleet
                "snow" -> drawableToUse = R.drawable.snow
                "sunny" -> drawableToUse = R.drawable.sunny
                "wind" -> drawableToUse = R.drawable.wind

            }

            return drawableToUse

        }

        fun getStrDayFromNumber( dayInt : Int ) : String {

            var day = ""
            when (dayInt) {

                Calendar.SUNDAY -> day = "Sunday"
                Calendar.MONDAY -> day = "Monday"
                Calendar.TUESDAY -> day = "Tuesday"
                Calendar.WEDNESDAY -> day = "Wednesday"
                Calendar.THURSDAY -> day = "Thursday"
                Calendar.FRIDAY -> day = "Friday"
                Calendar.SATURDAY -> day = "Saturday"

            }

            return day
        }

    }

}
package com.santamaria.mycurrentweather

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.util.*

/**
 * Created by Santamaria on 23/10/2017.
 */
class UtilityClass {

    companion object {

        private var mAdView: AdView? = null

        fun getImage(iconName : String) : Int {

            var drawableToUse = 0

            when (iconName) {

                "clear-day" -> drawableToUse = R.drawable.clear_day
                "clear-night" -> drawableToUse = R.drawable.clear_night
                "cloudy" -> drawableToUse = R.drawable.cloudy
                "cloudy-night" -> drawableToUse = R.drawable.cloudy_night
                "fog" -> drawableToUse = R.drawable.fog
                "na" -> drawableToUse = R.drawable.na
                "partly-cloudy-day" -> drawableToUse = R.drawable.partly_cloudy_day
                "partly_cloudy", "partly-cloudy-night" -> drawableToUse = R.drawable.partly_cloudy
                "rain" -> drawableToUse = R.drawable.rain
                "sleet" -> drawableToUse = R.drawable.sleet
                "snow" -> drawableToUse = R.drawable.snow
                "sunny" -> drawableToUse = R.drawable.sunny
                "wind" -> drawableToUse = R.drawable.wind

            }

            return drawableToUse

        }

        fun getStrDayFromNumber(context : Context, dayInt : Int ) : String {

            var day = ""
            when (dayInt) {

                Calendar.SUNDAY -> day = context.getString(R.string.sunday)
                Calendar.MONDAY -> day = context.getString(R.string.monday)
                Calendar.TUESDAY -> day = context.getString(R.string.tuesday)
                Calendar.WEDNESDAY -> day = context.getString(R.string.wednesday)
                Calendar.THURSDAY -> day = context.getString(R.string.thursday)
                Calendar.FRIDAY -> day = context.getString(R.string.friday)
                Calendar.SATURDAY -> day = context.getString(R.string.saturday)

            }

            return day
        }

        fun loadAdds(activity: Activity) {
            mAdView = activity.findViewById(R.id.adView) as AdView
            val adRequest = AdRequest.Builder().build()
            mAdView?.loadAd(adRequest)
        }

    }

}
package com.santamaria.mycurrentweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.santamaria.mycurrentweather.API.Forecast
import com.santamaria.mycurrentweather.Models.Basic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var forecastData : Basic? = null

    var icon : ImageView? = null
    var summary : TextView? = null
    var temperature : TextView? = null
    var high : TextView? = null
    var low : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getViews()

        getForecastInformation()


    }

    private fun getForecastInformation() {
        var forecastCall : Call<Basic> = Forecast.getForecast().getForecastApi(37.8267,-122.4233, "en", "si")

        forecastCall.enqueue(object : Callback<Basic>{
            override fun onResponse(call: Call<Basic>?, response: Response<Basic>?) {
                if (response != null && response.isSuccessful()){

                    forecastData = response.body()

                    loadMainData()
                }
            }

            override fun onFailure(call: Call<Basic>?, t: Throwable?) {
                Toast.makeText(applicationContext, "Communication Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getViews() {
        icon = findViewById(R.id.idImgCurrentWeather) as ImageView
        summary = findViewById(R.id.idCurrentWeather) as TextView
        temperature = findViewById(R.id.idCurrentNumber) as TextView
        high = findViewById(R.id.idHigh) as TextView
        low = findViewById(R.id.idLow) as TextView
    }

    private fun loadMainData() {

        summary?.text = forecastData?.currently?.summary
        temperature?.text = Math.round(forecastData?.currently?.temperature!!).toString()
        getImage(forecastData?.currently?.icon!!)

    }

    private fun getImage(iconName : String){

        var drawableToUse = 0

        when (iconName) {

            "clear-day" -> drawableToUse = R.drawable.clear_day
            "clear-night" -> drawableToUse = R.drawable.clear_night
            "cloudy" -> drawableToUse = R.drawable.cloudy
            "cloudy-night" -> drawableToUse = R.drawable.cloudy_night
            "fog" -> drawableToUse = R.drawable.fog
            "na" -> drawableToUse = R.drawable.na
            "partly_cloudy" -> drawableToUse = R.drawable.partly_cloudy
            "rain" -> drawableToUse = R.drawable.rain
            "sleet" -> drawableToUse = R.drawable.sleet
            "snow" -> drawableToUse = R.drawable.snow
            "sunny" -> drawableToUse = R.drawable.sunny
            "wind" -> drawableToUse = R.drawable.wind

        }

        icon?.setImageResource(drawableToUse)

    }
}

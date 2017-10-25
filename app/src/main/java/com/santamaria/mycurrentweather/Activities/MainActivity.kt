package com.santamaria.mycurrentweather.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.santamaria.mycurrentweather.API.Forecast
import com.santamaria.mycurrentweather.Models.Basic
import com.santamaria.mycurrentweather.R
import com.santamaria.mycurrentweather.UtilityClass
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
        var forecastCall : Call<Basic> = Forecast.getForecast().getForecastApi(10.0687544,-84.3265938, "en", "si")

        forecastCall.enqueue(object : Callback<Basic>{
            override fun onResponse(call: Call<Basic>?, response: Response<Basic>?) {
                if (response != null && response.isSuccessful()){

                    forecastData = response.body()

                    loadMainActivity()
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
        high = findViewById(R.id.idTempH) as TextView
        low = findViewById(R.id.idTempL) as TextView
    }

    private fun loadMainActivity() {

        summary?.text = forecastData?.currently?.summary
        temperature?.text = Math.round(forecastData?.currently?.temperature!!).toString()
        icon?.setImageResource(UtilityClass.getImage(forecastData?.currently?.icon!!))
        if (forecastData?.daily?.data != null && forecastData?.daily?.data?.size!! > 0) {
            high?.text = Math.round(forecastData?.daily?.data?.get(0)!!.temperatureHigh).toString()
            low?.text = Math.round(forecastData?.daily?.data?.get(0)!!.temperatureLow).toString()
        }

    }

    fun onDailyClick(view : View) {

        var intent = Intent(this, DailyActivity::class.java)
        var bundle = Bundle()
        bundle.putParcelable("daily", forecastData?.daily)
        intent.putExtras(bundle)
        startActivity(intent)

    }

    fun onHourlyClick(view : View) {

        var intent = Intent(this, HourlyActivity::class.java)
        intent.putExtra("hourly", forecastData?.hourly)
        startActivity(intent)

    }

}


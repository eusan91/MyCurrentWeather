package com.santamaria.mycurrentweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var forecastCall : Call<Basic> = Forecast.getForecast().getForecastApi(37.8267,-122.4233)

        forecastCall.enqueue(object : Callback<Basic>{
            override fun onResponse(call: Call<Basic>?, response: Response<Basic>?) {
                Log.d("ema", "1")
            }

            override fun onFailure(call: Call<Basic>?, t: Throwable?) {
                Log.d("ema", "0")
            }

        })
    }
}

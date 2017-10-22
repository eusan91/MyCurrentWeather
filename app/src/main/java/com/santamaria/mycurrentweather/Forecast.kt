package com.santamaria.mycurrentweather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Santamaria on 22/10/2017.
 */
class Forecast {

    companion object {

        fun getForecast() : ForecastService {

            return Retrofit.Builder()
                    .baseUrl("https://api.darksky.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ForecastService::class.java)

        }

    }

}

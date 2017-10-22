package com.santamaria.mycurrentweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Santamaria on 21/10/2017.
 */
interface ForecastService {

    @GET("forecast/bf3f58abe2562f28a37efca9f03c828b/{latitudeCoord},{longitudeCoord}")
    fun getForecastApi(@Path("latitudeCoord") latitude : Double, @Path("longitudeCoord") longitude : Double ) : Call<Basic>
}
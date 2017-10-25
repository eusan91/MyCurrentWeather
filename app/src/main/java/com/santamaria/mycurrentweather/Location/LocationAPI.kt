package com.santamaria.mycurrentweather.Location

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

/**
 * Created by Santamaria on 25/10/2017.
 */
class LocationAPI : LocationListener{

    var location : Location? = null

    constructor(){
        location = Location("Start")
        location?.longitude = 0.0
        location?.latitude = 0.0
    }
    override fun onLocationChanged(p0: Location?) {
        location=p0

        Log.d("ema", location?.longitude.toString())
        Log.d("ema", location?.latitude.toString())
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(p0: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(p0: String?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
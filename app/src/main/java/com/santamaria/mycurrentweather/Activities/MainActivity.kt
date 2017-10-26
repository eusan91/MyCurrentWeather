package com.santamaria.mycurrentweather.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.santamaria.mycurrentweather.Location.LocationAPI
import com.santamaria.mycurrentweather.Retrofit.Forecast
import com.santamaria.mycurrentweather.Models.Basic
import com.santamaria.mycurrentweather.R
import com.santamaria.mycurrentweather.UtilityClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MainActivity : AppCompatActivity(){

    var forecastData : Basic? = null

    var location : TextView? = null
    var icon : ImageView? = null
    var summary : TextView? = null
    var temperature : TextView? = null
    var high : TextView? = null
    var low : TextView? = null

    var myLocation : LocationAPI? = null
    var locationManager : LocationManager? = null
    val GPS_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getViews()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), GPS_PERMISSION)

            }

        } else {
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == GPS_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    fun getForecastInformation(latitude : Double, longitude : Double) {
        var forecastCall : Call<Basic> = Forecast.getForecast().getForecastApi(latitude,longitude, "en", "si")

        forecastCall.enqueue(object : Callback<Basic>{
            override fun onResponse(call: Call<Basic>?, response: Response<Basic>?) {
                if (response != null && response.isSuccessful){

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
        location = findViewById(R.id.idLocation) as TextView
    }

    private fun loadMainActivity() {

        summary?.text = forecastData?.currently?.summary
        temperature?.text = Math.round(forecastData?.currently?.temperature!!).toString()
        icon?.setImageResource(UtilityClass.getImage(forecastData?.currently?.icon!!))
        if (forecastData?.daily?.data != null && forecastData?.daily?.data?.size!! > 0) {
            high?.text = Math.round(forecastData?.daily?.data?.get(0)!!.temperatureHigh).toString()
            low?.text = Math.round(forecastData?.daily?.data?.get(0)!!.temperatureLow).toString()
        }
        location?.text = forecastData?.timezone

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

    fun onRefreshData(view : View) {

        //getCurrentLocation()
        //getForecastInformation()

    }

    private fun getCurrentLocation(){

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocation = LocationAPI(this)

        val gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled!!) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
        }

        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, myLocation)
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, myLocation)

    }

}


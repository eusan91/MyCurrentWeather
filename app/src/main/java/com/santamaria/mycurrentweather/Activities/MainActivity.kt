package com.santamaria.mycurrentweather.Activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
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
import java.util.*


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
                return
            }

        }

        myLocation = LocationAPI(this)
        getCurrentLocation()
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

        var lang = Locale.getDefault().language

        if (lang.compareTo("es", true) != 0 ){
            lang = "en"
        }

        var forecastCall : Call<Basic> = Forecast.getForecast().getForecastApi(latitude,longitude, lang, "auto")

        forecastCall.enqueue(object : Callback<Basic>{
            override fun onResponse(call: Call<Basic>?, response: Response<Basic>?) {
                if (response != null && response.isSuccessful){

                    forecastData = response.body()

                    loadMainActivity(latitude, longitude)
                }
            }

            override fun onFailure(call: Call<Basic>?, t: Throwable?) {
                Toast.makeText(applicationContext, getString(R.string.communication_error), Toast.LENGTH_SHORT).show()
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

    private fun loadMainActivity(latitude : Double, longitude : Double) {

        summary?.text = forecastData?.currently?.summary
        temperature?.text = Math.round(forecastData?.currently?.temperature!!).toString()
        icon?.setImageResource(UtilityClass.getImage(forecastData?.currently?.icon!!))
        if (forecastData?.daily?.data != null && forecastData?.daily?.data?.size!! > 0) {
            high?.text = Math.round(forecastData?.daily?.data?.get(0)!!.temperatureHigh).toString()
            low?.text = Math.round(forecastData?.daily?.data?.get(0)!!.temperatureLow).toString()
        }

        val geocoder = Geocoder(this, Locale.getDefault())
        val list = geocoder.getFromLocation(
                latitude, longitude, 1)

        if (!list.isEmpty()) {
            val DirCalle = list[0]
            location?.text = DirCalle.subAdminArea
        } else {
            location?.text = forecastData?.timezone
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

    fun onRefreshData(view : View) {

        //getCurrentLocation()
        //getForecastInformation()

    }

    private fun getCurrentLocation(){

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!gpsEnabled!!) {
            ActivateGPSDialog()
        } else {
            myLocation = LocationAPI(this)

            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1800000, 0f, myLocation)
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 0f, myLocation)
        }

    }

    private fun ActivateGPSDialog(){

        var alert = AlertDialog.Builder(this).create()
        alert.setTitle(getString(R.string.alert_title))
        alert.setMessage(getString(R.string.alert_message))
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", DialogInterface.OnClickListener { dialogInterface, i ->

            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)

        })

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_negative), DialogInterface.OnClickListener { dialogInterface, i ->  })

        alert.show()
    }

    override fun onResume() {
        super.onResume()

        var gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (gpsEnabled!!) {


            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1800000, 0f, myLocation)
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 0f, myLocation)
        }
    }

    override fun onStop() {
        super.onStop()
        locationManager?.removeUpdates(myLocation)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}


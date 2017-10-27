package com.santamaria.mycurrentweather.Activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.santamaria.mycurrentweather.App.MyApplication
import com.santamaria.mycurrentweather.Location.LocationAPI
import com.santamaria.mycurrentweather.Retrofit.Forecast
import com.santamaria.mycurrentweather.Models.Basic
import com.santamaria.mycurrentweather.R
import com.santamaria.mycurrentweather.UtilityClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var forecastData : Basic? = null

    var location : TextView? = null
    var icon : ImageView? = null
    var summary : TextView? = null
    var temperature : TextView? = null
    var high : TextView? = null
    var low : TextView? = null
    var navDrawer : NavigationView? = null
    var drawerLayout : DrawerLayout? = null

    var myLocation : LocationAPI? = null
    var locationManager : LocationManager? = null
    val GPS_PERMISSION = 1

    val SPANISH_LANG = "es"
    val ENGLISH_LANG = "en"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_drawer)

        getViews()

        if (MyApplication.firstRun){
            MyApplication.saveSetting(MyApplication.keyNameFirstRun)

            drawerLayout?.openDrawer(Gravity.START)
        }

        navDrawer?.setNavigationItemSelectedListener ( this )

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


        var forecastCall : Call<Basic> = Forecast.getForecast().getForecastApi(latitude,longitude, MyApplication.language, MyApplication.measurement)

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
        navDrawer = findViewById(R.id.idNavDrawer) as NavigationView
        drawerLayout = findViewById(R.id.idDrawerLayout) as DrawerLayout
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

            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1800000, 1000f, myLocation)
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 1000f, myLocation)
        }

    }

    private fun ActivateGPSDialog(){

        var alert = AlertDialog.Builder(this).create()
        alert.setTitle(getString(R.string.alert_title))
        alert.setMessage(getString(R.string.alert_message))
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", { dialogInterface, i ->

            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)

        })

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_negative), { dialogInterface, i ->  })

        alert.show()
    }

    override fun onResume() {
        super.onResume()

        var gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (gpsEnabled!!) {


            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1800000, 1000f, myLocation)
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 1000f, myLocation)
        }
    }

    override fun onStop() {
        super.onStop()
        locationManager?.removeUpdates(myLocation)
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            moveTaskToBack(true)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            R.id.idExit -> { alertExitMessage()}
            R.id.idMeasurement -> {}
            R.id.idlanguage -> { createAlertDialogLanguage() }
        }

        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    private fun alertExitMessage() : Unit {

        val alert = AlertDialog.Builder(this).create()

        alert.setTitle(getString(R.string.exit))
        alert.setMessage(getString(R.string.exit_title_msg))
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No", { dialogInterface, i -> })
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), { dialogInterface, i ->
            this.finish()
        })
        alert.show()
    }

    private fun createAlertDialogLanguage() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.language))
        builder.setIcon(R.drawable.ic_language)
        builder.setMessage(getString(R.string.select_language))

        val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_define_language_item, null)
        builder.setView(viewInflated)

        var radioEnglish = viewInflated.findViewById(R.id.english) as RadioButton
        var radioSpanish = viewInflated.findViewById(R.id.spanish) as RadioButton

        if (MyApplication.language.compareTo("en") == 0) {
            radioEnglish.isChecked = true
        } else {
            radioSpanish.isChecked = true
        }

        //get the one from the system and check
        builder.setPositiveButton("OK") { dialogInterface, i ->
            val englishState = radioEnglish.isChecked
            val spanishState = radioSpanish.isChecked

            if (englishState && MyApplication.language.compareTo(ENGLISH_LANG) != 0) {

                MyApplication.language = ENGLISH_LANG
                MyApplication.saveSetting(MyApplication.keyNameLanguage)
                MyApplication.updateLanguage(resources)
                restartApp()

            } else if (spanishState && MyApplication.language.compareTo(SPANISH_LANG) != 0) {

                MyApplication.language = SPANISH_LANG
                MyApplication.saveSetting(MyApplication.keyNameLanguage)
                MyApplication.updateLanguage(resources)
                restartApp()

            }
        }.setNegativeButton(getString(R.string.cancel), { dialogInterface, i ->
            //no action required
        })

        builder.create().show()
    }

    private fun restartApp() {
        val intent1 = intent
        intent1.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        finish()
        startActivity(intent1)
    }


}


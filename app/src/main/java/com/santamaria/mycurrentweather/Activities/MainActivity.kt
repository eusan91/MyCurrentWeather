package com.santamaria.mycurrentweather.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
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
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
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

    private var forecastData : Basic? = null

    private var location : TextView? = null
    private var icon : ImageView? = null
    private var summary : TextView? = null
    private var temperature : TextView? = null
    private var high : TextView? = null
    private var low : TextView? = null
    private var navDrawer : NavigationView? = null
    private var drawerLayout : DrawerLayout? = null
    private var progressBar : ProgressBar? = null

    private var myLocation : LocationAPI? = null
    private var locationManager : LocationManager? = null
    private val GPS_PERMISSION = 1

    private val SPANISH_LANG = "es"
    private val ENGLISH_LANG = "en"

    private val CELSIUS_MEASUREMENT = "si"
    private val FARH_MEASUREMENT = "us"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_drawer)

        getViews()

        UtilityClass.loadAdds(this)

        if (MyApplication.firstRun){
            MyApplication.saveSetting(MyApplication.keyNameFirstRun)

            drawerLayout?.openDrawer(Gravity.START)
        }

        navDrawer?.setNavigationItemSelectedListener ( this )

        if (checkGPSPermission()){

            myLocation = LocationAPI(this)
            getCurrentLocation()

        }

    }

    private fun checkGPSPermission() : Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), GPS_PERMISSION)
                return false
            }
        }

        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == GPS_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                myLocation = LocationAPI(this)
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
                progressBar?.visibility = View.INVISIBLE
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
        progressBar = findViewById(R.id.idProgress) as ProgressBar
    }

    private fun loadMainActivity(latitude : Double, longitude : Double) {

        progressBar?.visibility = View.INVISIBLE
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
            location?.text = DirCalle.featureName
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

    }

    fun openDarkSkyWeb(view : View) {

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://darksky.net/poweredby/"))
        startActivity(browserIntent)

    }

    private fun getCurrentLocation(){

        if (checkGPSPermission()) {

            if (locationManager == null) {
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }

            var gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (!gpsEnabled!!) {
                ActivateGPSDialog()
            } else {

                progressBar?.visibility = View.VISIBLE

                if (myLocation == null) {
                    LocationAPI(this)
                }

                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1800000, 1000f, myLocation)
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 1000f, myLocation)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.idExit -> { alertExitMessage()}
            R.id.idMeasurement -> { createAlertDialogThermometric() }
            R.id.idlanguage -> { createAlertDialogLanguage() }
        }

        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    // --------- LIFE CYCLE overrides ------

    override fun onResume() {
        super.onResume()

        if (locationManager != null && myLocation != null) {
            var gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (temperature?.text!!.isEmpty()){
                progressBar?.visibility = View.VISIBLE
            }
            if (gpsEnabled!! && checkGPSPermission()) {
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1800000, 1000f, myLocation)
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1800000, 1000f, myLocation)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (locationManager != null && myLocation != null) {
            locationManager?.removeUpdates(myLocation)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            moveTaskToBack(true)
        }
    }

    // --------- ALERT DIALOGS ------

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

        if (MyApplication.language.compareTo(ENGLISH_LANG) == 0) {
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

            } else if (spanishState && MyApplication.language.compareTo(SPANISH_LANG) != 0) {

                MyApplication.language = SPANISH_LANG

            }

            MyApplication.saveSetting(MyApplication.keyNameLanguage)
            MyApplication.updateLanguage(resources)
            restartApp()

        }.setNegativeButton(getString(R.string.cancel), { dialogInterface, i ->
            //no action required
        })

        builder.create().show()
    }

    private fun createAlertDialogThermometric() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.title_measurement))
        builder.setIcon(R.drawable.ic_language)
        builder.setMessage(getString(R.string.select_measurement))

        val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_define_measurement_unit_item, null)
        builder.setView(viewInflated)

        var radioCelsius = viewInflated.findViewById(R.id.celsius) as RadioButton
        var radioFahrenheit = viewInflated.findViewById(R.id.fahrenheit) as RadioButton

        if (MyApplication.measurement.compareTo(CELSIUS_MEASUREMENT) == 0) {
            radioCelsius.isChecked = true
        } else {
            radioFahrenheit.isChecked = true
        }

        //get the one from the system and check
        builder.setPositiveButton("OK") { dialogInterface, i ->
            val radioCelsius = radioCelsius.isChecked
            val radioFahrenheit = radioFahrenheit.isChecked

            if (radioCelsius && MyApplication.measurement.compareTo(CELSIUS_MEASUREMENT) != 0) {

                MyApplication.measurement = CELSIUS_MEASUREMENT

            } else if (radioFahrenheit && MyApplication.language.compareTo(FARH_MEASUREMENT) != 0) {

                MyApplication.measurement = FARH_MEASUREMENT

            }

            MyApplication.saveSetting(MyApplication.keyNameMeasurement)
            getCurrentLocation()

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


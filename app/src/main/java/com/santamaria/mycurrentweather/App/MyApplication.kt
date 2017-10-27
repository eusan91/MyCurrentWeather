package com.santamaria.mycurrentweather.App

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

/**
 * Created by Santamaria on 26/10/2017.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE)

        language = MyApplication.sharedPreferences!!.getString(MyApplication.keyNameLanguage, "en")
        measurement = MyApplication.sharedPreferences!!.getString(MyApplication.keyNameMeasurement, "si")//us
        firstRun = MyApplication.sharedPreferences!!.getBoolean(MyApplication.keyNameFirstRun, true)

        if (firstRun){
            language = getCellphoneLanguage()
            saveSetting(MyApplication.keyNameLanguage)
        }

        updateLanguage(resources)

    }

    //SharedPreferences variables
    companion object variables {
        val SharedPreferencesName = "MyCurrentWeather_Settings"
        val keyNameLanguage = "LANGUAGE"
        val keyNameMeasurement = "MEASUREMENT"
        val keyNameFirstRun = "FIRST_RUN"
        var sharedPreferences: SharedPreferences? = null

        var language = ""
        var measurement = ""
        var firstRun : Boolean = true

        fun updateLanguage(resources : Resources){

            val config = Configuration()
            config.setLocale(Locale(language))
            resources.updateConfiguration(config, resources.displayMetrics)

        }

        fun saveSetting(key: String) {

            var editor = MyApplication.sharedPreferences!!.edit()

            if (key == MyApplication.keyNameMeasurement){
                editor.putString(key, measurement)
            } else if (key == MyApplication.keyNameLanguage){
                editor.putString(key, language)
            } else if (firstRun && key == MyApplication.keyNameFirstRun){
                firstRun = false
                editor.putBoolean(key, false)
            }

            editor.commit()

        }
    }

    private fun getCellphoneLanguage() : String{

        var lang = Locale.getDefault().language

        if (lang.compareTo("es", true) != 0 ){
            lang = "en"
        }

        return lang
    }
}
package com.santamaria.mycurrentweather

/**
 * Created by Santamaria on 21/10/2017.
 */
class Weathear {

    var currentWeathearName: String = ""
    var currentWeatherNumber : Int = 0
    var highTemp : Int = 0
    var lowTemp : Int = 0
    var iconName : String = ""

    constructor(currentWeatherName: String, currentWeatherNumber : Int, highTemp : Int, lowTemp : Int, iconName : String){

        this.currentWeathearName = currentWeatherName
        this.currentWeatherNumber = currentWeatherNumber
        this.highTemp = highTemp
        this.lowTemp = lowTemp
        this.iconName = iconName
    }

    constructor()


}
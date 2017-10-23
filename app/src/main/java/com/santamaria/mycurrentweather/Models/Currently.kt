package com.santamaria.mycurrentweather.Models

/**
 * Created by Santamaria on 22/10/2017.
 */
class Currently {

    var summary: String = ""
    var temperature: Double = 0.0
    var icon: String = ""

    constructor(summary: String, temperature : Double, icon : String){

        this.summary = summary
        this.temperature = temperature
        this.icon = icon
    }
}
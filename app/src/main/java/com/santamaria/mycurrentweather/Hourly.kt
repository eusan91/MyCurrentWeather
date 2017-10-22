package com.santamaria.mycurrentweather

/**
 * Created by Santamaria on 21/10/2017.
 */
class Hourly {

    var iconName : String = ""
    var hour : Int = 0
    var summary : String = ""

    constructor(iconName: String, hour: Int, summary: String) {
        this.iconName = iconName
        this.hour = hour
        this.summary = summary
    }
}
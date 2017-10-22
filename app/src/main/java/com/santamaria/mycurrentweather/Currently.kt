package com.santamaria.mycurrentweather

/**
 * Created by Santamaria on 22/10/2017.
 */
class Currently {

    var summary: String = ""
    var precipProbability: Int = 0
    var icon: String = ""

    constructor(summary: String, precipProbability : Int, icon : String){

        this.summary = summary
        this.precipProbability = precipProbability
        this.icon = icon
    }
}
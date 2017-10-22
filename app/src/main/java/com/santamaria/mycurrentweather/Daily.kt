package com.santamaria.mycurrentweather

/**
 * Created by Santamaria on 21/10/2017.
 */
class Daily {

    var iconName = ""
    var time : Int = 0
    var summary = ""
    var rainProbability : Int = 0

    constructor(iconName: String, time: Int, summary: String, rainProbability: Int) {
        this.iconName = iconName
        this.time = time
        this.summary = summary
        this.rainProbability = rainProbability
    }
}
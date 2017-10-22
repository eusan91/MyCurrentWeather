package com.santamaria.mycurrentweather

/**
 * Created by Santamaria on 21/10/2017.
 */
class Minutely {

    var iconName : String = ""
    var minute : Int = 0
    var probability : Int = 0

    constructor(iconName: String, minute: Int, probability: Int) {
        this.iconName = iconName
        this.minute = minute
        this.probability = probability
    }
}
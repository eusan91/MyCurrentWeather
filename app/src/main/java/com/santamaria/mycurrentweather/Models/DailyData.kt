package com.santamaria.mycurrentweather.Models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Santamaria on 22/10/2017.
 */
class DailyData() : Parcelable {

    var time : Long = 0
    var summary : String = ""
    var icon : String = ""
    var precipProbability : Double = 0.0
    var temperatureLow : Double = 0.0
    var temperatureHigh : Double = 0.0

    constructor(parcel: Parcel) : this() {
        time = parcel.readLong()
        summary = parcel.readString()
        icon = parcel.readString()
        precipProbability = parcel.readDouble()
        temperatureLow = parcel.readDouble()
        temperatureHigh = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
        parcel.writeString(summary)
        parcel.writeString(icon)
        parcel.writeDouble(precipProbability)
        parcel.writeDouble(temperatureLow)
        parcel.writeDouble(temperatureHigh)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DailyData> {
        override fun createFromParcel(parcel: Parcel): DailyData {
            return DailyData(parcel)
        }

        override fun newArray(size: Int): Array<DailyData?> {
            return arrayOfNulls(size)
        }
    }

}
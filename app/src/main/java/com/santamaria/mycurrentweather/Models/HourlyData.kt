package com.santamaria.mycurrentweather.Models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Santamaria on 22/10/2017.
 */
class HourlyData() : Parcelable {

    var time : Long = 0
    var summary : String = ""
    var icon : String = ""

    constructor(parcel: Parcel) : this() {
        time = parcel.readLong()
        summary = parcel.readString()
        icon = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
        parcel.writeString(summary)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HourlyData> {
        override fun createFromParcel(parcel: Parcel): HourlyData {
            return HourlyData(parcel)
        }

        override fun newArray(size: Int): Array<HourlyData?> {
            return arrayOfNulls(size)
        }
    }

}
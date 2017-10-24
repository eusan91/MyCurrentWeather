package com.santamaria.mycurrentweather.Models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Santamaria on 21/10/2017.
 */
class Hourly(var icon : String, var data : ArrayList<HourlyData>? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(HourlyData.CREATOR)) {
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(icon)
        parcel.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hourly> {
        override fun createFromParcel(parcel: Parcel): Hourly {
            return Hourly(parcel)
        }

        override fun newArray(size: Int): Array<Hourly?> {
            return arrayOfNulls(size)
        }
    }
}
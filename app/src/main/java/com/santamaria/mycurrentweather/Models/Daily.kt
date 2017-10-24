package com.santamaria.mycurrentweather.Models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Santamaria on 21/10/2017.
 */
class Daily(var summary : String, var icon : String, var data : ArrayList<DailyData>? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(DailyData.CREATOR)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(summary)
        parcel.writeString(icon)
        parcel.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Daily> {
        override fun createFromParcel(parcel: Parcel): Daily {
            return Daily(parcel)
        }

        override fun newArray(size: Int): Array<Daily?> {
            return arrayOfNulls(size)
        }
    }


}
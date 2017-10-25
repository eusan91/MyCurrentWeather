package com.santamaria.mycurrentweather.Adapters

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.santamaria.mycurrentweather.Models.HourlyData
import com.santamaria.mycurrentweather.R
import com.santamaria.mycurrentweather.UtilityClass
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by santamae on 10/24/2017.
 */
class HourlyAdapter(var context : Context?, var data: ArrayList<HourlyData>, var layout: Int) : BaseAdapter() {

    var dfOnlyDate = SimpleDateFormat("dd/MM")
    var df = SimpleDateFormat("HH:mm")

    var firstIdxDays : LinkedList<Int>? = null

    init {

        firstIdxDays = LinkedList()

        firstIdxDays!!.add(0)
        var day = dfOnlyDate.format(data[0].time*1000)

        for ((idx, value) in data.withIndex()){

            if (dfOnlyDate.format(value.time*1000) != day){
                firstIdxDays!!.add(idx)
                day = dfOnlyDate.format(value.time*1000)
            }
        }


    }
    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
       return 0
    }

    override fun getCount(): Int {

        if (data != null) {
            return data.size
        }

        return 0
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

        var view : View
        var viewHolder : ViewHolder

        if ( convertView == null ) {

            view = LayoutInflater.from(context).inflate(layout, viewGroup, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder

        } else {

            view = convertView
            viewHolder = view.tag as ViewHolder

        }

        var hourlyData = data[position]

        var cal = Calendar.getInstance()
        var dayDate = Date(hourlyData.time*1000)
        cal.time = dayDate


        df.timeZone = TimeZone.getDefault();

        if (firstIdxDays!!.contains(position)) {
            viewHolder.tvGroupDay.text = UtilityClass.getStrDayFromNumber(cal.get(Calendar.DAY_OF_WEEK)) + " " + dfOnlyDate.format(dayDate)
            viewHolder.tvGroupDay.visibility = View.VISIBLE

        } else {
            viewHolder.tvGroupDay.visibility = View.GONE
        }

        viewHolder.tvDay!!.text = df.format(dayDate)
        viewHolder?.tvSummary?.text = hourlyData.summary
        viewHolder?.ivIcon?.setImageResource(UtilityClass.getImage((hourlyData.icon)))

        return view
    }

    inner class ViewHolder (var view : View ){

        var tvGroupDay : TextView
        var tvDay : TextView
        var ivIcon : ImageView
        var tvSummary : TextView

        init {
            tvGroupDay = view.findViewById(R.id.idGroupDay) as TextView
            tvDay = view.findViewById(R.id.idDay) as TextView
            ivIcon = view.findViewById(R.id.idIcon) as ImageView
            tvSummary = view.findViewById(R.id.idSummary) as TextView
        }
    }

}
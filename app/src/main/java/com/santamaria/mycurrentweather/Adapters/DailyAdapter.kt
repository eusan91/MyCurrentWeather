package com.santamaria.mycurrentweather.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.santamaria.mycurrentweather.Models.DailyData
import com.santamaria.mycurrentweather.R
import com.santamaria.mycurrentweather.UtilityClass
import java.util.*

/**
 * Created by Santamaria on 23/10/2017.
 */
class DailyAdapter(var context : Context, var dailyList : ArrayList<DailyData>, var layout : Int) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return dailyList[position]
    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getCount(): Int {

        if (dailyList != null ) {
            return dailyList.size
        }

        return 0
    }

    override fun getView(position: Int, viewRoot: View?, viewGroup: ViewGroup?): View {

        var viewHolder : ViewHolder
        var view : View

        if (viewRoot == null){
            view = LayoutInflater.from(context).inflate(layout, viewGroup, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
         } else {
            view = viewRoot
            viewHolder = viewRoot.tag as ViewHolder
        }

        val daily = dailyList[position]

        var cal = Calendar.getInstance()
        var dayDate = Date(daily.time*1000)
        cal.time = dayDate

        if (position == 0) {
            viewHolder.tvDay!!.text = "Today"
        } else {
            viewHolder.tvDay!!.text = UtilityClass.getStrDayFromNumber(context!!, cal.get(Calendar.DAY_OF_WEEK))
        }

        viewHolder.ivIcon!!.setImageResource(UtilityClass.getImage(daily.icon))
        viewHolder.tvProbability!!.text= Math.round(daily.precipProbability).toString() + "%"
        viewHolder.tvSummary!!.text= daily.summary

        return view
    }

    inner class ViewHolder (var view : View){
        var tvDay : TextView
        var ivIcon : ImageView
        var tvSummary : TextView
        var tvProbability : TextView

        init {
            tvDay = view.findViewById(R.id.idDay) as TextView
            ivIcon = view.findViewById(R.id.idIcon) as ImageView
            tvSummary = view.findViewById(R.id.idSummary) as TextView
            tvProbability = view.findViewById(R.id.idProbability) as TextView
        }

    }
}


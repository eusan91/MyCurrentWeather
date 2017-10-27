package com.santamaria.mycurrentweather.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.santamaria.mycurrentweather.Adapters.HourlyAdapter
import com.santamaria.mycurrentweather.Models.Hourly

import com.santamaria.mycurrentweather.R
import com.santamaria.mycurrentweather.UtilityClass

class HourlyActivity : AppCompatActivity() {

    var hourly : Hourly? = null
    var customAdapter : BaseAdapter? = null

    var listViewHourly : ListView? = null
    var tvNoData : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly)

        listViewHourly = findViewById(R.id.idListviewHourly) as ListView
        tvNoData = findViewById(R.id.idNoData) as TextView?

        UtilityClass.loadAdds(this)

        var extras = intent.extras

        if ( extras != null ){
            hourly = extras.get("hourly") as Hourly?
        }

        if (hourly?.data != null && hourly?.data!!.size > 0) {
            customAdapter = HourlyAdapter(applicationContext, hourly?.data!!, R.layout.hourly_item)
            tvNoData?.visibility = View.INVISIBLE
        } else {
            tvNoData?.visibility = View.VISIBLE
        }

        if (customAdapter != null){
            listViewHourly?.adapter = customAdapter
        }

    }
}



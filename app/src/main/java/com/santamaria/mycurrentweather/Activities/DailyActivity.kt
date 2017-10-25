package com.santamaria.mycurrentweather.Activities

import android.app.ListActivity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.santamaria.mycurrentweather.Adapters.DailyAdapter
import com.santamaria.mycurrentweather.Models.Daily

import com.santamaria.mycurrentweather.R

class DailyActivity : AppCompatActivity() {

    var daily : Daily? = null
    var customAdapter: BaseAdapter? = null
    var tvNoData : TextView? = null

    var listViewDaily : ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        listViewDaily = findViewById(R.id.idListViewDaily) as ListView?
        tvNoData = findViewById(R.id.idNoData) as TextView?

        var bundle = intent.extras

        if (bundle != null) {
            daily = bundle.get("daily") as Daily?
        }

        if (daily?.data != null && daily?.data!!.size > 0) {
            customAdapter = DailyAdapter(applicationContext, daily?.data!!, R.layout.daily_item)
            tvNoData?.visibility = View.INVISIBLE
        } else {
            tvNoData?.visibility = View.VISIBLE
        }

        if (customAdapter != null){
            listViewDaily?.adapter = customAdapter
        }

    }
}

package com.santamaria.mycurrentweather.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.BaseAdapter
import android.widget.ListView
import com.santamaria.mycurrentweather.Adapters.DailyAdapter
import com.santamaria.mycurrentweather.Models.Daily

import com.santamaria.mycurrentweather.R

class DailyActivity : AppCompatActivity() {

    var daily : Daily? = null
    var customAdapter: BaseAdapter? = null

    var listViewDaily : ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        listViewDaily = findViewById(R.id.idListViewDaily) as ListView?

        var bundle = intent.extras

        if (bundle != null) {
            daily = bundle.get("daily") as Daily
        }

        if (daily?.data != null && daily?.data!!.size > 0) {
            customAdapter = DailyAdapter(applicationContext, daily?.data!!, R.layout.daily_item)
        }

        if (customAdapter != null){
            listViewDaily?.adapter = customAdapter
        }

    }
}

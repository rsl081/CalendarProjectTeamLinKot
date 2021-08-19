package com.example.calendarprojectteamlinkot.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.Constants
import com.example.calendarprojectteamlinkot.models.MonthModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var kalendarList:ArrayList<MonthModel>? = null

    private var monthAdapter: MonthAdapter? = null

    private var yearAdapter: YearAdapter? = null

    private var currentMonthPosition = -1

    var i = 0
    var k = 0
    var rnx = 7
    var l = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("Exa", currentMonthPosition.toString())
        init()
        onClick()
    }

    fun onClick()
    {
        rightarrow.setOnClickListener{
//            l.clear()
//
//            k += 7
//            rnx += 7
//            Log.v("happy", k.toString() +" || "+ rnx.toString())
//            var arrList = kalendarList!![currentMonthPosition+1].days
//
//            for (a in arrList)
//            {
//                if(i < rnx)
//                {
//                    l.add(arrList[i])
//
//                }
//                i++
//            }
//            monthAdapter?.updateData(l)

            if(currentMonthPosition < 10)
            {
                currentMonthPosition++
                tvyearAndMonth.text = kalendarList!![currentMonthPosition+1].month.toString() + " " + kalendarList!![currentMonthPosition+1].year.toString()
                monthAdapter?.updateData(kalendarList!![currentMonthPosition+1].days)
            }
        }

        leftarrow.setOnClickListener{
            if(currentMonthPosition > -1)
            {
                currentMonthPosition--
                tvyearAndMonth.text = kalendarList!![currentMonthPosition+1].month.toString() + " " + kalendarList!![currentMonthPosition+1].year.toString()
                monthAdapter?.updateData(kalendarList!![currentMonthPosition+1].days)
            }
        }
    }

    fun init()
    {
        kalendarList = Constants.defaultKalendarList()
        tvyearAndMonth.text = kalendarList!![currentMonthPosition+1].month.toString() + " " + kalendarList!![currentMonthPosition+1].year.toString()
        monthView()
        tvview.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view: View = layoutInflater.inflate(
                R.layout.dropdown_item_view,null,false)
            builder.setTitle("View")
            // access the items of the list
            val languages = resources.getStringArray(R.array.view)
            // access the spinner
            val spinner = view.findViewById<Spinner>(R.id.mspinner)
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                if(spinner.selectedItem.toString() == "Month")
                {
                    monthView()
                }else if(spinner.selectedItem.toString() == "Year")
                {
                    yearView()
                }
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }
            builder.setView(view)
            builder.show()

        }
    }//End of Initialization

    private fun monthView()
    {
        constraintLayoutWeek.visibility = View.VISIBLE
        cons_rycler.visibility = View.VISIBLE
        cns_yearview.visibility = View.GONE
        //val calendar = Calendar.getInstance()

        // Set the LayoutManager that this RecyclerView will use.
        rvItemsList.layoutManager = GridLayoutManager(this, 7)

        // Adapter class is initialized and list is passed in the param.
        monthAdapter = MonthAdapter(this, kalendarList!![currentMonthPosition+1].days)

        // adapter instance is set to the recyclerview to inflate the items.
        rvItemsList.adapter = monthAdapter
    }

    private fun yearView()
    {
        constraintLayoutWeek.visibility = View.GONE
        cons_rycler.visibility = View.GONE
        cns_yearview.visibility = View.VISIBLE

        // Set the LayoutManager that this RecyclerView will use.
        rvYearList.layoutManager = GridLayoutManager(this, 2)

        // Adapter class is initialized and list is passed in the param.
        yearAdapter = YearAdapter(this, kalendarList!!)

        // adapter instance is set to the recyclerview to inflate the items.
        rvYearList.adapter = yearAdapter
    }

    private fun weekView()
    {
//        var arrList = kalendarList!![currentMonthPosition+1].days
//
//        var i = 0
//
//        for (a in arrList)
//        {
//            if(i < 7)
//            {
//                week!!.add(a)
//                Log.v("happy", week.toString())
//            }
//            i++
//        }


        // Set the LayoutManager that this RecyclerView will use.
        rvItemsList.layoutManager = GridLayoutManager(this, 7)

        // Adapter class is initialized and list is passed in the param.
        monthAdapter = MonthAdapter(this, x())

        // adapter instance is set to the recyclerview to inflate the items.
        rvItemsList.adapter = monthAdapter
    }

    fun x(): ArrayList<Int>
    {
        //var l = ArrayList<Int>()
        var arrList = kalendarList!![currentMonthPosition+1].days

        for (a in arrList)
        {

            if(i < rnx)
            {
                l.add(arrList[i])
                Log.v("happy", l.toString())
            }
            i++
        }
        return l
    }

}

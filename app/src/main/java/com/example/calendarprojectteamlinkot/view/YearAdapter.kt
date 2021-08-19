package com.example.calendarprojectteamlinkot.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.MonthModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_year_adapter.view.*

class YearAdapter(val context: Context, val items: ArrayList<MonthModel>) :
    RecyclerView.Adapter<YearAdapter.ViewHolder>()
{
    var monthList: ArrayList<MonthModel> = items
    private var monthAdapter: MonthAdapter? = null
    private var textSize: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.activity_year_adapter,
            parent,
            false
        )

        itemView.minimumHeight = parent.measuredHeight / 4
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {

        val item = monthList.get(position)

        holder.tvyearAndMonth.text = item.month.toString() + " " + item.year

        // Set the LayoutManager that this RecyclerView will use.
        holder.recyMon.layoutManager = GridLayoutManager(context, 7)

        // Adapter class is initialized and list is passed in the param.
        monthAdapter = MonthAdapter(context, item.days)

        // adapter instance is set to the recyclerview to inflate the items.
        holder.recyMon.adapter = monthAdapter
    }


    override fun getItemCount(): Int {
        return monthList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Holds the TextView that will add each item to
        val tvyearAndMonth = view.tvyearAndMonth
        val recyMon = view.rvYearList2
    }
}
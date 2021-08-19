package com.example.calendarprojectteamlinkot.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarprojectteamlinkot.R
import kotlinx.android.synthetic.main.activity_month_adapter.view.*
import java.util.*
import kotlin.collections.ArrayList

class MonthAdapter(val context: Context, val items: ArrayList<Int>) :
        RecyclerView.Adapter<MonthAdapter.ViewHolder>()
{

    var dataSource: ArrayList<Int> = items
    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(context).inflate(
//                R.layout.activity_kalendar_adapter,
//                parent,
//                false
//            )
//        )
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.activity_month_adapter,
            parent,
            false
        )

        itemView.minimumHeight = parent.measuredHeight / 6
        return ViewHolder(itemView)
    }
    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = dataSource.get(position)
        val calendar = Calendar.getInstance()

        holder.tvItem.text = item.toString()

//        // Updating the background color according to the odd/even positions in list.
//        if (position % 2 == 0) {
//            holder.tvItem.setBackgroundColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.colorLightGray
//                )
//            )
//        } else {
//            holder.tvItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
//        }
    }
    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return dataSource.size
    }
    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val tvItem = view.tvItem
    }

    fun updateData(items: ArrayList<Int>) {
        this.dataSource = items
        notifyDataSetChanged()
    }
}

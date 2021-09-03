package com.example.calendarprojectteamlinkot.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.tasks_item.view.*

class TaskListItemsAdapter(private val context: Context,
                           private var list: List<Task>
):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.tasks_item,
                parent,
                false
            )
        )
//        val binding = TasksItemBinding.inflate(LayoutInflater.from(parent.context),
//            parent,
//            false)
//        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            val name = model.assignee

                holder.itemView.tv_task_name.text = model.name
                model.id?.let { Log.i("idtaskss", it) }
                holder.itemView.tv_description.text = model.isCompleted.toString()
                holder.itemView.tv_username.text = name?.username
                holder.itemView.expanded_view.visibility = if (model.expand) View.VISIBLE else View.GONE
                holder.itemView.card_layout.setOnClickListener {
                    model.expand = !model.expand
                    notifyDataSetChanged()
                }

            ApiClass().getCurrentUser {
                if(it != name?.username){
                    holder.itemView.cb_task_item.isEnabled = false
                    holder.itemView.cb_task_item.alpha = 0.10F
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
//    inner class MyViewHolder(val binding: TasksItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}
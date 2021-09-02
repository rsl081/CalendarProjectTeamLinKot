package com.example.calendarprojectteamlinkot.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.databinding.TasksItemBinding
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import kotlinx.android.synthetic.main.activity_day.*
class TaskListItemsAdapter(private val context: Context,
                           private var list: List<Task>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return MyViewHolder(
//            LayoutInflater.from(context).inflate(
//                R.layout.tasks_item,
//                parent,
//                false
//            )
//        )
        val binding = TasksItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            val name = model.assignee
//            ApiClass().getCurrentUser{
//                if(it == name?.username){
//                    //green mismo pero yung lahat dapat walang kulay
//                    holder.itemView.tv_item_task.visibility = View.VISIBLE
//                    holder.itemView.tv_item_task.text = model.name
//                }else{
//                    holder.itemView.tv_item_task.visibility = View.GONE
//                }
//            }

            with(holder){
                binding.tvUsername.text = name?.username
                binding.tvTaskName.text = model.name
                binding.tvDescription.text = model.description
                binding.expandedView.visibility = if (model.expand) View.VISIBLE else View.GONE
                binding.cardLayout.setOnClickListener {
                    model.expand = !model.expand
                    notifyDataSetChanged()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

//    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class MyViewHolder(val binding: TasksItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}
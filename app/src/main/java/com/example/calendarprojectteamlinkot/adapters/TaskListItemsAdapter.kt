package com.example.calendarprojectteamlinkot.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarprojectteamlinkot.R
import com.example.calendarprojectteamlinkot.models.EditTask
import com.example.calendarprojectteamlinkot.models.Task
import com.example.calendarprojectteamlinkot.models.User
import com.example.calendarprojectteamlinkot.repository.ApiClass
import com.example.calendarprojectteamlinkot.utils.Constants
import com.example.calendarprojectteamlinkot.view.CreateTaskActivity
import kotlinx.android.synthetic.main.tasks_item.view.*

class TaskListItemsAdapter(private val context: Context,
                           private var list: MutableList<Task>
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
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            val user = model.assignee
            val createdBy = model.createdBy

                holder.itemView.tv_task_name.text = model.name
                holder.itemView.tv_description.text = model.date
                holder.itemView.tv_username.text = user?.username
                holder.itemView.expanded_view.visibility = if (model.expand) View.VISIBLE else View.GONE
                holder.itemView.card_layout.setOnClickListener {
                    model.expand = !model.expand
                    notifyItemChanged(position)
                    //notifyDataSetChanged()
                }

            ApiClass().getCurrentUser {
                if(it != user?.username){
                    holder.itemView.cb_task_item.isEnabled = false

                    holder.itemView.cb_task_item.alpha = 1.0F

                }else {
                    holder.itemView.card_layout.setBackgroundColor(Color.parseColor("#C5E8B7"))
                }
            }

            holder.itemView.cb_task_item.isChecked = list[position].isCompleted == true

            holder.itemView.cb_task_item.setOnClickListener {
                ApiClass().checkTask(context, model.id)
            }

            holder.itemView.delete_tasks_item.setOnClickListener {
                if(createdBy.username.equals(user?.username)){
                    ApiClass().deleteTask(context, model.id)
                    deleteItem(position)
                }else{
                    Toast.makeText(context, "Only the task creator can delete this", Toast.LENGTH_SHORT).show();
                }
            }

            holder.itemView.edit_tasks_item.setOnClickListener {
                if(createdBy.username.equals(user?.username)){
                    val editTask = EditTask(model.id, model.name, model.description,User(user?.username,null,null),model.date)
                    val intent = Intent(Intent(context, CreateTaskActivity::class.java))
                    intent.putExtra(Constants.TASK_DETAIL, editTask)
                    context.startActivity(intent)
                }else{
                    Toast.makeText(context, "Only the task creator can delete this", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    fun deleteItem(index: Int){
        list.removeAt(index)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}
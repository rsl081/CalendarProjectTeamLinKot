package com.example.calendarprojectteamlinkot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarprojectteamlinkot.databinding.ActivityTaskBinding
import com.example.calendarprojectteamlinkot.models.RvAdapter
import com.example.calendarprojectteamlinkot.models.Tasks

class TaskActivity : AppCompatActivity() {

    private var _binding: ActivityTaskBinding? = null
    private val binding get() = _binding!!

    // get reference to the adapter class
    private var taskList = ArrayList<Tasks>()
    private lateinit var rvAdapter: RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // define layout manager for the Recycler view
        binding.rvList.layoutManager = LinearLayoutManager(this)

        // attach adapter to the recycler view
        rvAdapter = RvAdapter(taskList)
        binding.rvList.adapter = rvAdapter

        // create new objects
        // add some row data
        val task1 = Tasks(
            "Task",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            false
        )
        val task2 = Tasks(
            "Task2",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            false
        )
        val task3 = Tasks(
            "Task3",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            false
        )
        val task4 = Tasks(
            "Task4",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua",
            false
        )

        // add items to list
        taskList.add(task1)
        taskList.add(task2)
        taskList.add(task3)
        taskList.add(task4)

        rvAdapter.notifyDataSetChanged()

    }

    // on destroy of view make the binding reference to null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
package com.learn.todolistkotlin.fragmen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.learn.todolistkotlin.R
import com.learn.todolistkotlin.activity.AllActivity
import com.learn.todolistkotlin.activity.EditActivity
import com.learn.todolistkotlin.adapter.TaskAdapter
import com.learn.todolistkotlin.adapter.TaskCompletedAdapter
import com.learn.todolistkotlin.database.DatabaseClient
import com.learn.todolistkotlin.database.TaskDao
import com.learn.todolistkotlin.database.TaskModel
import com.learn.todolistkotlin.databinding.FragmentHomeBinding
import com.learn.todolistkotlin.util.dateToLong
import com.learn.todolistkotlin.util.dateToday

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: TaskDao
    private lateinit var taskSelected: TaskModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService(requireActivity()).taskDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupListListener()
        setupData()
    }

    override fun onStart() {
        super.onStart()
        setupData()
    }

    private fun setupList(){
        binding.listTask.adapter = taskAdapter
        binding.listTaskCompleted.adapter = taskCompletedAdapter

    }

    private fun setupListListener(){
        binding.labelTaskCompleted.setOnClickListener {
            if(binding.listTaskCompleted.visibility == View.GONE){
                binding.listTaskCompleted.visibility == View.VISIBLE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_down)
            } else{
                binding.listTaskCompleted.visibility == View.GONE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_forward)
            }
        }
        binding.fabAdd.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
//            testInsert()
        }
        binding.textTask.setOnClickListener {
            startActivity(Intent(requireActivity(), AllActivity::class.java))
        }
    }

    private fun setupData(){
        database.taskAll(
            false,
            dateToLong(dateToday())
        ).observe(viewLifecycleOwner, {
            Log.e("taskAll", it.toString())
            taskAdapter.addList(it)
            binding.textAlert.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
        })
        database.taskAll(
            true,
            dateToLong(dateToday())
        ).observe(viewLifecycleOwner, {
            Log.e("taskAllCompleted", it.toString())
            taskCompletedAdapter.addList(it)
            binding.labelTaskCompleted.visibility = if(it.isEmpty()) View.GONE else View.VISIBLE
            binding.imageTaskCompleted.visibility = if(it.isEmpty()) View.GONE else View.VISIBLE
        })
    }

    private val taskAdapter by lazy {
        TaskAdapter(arrayListOf(), object : TaskAdapter.AdapterListener{
            override fun onUpdate(taskModel: TaskModel) {
                taskSelected = taskModel
                taskSelected.completed = true
                Thread{
                    database.Update(taskSelected)
                }.start()
            }
            override fun onDetail(taskModel: TaskModel) {
                startActivity(Intent(
                    requireActivity(), EditActivity::class.java
                ).putExtra("intent_task",taskModel)
                )
            }
        })
    }

    private val taskCompletedAdapter by lazy {
        TaskCompletedAdapter(arrayListOf(), object : TaskCompletedAdapter.AdapterListener{
            override fun onClick(taskModel: TaskModel) {
                taskSelected = taskModel
                taskSelected.completed = false
                Thread{
                    database.Update(taskSelected)
                }.start()
            }
        })
    }
}
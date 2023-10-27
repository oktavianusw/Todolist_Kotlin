package com.learn.todolistkotlin.fragmen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.learn.todolistkotlin.R
import com.learn.todolistkotlin.activity.AllActivity
import com.learn.todolistkotlin.activity.EditActivity
import com.learn.todolistkotlin.adapter.TaskAdapter
import com.learn.todolistkotlin.adapter.TaskCompletedAdapter
import com.learn.todolistkotlin.database.DatabaseClient
import com.learn.todolistkotlin.database.TaskDao
import com.learn.todolistkotlin.database.TaskModel
import com.learn.todolistkotlin.databinding.FragmentAllBinding
import com.learn.todolistkotlin.util.dateToday


class AllFragment : Fragment() {

    private lateinit var binding: FragmentAllBinding
    private lateinit var database: TaskDao
    private lateinit var taskSelected: TaskModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBinding.inflate(inflater, container, false)
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
        binding.imageMenu.setOnClickListener{ imageMenu ->
            PopupMenu(requireActivity(), imageMenu) .apply {
                setOnMenuItemClickListener { item ->
                    when (item?.itemId) {
                        R.id.action_new -> {
                            findNavController().navigate(R.id.action_allFragment_to_addFragment)
                            true
                        }
                        R.id.action_delete -> {
                            Thread{
                                database.deleteCompleted()
                            }.start()
                            true
                        }
                        R.id.action_delete_all-> {
                            Thread{
                                database.deleteAll()
                            }.start()
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.menu_task_all)
            }
        }
        binding.labelTaskCompleted.setOnClickListener {
            if(binding.listTaskCompleted.visibility == View.GONE){
                binding.listTaskCompleted.visibility == View.VISIBLE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_down)
            } else{
                binding.listTaskCompleted.visibility == View.GONE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_forward)
            }
        }
    }

    private fun setupData(){
        database.taskAll(false).observe(viewLifecycleOwner, {
            Log.e("taskAll", it.toString())
            taskAdapter.addList(it)
            binding.textAlert.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
        })
        database.taskAll(true).observe(viewLifecycleOwner, {
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
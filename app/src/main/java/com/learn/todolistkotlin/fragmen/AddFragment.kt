package com.learn.todolistkotlin.fragmen

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.learn.todolistkotlin.R
import com.learn.todolistkotlin.database.DatabaseClient
import com.learn.todolistkotlin.database.TaskDao
import com.learn.todolistkotlin.database.TaskModel
import com.learn.todolistkotlin.databinding.FragmentAddBinding
import com.learn.todolistkotlin.util.dateToDialog
import com.learn.todolistkotlin.util.dateToLong
import com.learn.todolistkotlin.util.dateToString
import com.learn.todolistkotlin.util.dateToday


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var database: TaskDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService(requireActivity()).taskDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.e("myDate", System.currentTimeMillis().toString())
//        Log.e("myDate", dateToString(System.currentTimeMillis()))
//        binding.textDate.text = dateToday()
        binding.textDate.text = dateToday()

        binding.labelDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textDate.text = dateToString(year, month, dayOfMonth)
            }
            dateToDialog(requireActivity(), datePicker).show()
        }
        binding.buttonSave.setOnClickListener{
            val task = TaskModel(id=0, task = binding.editTask.text.toString(), false, dateToLong(binding.textDate.text.toString()))
            Thread{
                database.insert(task)
                requireActivity().runOnUiThread{
                    Toast.makeText(requireActivity(), "Berhasil", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }.start()
        }
    }

}
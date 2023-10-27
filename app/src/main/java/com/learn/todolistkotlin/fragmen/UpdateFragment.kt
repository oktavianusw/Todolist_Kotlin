package com.learn.todolistkotlin.fragmen

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.learn.todolistkotlin.R
import com.learn.todolistkotlin.database.DatabaseClient
import com.learn.todolistkotlin.database.TaskDao
import com.learn.todolistkotlin.database.TaskModel
import com.learn.todolistkotlin.databinding.FragmentDetailBinding
import com.learn.todolistkotlin.databinding.FragmentUpdateBinding
import com.learn.todolistkotlin.util.dateToDialog
import com.learn.todolistkotlin.util.dateToLong
import com.learn.todolistkotlin.util.dateToString


class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var database: TaskDao
    private lateinit var detail: TaskModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService(requireActivity()).taskDao()
        detail = requireArguments().getSerializable("argument_task") as TaskModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupListener()
    }

    private fun setupData() {
        binding.editTask.setText(detail.task)
        binding.textDate.text = dateToString(detail.date)
    }

    private fun setupListener() {
        binding.buttonSave.setOnClickListener{
            detail.task = binding.editTask.text.toString()
            detail.date = dateToLong(binding.textDate.text.toString())
            Thread{
                database.Update(detail)
                requireActivity().runOnUiThread{
                    Toast.makeText(requireActivity(), "Perubahan Disimpan", Toast.LENGTH_LONG).show()
                }
            }.start()
        }
        binding.buttonDelete.setOnClickListener {
            Thread{
                database.delete(detail)
                requireActivity().runOnUiThread{
                    Toast.makeText(requireActivity(), "Berhasil Dihapus", Toast.LENGTH_LONG).show()
                    requireActivity().finish()
                }
            }.start()

        }
        binding.labelDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textDate.text = dateToString(year, month, dayOfMonth)
            }
            dateToDialog(requireActivity(), datePicker).show()
        }
    }
}
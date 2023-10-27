package com.learn.todolistkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learn.todolistkotlin.database.TaskModel
import com.learn.todolistkotlin.databinding.AdapterTaskBinding
import com.learn.todolistkotlin.util.dateToString

class TaskAdapter (
    var items: ArrayList<TaskModel>,
    var listener: AdapterListener
    ): RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    class ViewHolder (val binding: AdapterTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        AdapterTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textTask.text = item.task
        holder.binding.textDate.text = dateToString(item.date)
        holder.itemView.setOnClickListener{
            listener.onDetail(item)
        }
        holder.binding.imageTask.setOnClickListener{
            listener.onUpdate(item)
        }
    }

    override fun getItemCount() = items.size

    fun addList(list: List<TaskModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    interface AdapterListener {
        fun onDetail(taskModel: TaskModel)
        fun onUpdate(taskModel: TaskModel)
    }
}


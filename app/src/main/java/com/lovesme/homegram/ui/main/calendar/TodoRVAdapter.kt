package com.lovesme.homegram.ui.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.databinding.ItemScheduleBinding

class TodoRVAdapter() :
    ListAdapter<Todo, TodoRVAdapter.TodoViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            ItemScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TodoViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Todo) {
            binding.scheduleTv.text = item.contents
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Todo>() {
            override fun areContentsTheSame(oldItem: Todo, newItem: Todo) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Todo, newItem: Todo) =
                oldItem.contents == newItem.contents
        }
    }
}
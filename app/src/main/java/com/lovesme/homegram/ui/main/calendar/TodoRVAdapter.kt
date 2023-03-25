package com.lovesme.homegram.ui.main.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.model.listener.DeleteClickListener
import com.lovesme.homegram.databinding.ItemScheduleBinding

class TodoRVAdapter(private val clickListener: DeleteClickListener) :
    ListAdapter<Pair<String, Todo>, TodoRVAdapter.TodoViewHolder>(diffUtil) {

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
        holder.bind(getItem(position), clickListener)
    }

    class TodoViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, Todo>, clickListener: DeleteClickListener) {
            binding.scheduleTv.text = item.second.contents
            binding.scheduleEditIv.setOnClickListener() {
                clickListener.onClickTodoItem(item.first)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Pair<String, Todo>>() {
            override fun areContentsTheSame(
                oldItem: Pair<String, Todo>,
                newItem: Pair<String, Todo>
            ) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Pair<String, Todo>, newItem: Pair<String, Todo>) =
                oldItem.first == newItem.first
        }
    }
}
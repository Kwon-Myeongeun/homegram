package com.lovesme.homegram.ui.main.daily.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lovesme.homegram.data.model.Answer
import com.lovesme.homegram.databinding.ItemAnswerBinding

class DailyDetailRVAdapter() :
    ListAdapter<Answer, DailyDetailRVAdapter.DailyAnswerViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyAnswerViewHolder {
        return DailyAnswerViewHolder(
            ItemAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyAnswerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DailyAnswerViewHolder(private val binding: ItemAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Answer) {
            binding.answerNameTv.text = item.name
            binding.answerContentsTv.text = item.contents
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Answer>() {
            override fun areContentsTheSame(oldItem: Answer, newItem: Answer) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Answer, newItem: Answer) =
                (oldItem.name == newItem.name) && (oldItem.contents == newItem.contents)
        }
    }
}
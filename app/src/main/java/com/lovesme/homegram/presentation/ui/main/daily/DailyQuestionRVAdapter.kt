package com.lovesme.homegram.presentation.ui.main.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lovesme.homegram.R
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.databinding.ItemQuestionBinding

class DailyQuestionRVAdapter :
    ListAdapter<Question, DailyQuestionRVAdapter.DailyQuestionViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyQuestionViewHolder {
        return DailyQuestionViewHolder(
            ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyQuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DailyQuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Question) {
            binding.questionTv.text = this.itemView.context.getString(R.string.question_display_msg)
                .format(item.seq, item.contents)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Question>() {
            override fun areContentsTheSame(oldItem: Question, newItem: Question) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Question, newItem: Question) =
                oldItem.contents == newItem.contents
        }
    }
}
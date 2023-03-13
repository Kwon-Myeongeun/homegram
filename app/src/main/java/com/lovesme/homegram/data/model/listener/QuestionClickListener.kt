package com.lovesme.homegram.data.model.listener

import com.lovesme.homegram.data.model.Question

interface QuestionClickListener {
    fun onClickItem(question: Question)
}
package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result

interface QuestionRepository {
    suspend fun getQuestion(): Result<List<Question>>
    suspend fun updateAnswer(seq: String, answer: String): Result<Unit>
}
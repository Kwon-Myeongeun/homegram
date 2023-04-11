package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun getQuestion(): Flow<Result<List<Question>>>
    suspend fun updateAnswer(key: String, answer: String): Result<Unit>
}
package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result

interface QuestionRemoteDataSource {
    suspend fun getQuestion(groupId: String): Result<List<Question>>
    suspend fun getGroupId(): Result<String>
    suspend fun updateAnswer(
        groupId: String,
        key: String,
        name: String,
        answer: String
    ): Result<Unit>
}
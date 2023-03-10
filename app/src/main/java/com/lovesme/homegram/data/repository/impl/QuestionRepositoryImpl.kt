package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(private val questionDataSource: QuestionRemoteDataSource) :
    QuestionRepository {
    override suspend fun getQuestion(): Result<List<Question>> {
        val result = questionDataSource.getGroupId()
        return if (result is Result.Success) {
            questionDataSource.getQuestion(result.data)
        } else {
            Result.Error((result as Result.Error).exception)
        }
    }
}
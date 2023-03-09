package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import javax.inject.Inject

class QuestionRemoteDataSourceImpl @Inject constructor() : QuestionRemoteDataSource {
    override suspend fun getQuestion(): Result<List<Question>> {
        TODO()
    }
}
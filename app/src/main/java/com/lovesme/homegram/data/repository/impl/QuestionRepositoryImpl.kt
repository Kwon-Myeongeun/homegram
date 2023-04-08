package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDataSource: QuestionRemoteDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource
) :
    QuestionRepository {
    override suspend fun getQuestion(): Result<List<Question>> {
        val groupId = userInfoLocalDataSource.getGroupId()
        return questionDataSource.getQuestion(groupId)
    }

    override suspend fun updateAnswer(key: String, answer: String): Result<Unit> {
        val groupId = userInfoLocalDataSource.getGroupId()
        val userName = userInfoLocalDataSource.getUserName()
        return questionDataSource.updateAnswer(groupId, key, userName, answer)
    }
}
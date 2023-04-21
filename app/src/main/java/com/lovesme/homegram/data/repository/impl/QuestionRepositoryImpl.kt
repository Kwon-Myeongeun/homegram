package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.datasource.UserInfoRemoteDataSource
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDataSource: QuestionRemoteDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val userInfoDataSource: UserInfoRemoteDataSource
) :
    QuestionRepository {
    override suspend fun getQuestion(): Flow<Result<List<Question>>> {
        return when (val result = userInfoDataSource.getGroupId()) {
            is Result.Success ->
                questionDataSource.getQuestion(result.data)
            is Result.Error -> flow {
                emit(Result.Error(result.exception))
            }
        }
    }

    override suspend fun updateAnswer(key: String, answer: String): Result<Unit> {
        val groupId = userInfoLocalDataSource.getGroupId()
        val userName = userInfoLocalDataSource.getUserName()
        return questionDataSource.updateAnswer(groupId, key, userName, answer)
    }
}
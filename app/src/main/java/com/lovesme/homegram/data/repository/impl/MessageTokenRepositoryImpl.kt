package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.MessageTokenDataSource
import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.MessageTokenRepository
import javax.inject.Inject

class MessageTokenRepositoryImpl @Inject constructor(
    private val questionDataSource: QuestionRemoteDataSource,
    private val messageTokenDataSource: MessageTokenDataSource
) :
    MessageTokenRepository {
    override suspend fun setMessageToken(token: String): Result<Unit> {
        val result = questionDataSource.getGroupId()
        return if (result is Result.Success) {
            return messageTokenDataSource.setMessageToken(token, result.data)
        } else {
            Result.Error((result as Result.Error).exception)
        }
    }
}
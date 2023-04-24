package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.SignInRepository
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val signInDataSource: SignInRemoteDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val questionDataSource: QuestionRemoteDataSource
) :
    SignInRepository {
    override suspend fun saveLogInUserInfo(): Result<Unit> {
        return signInDataSource.saveLogInUserInfo()
    }

    override suspend fun joinToInvitedGroup(groupId: String): Result<Unit> {
        var oldGroupId = userInfoLocalDataSource.getGroupId()
        var userName = userInfoLocalDataSource.getUserName()
        if (oldGroupId.isNullOrEmpty()) {
            val result = questionDataSource.getGroupId()
            if (result is Result.Success) {
                oldGroupId = result.data
            }
        }
        if (userName.isNullOrEmpty()) {
            val result = questionDataSource.getUserName()
            if (result is Result.Success) {
                userName = result.data
            }
        }
        return if (oldGroupId != groupId) {

            signInDataSource.joinToInvitedGroup(oldGroupId, groupId, userName)
        } else {
            Result.Error(Exception())
        }
    }
}
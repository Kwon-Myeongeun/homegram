package com.lovesme.homegram.data.repository.signin

import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.model.Result
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(private val signInDataSource: SignInRemoteDataSource) :
    SignInRepository {
    override suspend fun saveLogInUserInfo(): Result<Unit> {
        return signInDataSource.saveLogInUserInfo()
    }

    override suspend fun joinToInvitedGroup(oldGroupId: String, newGroupId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserInfo(groupId: String, name: String, birth: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}
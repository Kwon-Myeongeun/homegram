package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.SignInRepository
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(private val signInDataSource: SignInRemoteDataSource) :
    SignInRepository {
    override suspend fun saveLogInUserInfo(): Result<Unit> {
        return signInDataSource.saveLogInUserInfo()
    }

    override suspend fun joinToInvitedGroup(groupId: String): Result<Unit> {
        return signInDataSource.joinToInvitedGroup(groupId)
    }

    override suspend fun updateUserInfo(groupId: String, name: String, birth: String): Result<Unit> {
        TODO("Not yet implemented")
    }

}
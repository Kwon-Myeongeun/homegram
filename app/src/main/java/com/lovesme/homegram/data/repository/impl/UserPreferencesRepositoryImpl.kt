package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.UserInfoRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.UserPreferencesRepository

class UserPreferencesRepositoryImpl(private val userInfoDataSource: UserInfoRemoteDataSource) :
    UserPreferencesRepository {

    override suspend fun setMessageToken(token: String): Result<Unit> {
        val result = userInfoDataSource.getGroupId()
        return when (result) {
            is Result.Success ->
                userInfoDataSource.setMessageToken(result.data, token)
            is Result.Error -> Result.Error(result.exception)
        }
    }

    override suspend fun updateUserInfo(
        name: String,
        birth: String
    ): Result<Unit> {
        val result = userInfoDataSource.getGroupId()
        return when (result) {
            is Result.Success ->
                userInfoDataSource.updateUserInfo(result.data, name, birth)
            is Result.Error -> Result.Error(result.exception)
        }
    }
}
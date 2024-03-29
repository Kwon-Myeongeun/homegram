package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.UserInfoRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.UserPreferencesRepository

class UserPreferencesRepositoryImpl(private val userInfoDataSource: UserInfoRemoteDataSource) :
    UserPreferencesRepository {

    override suspend fun setMessageToken(token: String): Result<Unit> {
        return when (val result = userInfoDataSource.getGroupId()) {
            is Result.Success ->
                userInfoDataSource.setMessageToken(result.data, token)
            is Result.Error -> Result.Error(result.exception)
        }
    }

    override suspend fun updateUserInfo(
        name: String,
        birth: String
    ): Result<Unit> {
        return when (val result = userInfoDataSource.getGroupId()) {
            is Result.Success ->
                userInfoDataSource.updateUserInfo(result.data, name, birth)
            is Result.Error -> Result.Error(result.exception)
        }
    }

    override suspend fun getReceiverToken(): Result<List<String>> {
        return when (val result = userInfoDataSource.getGroupId()) {
            is Result.Success ->
                userInfoDataSource.getReceiverToken(result.data)
            is Result.Error -> Result.Error(result.exception)
        }
    }

    override suspend fun deleteUserInfo(): Result<Unit> {
        return when (val result = userInfoDataSource.getGroupId()) {
            is Result.Success ->
                userInfoDataSource.deleteUserInfo(result.data)
            is Result.Error -> Result.Error(result.exception)
        }
    }

    override suspend fun existUser(): Result<Boolean> {
        return userInfoDataSource.existUser()
    }

    override suspend fun existGroupId(groupId: String): Result<Boolean> {
        return userInfoDataSource.existGroupId(groupId)
    }

    override suspend fun existUserName(): Result<Boolean> {
        return userInfoDataSource.existUserName()
    }

    override suspend fun existMember(): Result<Boolean> {
        return when (val result = userInfoDataSource.getGroupId()) {
            is Result.Success ->
                userInfoDataSource.existMember(result.data)
            is Result.Error -> Result.Error(result.exception)
        }
    }
}
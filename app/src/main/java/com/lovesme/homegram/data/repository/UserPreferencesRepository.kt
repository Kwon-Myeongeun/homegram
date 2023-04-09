package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Result

interface UserPreferencesRepository {
    suspend fun setMessageToken(token: String): Result<Unit>
    suspend fun updateUserInfo(name: String, birth: String): Result<Unit>
    suspend fun getReceiverToken(): Result<List<String>>
    suspend fun deleteUserInfo(): Result<Unit>
    suspend fun existUser(): Result<Boolean>
    suspend fun existGroupId(groupId: String): Result<Boolean>
}
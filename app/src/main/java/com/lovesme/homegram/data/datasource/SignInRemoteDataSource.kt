package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Result

interface SignInRemoteDataSource {
    suspend fun saveLogInUserInfo(): Result<Unit>
    suspend fun joinToInvitedGroup(oldGroupId: String, newGroupId: String): Result<Unit>
    suspend fun updateUserInfo(groupId: String, name: String, birth: String): Result<Unit>
}
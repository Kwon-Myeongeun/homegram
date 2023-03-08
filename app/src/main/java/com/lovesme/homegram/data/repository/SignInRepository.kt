package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Result

interface SignInRepository {
    suspend fun saveLogInUserInfo(): Result<Unit>
    suspend fun joinToInvitedGroup(groupId: String): Result<Unit>
    suspend fun updateUserInfo(groupId: String, name: String, birth: String): Result<Unit>
}
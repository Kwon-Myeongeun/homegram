package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Result

interface UserInfoRemoteDataSource {
    suspend fun updateUserInfo(groupId: String, name: String, birth: String): Result<Unit>
    suspend fun setMessageToken(groupId: String, token: String): Result<Unit>
    suspend fun getGroupId(): Result<String>
    suspend fun getReceiverToken(groupId: String): Result<List<String>>
    suspend fun deleteUserInfo(groupId: String): Result<Unit>
    suspend fun existUser(): Result<Boolean>
    suspend fun existGroupId(groupId: String): Result<Boolean>
}
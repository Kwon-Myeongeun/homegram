package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.*
import kotlinx.coroutines.flow.Flow

interface SyncDataSource {
    suspend fun loadUserInfo(): Result<User?>
    suspend fun loadGroup(groupId: String): Result<List<Group?>>
    suspend fun isConnect(): Flow<Result<Boolean>>
}
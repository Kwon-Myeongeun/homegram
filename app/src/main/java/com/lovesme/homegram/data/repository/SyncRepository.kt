package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Result
import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    suspend fun syncStart(): Result<Unit>
    suspend fun isConnect(): Flow<Result<Boolean>>
}
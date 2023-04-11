package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.model.Result
import kotlinx.coroutines.flow.Flow

interface TodoRemoteDataSource {
    suspend fun getSchedule(groupId: String, date: String): Flow<Result<Map<String, Todo>>>
    suspend fun addSchedule(groupId: String, date: String, todo: Todo): Result<Unit>
    suspend fun deleteSchedule(groupId: String, date: String, key: String): Result<Unit>
}
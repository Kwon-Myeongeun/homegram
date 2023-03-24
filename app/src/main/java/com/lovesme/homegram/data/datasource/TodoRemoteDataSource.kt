package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.model.Result

interface TodoRemoteDataSource {
    suspend fun getSchedule(groupId: String, date: String): Result<List<Todo>>
    suspend fun addSchedule(groupId: String, date: String, todo: Todo): Result<Unit>
}
package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.model.Result

interface TodoRepository {
    suspend fun getSchedule(date: String): Result<Map<String, Todo>>
    suspend fun addSchedule(date: String, contents: String): Result<Unit>
    suspend fun deleteSchedule(date: String, key: String): Result<Unit>
}
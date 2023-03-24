package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.model.Result

interface TodoRepository {
    suspend fun getSchedule(date: String): Result<List<Todo>>
    suspend fun addSchedule(date: String, contents: String): Result<Unit>
}
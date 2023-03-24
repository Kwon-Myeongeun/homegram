package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.TodoRemoteDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.data.repository.TodoRepository
import javax.inject.Inject
import com.lovesme.homegram.data.model.Result

class TodoRepositoryImpl @Inject constructor(
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val todoDataSource: TodoRemoteDataSource
) :
    TodoRepository {
    override suspend fun getSchedule(date: String): Result<List<Todo>> {
        val groupId = userInfoLocalDataSource.getGroupId()
        return todoDataSource.getSchedule(groupId, date)
    }
}
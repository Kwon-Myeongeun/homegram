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
    override suspend fun getSchedule(date: String): Result<Map<String, Todo>> {
        val groupId = userInfoLocalDataSource.getGroupId()
        return todoDataSource.getSchedule(groupId, date)
    }

    override suspend fun addSchedule(date: String, contents: String): Result<Unit> {
        val groupId = userInfoLocalDataSource.getGroupId()
        val userName = userInfoLocalDataSource.getUserName()
        val todo = Todo(userName, contents)
        return todoDataSource.addSchedule(groupId, date, todo)
    }

    override suspend fun deleteSchedule(date: String, key: String): Result<Unit> {
        val groupId = userInfoLocalDataSource.getGroupId()
        return todoDataSource.deleteSchedule(groupId, date, key)
    }
}
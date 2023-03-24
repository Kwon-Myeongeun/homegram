package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.TodoRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.util.Constants
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TodoRemoteDataSourceImpl @Inject constructor() : TodoRemoteDataSource {

    override suspend fun getSchedule(groupId: String, date: String): Result<List<Todo>> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                Constants.database.reference
                    .child(Constants.DIRECTORY_TODO)
                    .child(groupId)
                    .child(date)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val todoList = mutableListOf<Todo?>()
                        for (child in snapshot.children) {
                            todoList.add(child.getValue(Todo::class.java))
                        }
                        continuation.resume(Result.Success(todoList.filterNotNull()))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }
}
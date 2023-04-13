package com.lovesme.homegram.data.datasource.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lovesme.homegram.data.datasource.TodoRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.Todo
import com.lovesme.homegram.util.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TodoRemoteDataSourceImpl @Inject constructor() : TodoRemoteDataSource {

    override suspend fun getSchedule(
        groupId: String,
        date: String
    ): Flow<Result<List<Todo>>> =
        callbackFlow {
            val reference = Constants.database.reference
                .child(Constants.DIRECTORY_TODO)
                .child(groupId)
                .child(date)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val todoList = mutableListOf<Todo>()
                    for (child in snapshot.children) {
                        val key = child.key.toString()
                        val todo = child.getValue(Todo::class.java)
                        todo?.let {
                            todo.date = key
                            todoList.add(todo)
                        }
                    }
                    trySend(Result.Success(todoList))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    trySend(Result.Error(Error(databaseError.message)))
                }
            }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

    override suspend fun getAllSchedule(groupId: String): Flow<Result<List<Todo>>> =
        callbackFlow {
            val reference = Constants.database.reference
                .child(Constants.DIRECTORY_TODO)
                .child(groupId)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val todoList = mutableListOf<Todo>()
                    for (childSnapshot in snapshot.children) {
                        for (child in childSnapshot.children) {
                            val key = childSnapshot.key.toString()
                            val todo = child.getValue(Todo::class.java)
                            todo?.let {
                                todo.date = key
                                todoList.add(todo)
                            }
                        }
                    }

                    trySend(Result.Success(todoList))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    trySend(Result.Error(Error(databaseError.message)))
                }
            }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

    override suspend fun addSchedule(groupId: String, date: String, todo: Todo): Result<Unit> =
        suspendCoroutine { continuation ->
            Constants.database.reference
                .child(Constants.DIRECTORY_TODO)
                .child(groupId)
                .child(date)
                .push()
                .setValue(todo)
                .addOnSuccessListener {
                    continuation.resume(Result.Success(Unit))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.Error(exception))
                }
        }

    override suspend fun deleteSchedule(groupId: String, date: String, key: String): Result<Unit> =
        suspendCoroutine { continuation ->
            Constants.database.reference
                .child(Constants.DIRECTORY_TODO)
                .child(groupId)
                .child(date)
                .child(key)
                .removeValue()
                .addOnSuccessListener {
                    continuation.resume(Result.Success(Unit))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.Error(exception))
                }
        }
}
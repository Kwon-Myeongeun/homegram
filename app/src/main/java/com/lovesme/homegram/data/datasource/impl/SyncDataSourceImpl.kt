package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.SyncDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.util.Constants
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SyncDataSourceImpl @Inject constructor() : SyncDataSource {
    override suspend fun loadUserInfo(): Result<User?> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                Constants.database.reference
                    .child(Constants.DIRECTORY_USER)
                    .child(id)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(snapshot.getValue(User::class.java)))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }
}
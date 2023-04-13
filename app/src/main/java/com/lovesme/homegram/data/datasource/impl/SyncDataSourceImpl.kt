package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.SyncDataSource
import com.lovesme.homegram.data.model.Group
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

    override suspend fun loadGroup(groupId: String): Result<List<Group?>> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val groupList = mutableListOf<Group?>()
                Constants.database.reference
                    .child(Constants.DIRECTORY_GROUP)
                    .child(groupId)
                    .child(Constants.DIRECTORY_MEMBER)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        for (child in snapshot.children) {
                            if (child.key != id) {
                                val item = child.getValue(Group::class.java)
                                groupList.add(item)
                            }
                        }
                        continuation.resume(Result.Success(groupList))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }
}
package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.UserInfoRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.util.Constants
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserInfoRemoteDataSourceImpl @Inject constructor() :
    UserInfoRemoteDataSource {

    override suspend fun getGroupId() =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val reference = Constants.database.reference
                    .child(Constants.DIRECTORY_USER)
                    .child(id)
                    .child(Constants.DIRECTORY_GROUP_ID)

                reference.get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(snapshot.value.toString()))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    override suspend fun updateUserInfo(
        groupId: String,
        name: String,
        birth: String
    ): Result<Unit> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val childUpdates = hashMapOf<String, Any?>(
                    "/${Constants.DIRECTORY_USER}/$id/${Constants.DIRECTORY_NAME}/" to name,
                    "/${Constants.DIRECTORY_USER}/$id/${Constants.DIRECTORY_BIRTH}/" to birth,
                    "/${Constants.DIRECTORY_GROUP}/$groupId/${Constants.DIRECTORY_MEMBER}/${id}/${Constants.DIRECTORY_NAME}/" to name,
                )

                Constants.database.reference.updateChildren(childUpdates)
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    override suspend fun setMessageToken(groupId: String, token: String): Result<Unit> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val childUpdates = hashMapOf<String, Any?>(
                    "/${Constants.DIRECTORY_USER}/$id/${Constants.DIRECTORY_TOKEN}/" to token,
                    "/${Constants.DIRECTORY_GROUP}/$groupId/${Constants.DIRECTORY_MEMBER}/$id/${Constants.DIRECTORY_TOKEN}" to token
                )
                Constants.database.reference.updateChildren(childUpdates)
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    override suspend fun getReceiverToken(groupId: String): Result<List<String>> =
        suspendCoroutine { continuation ->
            val reference = Constants.database.reference
                .child(Constants.DIRECTORY_GROUP)
                .child(groupId)
                .child(Constants.DIRECTORY_MEMBER)

            val tokenList = mutableListOf<String>()

            reference.get()
                .addOnSuccessListener { snapshot ->
                    for (child in snapshot.children) {
                        for (tokenItem in child.children) {
                            if (tokenItem.key == Constants.DIRECTORY_TOKEN) {
                                tokenList.add(tokenItem.value.toString())
                            }
                        }
                    }
                    continuation.resume(Result.Success(tokenList))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.Error(exception))
                }
        }

    override suspend fun deleteUserInfo(groupId: String): Result<Unit> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val childUpdates = hashMapOf<String, Any?>(
                    "/${Constants.DIRECTORY_USER}/$id" to null,
                    "/${Constants.DIRECTORY_GROUP}/$groupId/${Constants.DIRECTORY_MEMBER}/$id" to null,
                    "/${Constants.DIRECTORY_LOCATION}/$groupId/$id" to null,
                )
                Constants.database.reference.updateChildren(childUpdates)
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }
}
package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.util.Constants
import com.lovesme.homegram.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignInRemoteDataSourceImpl @Inject constructor() : SignInRemoteDataSource {

    override suspend fun saveLogInUserInfo() =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val groupId = UUID.generate()
                val childUpdates = hashMapOf<String, Any?>(
                    "/$Constants.DIRECTORY_USER/$id/" to User(Constants.email, groupId),
                    "/$Constants.DIRECTORY_GROUP/$groupId/$Constants.DIRECTORY_MEMBER/$id/" to ""
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

    override suspend fun joinToInvitedGroup(oldGroupId: String, newGroupId: String) =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val childUpdates = hashMapOf<String, Any?>(
                    "/${Constants.DIRECTORY_USER}/$id/${Constants.DIRECTORY_GROUP_ID}/" to newGroupId,
                    "/${Constants.DIRECTORY_GROUP}/$newGroupId/${Constants.DIRECTORY_MEMBER}/$id" to "name",
                    "/${Constants.DIRECTORY_GROUP}/$oldGroupId/${Constants.DIRECTORY_MEMBER}/$id/" to null,
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
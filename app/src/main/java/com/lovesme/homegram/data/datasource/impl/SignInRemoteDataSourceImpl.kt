package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.model.Result
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
                val key =
                    Constants.database.reference.child(Constants.DIRECTORY_DAILY).child(groupId)
                        .push().key
                val childUpdates = hashMapOf<String, Any?>(
                    "/${Constants.DIRECTORY_USER}/$id/${Constants.DIRECTORY_EMAIL}/" to Constants.email,
                    "/${Constants.DIRECTORY_USER}/$id/${Constants.DIRECTORY_GROUP_ID}/" to groupId,
                    "/${Constants.DIRECTORY_DAILY}/$groupId/${key}/" to hashMapOf(
                        Constants.DIRECTORY_QUESTION_CONTENTS to firstQuestion,
                        Constants.DIRECTORY_QUESTION_IS_DONE to false,
                        Constants.DIRECTORY_QUESTION_NUM to 1,
                    ),
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
                    "/${Constants.DIRECTORY_GROUP}/$newGroupId/${Constants.DIRECTORY_MEMBER}/$id/${Constants.DIRECTORY_NAME}" to "name",
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

    companion object {
        const val firstQuestion = "내가 기억하는 가장 어릴적 기억은?"
    }
}
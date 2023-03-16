package com.lovesme.homegram.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.util.Constants

class UserPreferencesRepository {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun getGroupId() =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val reference = Constants.database.reference
                    .child(Constants.DIRECTORY_USER)
                    .child(id)
                    .child(Constants.DIRECTORY_GROUP_ID)

                reference.get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(snapshot.value))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    suspend fun updateGroupId(oldGroupId: String, newGroupId: String) =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val childUpdates = hashMapOf<String, Any?>(
                    "/$Constants.DIRECTORY_USER/$id/$Constants.DIRECTORY_GROUP_ID/" to newGroupId,
                    "/$Constants.DIRECTORY_GROUP/$newGroupId/$Constants.DIRECTORY_MEMBER/$id" to "name",
                    "/$Constants.DIRECTORY_GROUP/$oldGroupId/$Constants.DIRECTORY_MEMBER/$id/" to null,
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

    suspend fun existsUserName() =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val userReference = Constants.database.reference
                    .child(Constants.DIRECTORY_USER)
                    .child(id)
                    .child(Constants.DIRECTORY_NAME)

                userReference.get()
                    .addOnSuccessListener {
                        continuation.resume(Result.Success(it.exists()))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    suspend fun updateUser(groupId: String, name: String, birth: String) =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val childUpdates = hashMapOf<String, Any?>(
                    "/$Constants.DIRECTORY_USER/$id/$Constants.DIRECTORY_NAME/" to name,
                    "/$Constants.DIRECTORY_USER/$id/$Constants.DIRECTORY_BIRTH/" to birth,
                    "/$Constants.DIRECTORY_GROUP/$groupId/$Constants.DIRECTORY_MEMBER/$id/" to name,
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
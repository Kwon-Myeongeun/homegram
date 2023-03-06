package com.lovesme.homegram.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.data.model.User
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import com.lovesme.homegram.data.model.Result

const val DIRECTORY_USER = "user"
const val DIRECTORY_GROUP_ID = "groupId"

class UserPreferencesRepository {
    private val database =
        Firebase.database(BuildConfig.DATABASE_URL)
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun addUser(user: User) =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val reference = database.reference
                    .child(DIRECTORY_USER)
                    .child(id)

                reference.setValue(user)
                    .addOnSuccessListener {
                        continuation.resume(Result.Success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    suspend fun getGroupId() =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val reference = database.reference
                    .child(DIRECTORY_USER)
                    .child(id)
                    .child(DIRECTORY_GROUP_ID)

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
                    "/$DIRECTORY_USER/$id/$DIRECTORY_GROUP_ID/" to newGroupId,
                    "/$DIRECTORY_GROUP/$newGroupId/$DIRECTORY_MEMBER/$id" to "name",
                    "/$DIRECTORY_GROUP/$oldGroupId/$DIRECTORY_MEMBER/$id/" to null,
                )


                database.reference.updateChildren(childUpdates)
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }

            }
        }
}
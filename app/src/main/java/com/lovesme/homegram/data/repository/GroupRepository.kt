package com.lovesme.homegram.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.util.UUID

const val DIRECTORY_GROUP = "group"
const val DIRECTORY_MEMBER = "member"

class GroupRepository {
    private val database =
        Firebase.database(BuildConfig.DATABASE_URL)
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val groupId = UUID.generate()

    suspend fun createGroup() =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val userReference = database.reference
                    .child(DIRECTORY_USER)
                    .child(userId)

                val groupReference = database.reference
                    .child(DIRECTORY_GROUP)
                    .child(groupId)
                    .child(DIRECTORY_MEMBER)
                    .child(id)

                userReference.get()
                    .addOnSuccessListener {
                        if (!it.exists()) {
                            groupReference.setValue("name")
                                .addOnSuccessListener {
                                    continuation.resume(Result.Success(groupId))
                                }
                                .addOnFailureListener { exception ->
                                    continuation.resume(Result.Error(exception))
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }


            }
        }
}
package com.lovesme.homegram.data.datasource.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.data.datasource.MessageTokenDataSource
import com.lovesme.homegram.data.model.Result
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MessageTokenDataSourceImpl @Inject constructor() : MessageTokenDataSource {
    override suspend fun setMessageToken(token: String, groupId: String): Result<Unit> =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val childUpdates = hashMapOf<String, Any?>(
                    "/${DIRECTORY_GROUP}/$groupId/${DIRECTORY_MEMBER}/$id" to token
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


    companion object {
        val database = Firebase.database(BuildConfig.DATABASE_URL)
        private val userId = FirebaseAuth.getInstance().currentUser?.uid

        const val DIRECTORY_GROUP = "group"
        const val DIRECTORY_MEMBER = "member"
    }
}

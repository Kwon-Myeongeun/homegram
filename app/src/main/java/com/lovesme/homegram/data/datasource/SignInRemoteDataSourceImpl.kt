package com.lovesme.homegram.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignInRemoteDataSourceImpl @Inject constructor() : SignInRemoteDataSource {

    override suspend fun saveLogInUserInfo() =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val groupId = UUID.generate()
                val childUpdates = hashMapOf<String, Any?>(
                    "/$DIRECTORY_USER/$id/" to User(email,groupId),
                    "/$DIRECTORY_GROUP/$groupId/$DIRECTORY_MEMBER/$id/" to ""
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
        private val email = FirebaseAuth.getInstance().currentUser?.email ?: ""

        const val DIRECTORY_GROUP = "group"
        const val DIRECTORY_MEMBER = "member"
        const val DIRECTORY_USER = "user"
        const val DIRECTORY_GROUP_ID = "groupId"
        const val DIRECTORY_NAME = "name"
        const val DIRECTORY_BIRTH = "birth"
    }

    override suspend fun joinToInvitedGroup(oldGroupId: String, newGroupId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserInfo(groupId: String, name: String, birth: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}
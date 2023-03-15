package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.SyncDataSource
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.util.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SyncDataSourceImpl @Inject constructor() : SyncDataSource {
    override fun loadUserInfo(userId: String): Flow<User?> = callbackFlow {
        Constants.database.reference
            .child(Constants.DIRECTORY_USER)
            .child(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                trySend(
                    snapshot.getValue(User::class.java)
                )
            }
            .addOnFailureListener {
                trySend(null)
            }
        awaitClose()
    }

}
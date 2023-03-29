package com.lovesme.homegram.data.datasource.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lovesme.homegram.data.datasource.LocationRemoteDataSource
import com.lovesme.homegram.data.model.*
import com.lovesme.homegram.util.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationRemoteDataSourceImpl @Inject constructor() : LocationRemoteDataSource {
    override suspend fun getLocation(groupId: String): Flow<Result<List<Location>>> = callbackFlow {
        Constants.userId?.let { id ->
            val reference = Constants.database.reference
                .child(Constants.DIRECTORY_LOCATION)
                .child(groupId)

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val locationList = mutableListOf<Location?>()
                    for (child in dataSnapshot.children) {
                        val item = child.getValue(Location::class.java)
                        if (child.key == id) {
                            item?.title = Constants.PERSONAL_MAP_TITLE
                        }
                        locationList.add(item)
                    }
                    trySend(Result.Success(locationList.filterNotNull()))
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    trySend(Result.Error(Error(databaseError.message)))
                }
            }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }
    }

    override suspend fun setLocation(groupId: String, location: Location): Result<Unit> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                Constants.database.reference
                    .child(Constants.DIRECTORY_LOCATION)
                    .child(groupId)
                    .child(id)
                    .setValue(location)
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }
}

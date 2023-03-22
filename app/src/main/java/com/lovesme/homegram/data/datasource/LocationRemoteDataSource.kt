package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.Result
import kotlinx.coroutines.flow.Flow

interface LocationRemoteDataSource {
    suspend fun getLocation(groupId: String): Flow<Result<List<Location>>>
    suspend fun setLocation(groupId: String, location: Location): Result<Unit>
}
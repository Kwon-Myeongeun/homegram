package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.Result
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocation(): Flow<Result<List<Location>>>
    suspend fun setLocation(location: Location): Result<Unit>
}
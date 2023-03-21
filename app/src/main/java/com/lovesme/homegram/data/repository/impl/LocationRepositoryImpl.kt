package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.LocationRemoteDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val locationDataSource: LocationRemoteDataSource
) :
    LocationRepository {
    override suspend fun getLocation(): Flow<Result<List<Location>>> {
        val groupId = userInfoLocalDataSource.getGroupId()
        return locationDataSource.getLocation(groupId)
    }
}
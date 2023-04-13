package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Group
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.User

interface UserInfoLocalDataSource {
    suspend fun syncAllUserInfo(userInfo: User)
    suspend fun syncAllGroup(group: List<Group>)
    suspend fun syncAllLocation(location: List<Location>)
    suspend fun getGroupId(): String
    suspend fun getUserName(): String
    suspend fun getUserToken(): String
    suspend fun updateToken(userToken: String)
}
package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Group
import com.lovesme.homegram.data.model.User

interface UserInfoLocalDataSource {
    suspend fun syncAllUserInfo(userInfo: User)
    suspend fun syncAllGroup(group: List<Group>)
    suspend fun getGroupId(): String
    suspend fun getUserName(): String
    suspend fun getUserToken(): String
    suspend fun updateToken(userToken: String)
}
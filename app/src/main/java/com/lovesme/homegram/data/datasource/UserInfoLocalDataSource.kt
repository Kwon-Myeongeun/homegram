package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.User

interface UserInfoLocalDataSource {
    suspend fun syncAllUserInfo(userInfo: User)
    suspend fun getGroupId(): String
}
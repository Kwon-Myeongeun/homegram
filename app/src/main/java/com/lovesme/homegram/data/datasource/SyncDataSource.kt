package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.data.model.Result

interface SyncDataSource {
    suspend fun loadUserInfo(userId: String): Result<User?>
    suspend fun loadDailyInfo(groupId: String): Result<List<Question>>
}
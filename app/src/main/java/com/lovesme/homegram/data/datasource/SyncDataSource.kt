package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Group
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.data.model.Result

interface SyncDataSource {
    suspend fun loadUserInfo(): Result<User?>
    suspend fun loadGroup(groupId: String): Result<List<Group?>>
}
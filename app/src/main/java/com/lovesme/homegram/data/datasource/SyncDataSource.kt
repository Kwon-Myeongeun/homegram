package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.User
import kotlinx.coroutines.flow.Flow

interface SyncDataSource {
    fun loadUserInfo(userId: String): Flow<User?>
}
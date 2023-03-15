package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.SyncDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.repository.SyncRepository
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource
) :
    SyncRepository {
    override suspend fun startSync(userId: String) {
        syncDataSource.loadUserInfo(userId).collect { UserInfo ->
            if (UserInfo == null) return@collect
            userInfoLocalDataSource.syncAllUserInfo(UserInfo)
        }
    }
}
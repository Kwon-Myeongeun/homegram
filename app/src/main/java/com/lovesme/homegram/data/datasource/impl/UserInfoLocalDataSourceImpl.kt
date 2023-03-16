package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.dao.UserInfoDao
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.User
import javax.inject.Inject

class UserInfoLocalDataSourceImpl @Inject constructor(private val userInfoDao: UserInfoDao) :
    UserInfoLocalDataSource {
    override suspend fun syncAllUserInfo(userInfo: User) {
        userInfoDao.syncAll(userInfo.mapToUserInfoEntity())
    }

    override suspend fun getGroupId(): String {
        return userInfoDao.getGroupId()
    }
}
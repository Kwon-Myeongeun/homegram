package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.dao.GroupDao
import com.lovesme.homegram.data.dao.LocationDao
import com.lovesme.homegram.data.dao.UserInfoDao
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Group
import com.lovesme.homegram.data.model.Location
import com.lovesme.homegram.data.model.User
import javax.inject.Inject

class UserInfoLocalDataSourceImpl @Inject constructor(
    private val userInfoDao: UserInfoDao,
    private val groupDao: GroupDao,
    private val locationDao: LocationDao
) :
    UserInfoLocalDataSource {
    override suspend fun syncAllUserInfo(userInfo: User) {
        userInfoDao.deleteAll()
        userInfoDao.syncAll(userInfo.mapToUserInfoEntity())
    }

    override suspend fun syncAllGroup(group: List<Group>) {
        groupDao.deleteAll()
        groupDao.syncAll(group.map { it.mapToUserInfoEntity() })
    }

    override suspend fun syncAllLocation(location: List<Location>) {
        locationDao.deleteAll()
        locationDao.syncAll(location.map { it.mapToLocationEntity() })
    }

    override suspend fun getGroupId(): String {
        return userInfoDao.getGroupId()
    }

    override suspend fun getUserName(): String {
        return userInfoDao.getUserName()
    }

    override suspend fun getUserToken(): String {
        return userInfoDao.getUserToken()
    }

    override suspend fun updateToken(userToken: String) {
        return userInfoDao.updateToken(userToken)
    }


}
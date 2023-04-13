package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.*
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.SyncRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val dailyLocalDataSource: DailyLocalDataSource,
    private val questionDataSource: QuestionRemoteDataSource,
    private val userInfoDataSource: UserInfoRemoteDataSource,
    private val locationRemoteDataSource: LocationRemoteDataSource,
) :
    SyncRepository {
    override suspend fun syncStart(): Result<Unit> {
        val userInfo = syncDataSource.loadUserInfo()
        if (userInfo is Result.Success) {
            userInfo.data?.let { userInfoLocalDataSource.syncAllUserInfo(it) }
        } else {
            return Result.Error((userInfo as Result.Error).exception)
        }

        val groupId = userInfoDataSource.getGroupId()
        if (groupId is Result.Success) {
            val group = syncDataSource.loadGroup(groupId.data)
            if (group is Result.Success) {
                userInfoLocalDataSource.syncAllGroup(group.data.filterNotNull())
            } else {
                return Result.Error((group as Result.Error).exception)
            }
            val location = locationRemoteDataSource.getLocation(groupId.data).first()
            if (location is Result.Success) {
                userInfoLocalDataSource.syncAllLocation(location.data)
            } else {
                return Result.Error((location as Result.Error).exception)
            }
            val daily = questionDataSource.getQuestion(groupId.data).first()
            if (daily is Result.Success) {
                dailyLocalDataSource.syncAllQuestion(
                    daily.data.map { item ->
                        item.mapToQuestionEntity()
                    }
                )
                dailyLocalDataSource.syncAllAnswer(
                    daily.data.map { item ->
                        item.mapToAnswerEntityList()
                    }.flatten()
                )
            } else {
                return Result.Error((daily as Result.Error).exception)
            }
        } else {
            return Result.Error((groupId as Result.Error).exception)
        }
        return Result.Success(Unit)
    }
}
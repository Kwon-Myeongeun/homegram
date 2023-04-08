package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.*
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.SyncRepository
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val dailyLocalDataSource: DailyLocalDataSource,
    private val questionDataSource: QuestionRemoteDataSource,
    private val userInfoDataSource: UserInfoRemoteDataSource,
) :
    SyncRepository {
    override suspend fun syncStart(userId: String) {
        val userInfo = syncDataSource.loadUserInfo(userId)
        if (userInfo is Result.Success) {
            userInfo.data?.let { userInfoLocalDataSource.syncAllUserInfo(it) }
        } else {
            Result.Error((userInfo as Result.Error).exception)
        }

        val groupId = userInfoDataSource.getGroupId()
        if (groupId is Result.Success) {

            val daily = questionDataSource.getQuestion(groupId.data)
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
                Result.Error((daily as Result.Error).exception)
            }
        } else {
            Result.Error((userInfo as Result.Error).exception)
        }
    }
}
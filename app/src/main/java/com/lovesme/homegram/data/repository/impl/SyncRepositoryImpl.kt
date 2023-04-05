package com.lovesme.homegram.data.repository.impl

import com.lovesme.homegram.data.datasource.DailyLocalDataSource
import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.datasource.SyncDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.repository.SyncRepository
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncDataSource,
    private val userInfoLocalDataSource: UserInfoLocalDataSource,
    private val dailyLocalDataSource: DailyLocalDataSource,
    private val questionDataSource: QuestionRemoteDataSource,
) :
    SyncRepository {
    override suspend fun syncStart(userId: String) {
        val userInfo = syncDataSource.loadUserInfo(userId)
        if (userInfo is Result.Success) {
            userInfo.data?.let { userInfoLocalDataSource.syncAllUserInfo(it) }
        } else {
            Result.Error((userInfo as Result.Error).exception)
        }

        val groupId = userInfoLocalDataSource.getGroupId()
        val daily = questionDataSource.getQuestion(groupId)
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
    }
}
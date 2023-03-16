package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.dao.AnswerDao
import com.lovesme.homegram.data.dao.QuestionDao
import com.lovesme.homegram.data.datasource.DailyLocalDataSource
import com.lovesme.homegram.data.model.AnswerEntity
import com.lovesme.homegram.data.model.QuestionEntity
import javax.inject.Inject

class DailyLocalDataSourceImpl @Inject constructor(
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao
) :
    DailyLocalDataSource {
    override suspend fun syncAllQuestion(questions: List<QuestionEntity>) {
        questionDao.syncAll(questions)
    }

    override suspend fun syncAllAnswer(answers: List<AnswerEntity>) {
        answerDao.syncAll(answers)
    }
}
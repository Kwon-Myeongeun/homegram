package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.AnswerEntity
import com.lovesme.homegram.data.model.QuestionEntity

interface DailyLocalDataSource {
    suspend fun syncAllQuestion(questions: List<QuestionEntity>)
    suspend fun syncAllAnswer(answers: List<AnswerEntity>)
}
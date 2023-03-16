package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.SyncDataSource
import com.lovesme.homegram.data.model.Answer
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.data.model.User
import com.lovesme.homegram.util.Constants
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SyncDataSourceImpl @Inject constructor() : SyncDataSource {
    override suspend fun loadUserInfo(userId: String): Result<User?> =
        suspendCoroutine { continuation ->
            Constants.database.reference
                .child(Constants.DIRECTORY_USER)
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(Result.Success(snapshot.getValue(User::class.java)))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.Error(exception))
                }
        }

    override suspend fun loadDailyInfo(groupId: String): Result<List<Question>> =
        suspendCoroutine { continuation ->
            val questionList = mutableListOf<Question>()
            var contents = ""
            val answerList = mutableListOf<Answer>()

            Constants.database.reference
                .child(Constants.DIRECTORY_DAILY)
                .child(groupId)
                .get()
                .addOnSuccessListener { snapshot ->
                    for (child in snapshot.children) {
                        val index = child.key ?: ""
                        for (chatItem in child.children) {
                            if (chatItem.key == Constants.DIRECTORY_QUESTION_CONTENTS) {
                                contents = chatItem.value.toString()
                            } else if (chatItem.key == Constants.DIRECTORY_QUESTION_MEMBER) {
                                for (answerItem in chatItem.children) {
                                    answerList.add(
                                        Answer(
                                            index,
                                            answerItem.key.toString(),
                                            answerItem.value.toString()
                                        )
                                    )
                                }
                            }
                        }
                        questionList.add(
                            Question(
                                index,
                                contents,
                                answerList.toMutableList()
                            )
                        )
                        answerList.clear()
                    }
                    continuation.resume(Result.Success(questionList))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.Error(exception))
                }
        }
}
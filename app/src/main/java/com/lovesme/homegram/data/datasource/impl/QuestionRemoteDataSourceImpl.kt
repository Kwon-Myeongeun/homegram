package com.lovesme.homegram.data.datasource.impl

import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.model.Answer
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import com.lovesme.homegram.util.Constants
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuestionRemoteDataSourceImpl @Inject constructor() : QuestionRemoteDataSource {

    override suspend fun getQuestion(groupId: String): Result<List<Question>> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val reference = Constants.database.reference
                    .child(Constants.DIRECTORY_DAILY)
                    .child(groupId)

                val questionList = mutableListOf<Question>()
                var contents = ""
                var index = ""
                var isDone = false
                val answerList = mutableListOf<Answer>()

                reference.get()
                    .addOnSuccessListener { snapshot ->
                        for (child in snapshot.children) {
                            for (chatItem in child.children) {
                                when (chatItem.key) {
                                    Constants.DIRECTORY_QUESTION_CONTENTS -> {
                                        contents = chatItem.value.toString()
                                    }
                                    Constants.DIRECTORY_QUESTION_NUM -> {
                                        index = chatItem.value.toString()
                                    }
                                    Constants.DIRECTORY_QUESTION_IS_DONE -> {
                                        isDone = chatItem.value.toString().toBoolean()
                                    }
                                    Constants.DIRECTORY_QUESTION_MEMBER -> {
                                        for (answerItem in chatItem.children) {
                                            answerList.add(
                                                Answer(
                                                    answerItem.key.toString(),
                                                    answerItem.value.toString()
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            questionList.add(
                                Question(
                                    child.key.toString(),
                                    index,
                                    contents,
                                    isDone,
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

    override suspend fun getGroupId(): Result<String> =
        suspendCoroutine { continuation ->
            Constants.userId?.let { id ->
                val reference = Constants.database.reference
                    .child(Constants.DIRECTORY_USER)
                    .child(id)
                    .child(Constants.DIRECTORY_GROUP_ID)

                reference.get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(snapshot.value.toString()))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    override suspend fun updateAnswer(
        groupId: String,
        key: String,
        name: String,
        answer: String
    ): Result<Unit> =
        suspendCoroutine { continuation ->
            val childUpdates = hashMapOf<String, Any?>(
                "/${Constants.DIRECTORY_DAILY}/$groupId/$key/${Constants.DIRECTORY_QUESTION_MEMBER}/$name" to answer,
                "/${Constants.DIRECTORY_DAILY}/$groupId/$key/${Constants.DIRECTORY_QUESTION_IS_DONE}" to true
            )

            Constants.database.reference.updateChildren(childUpdates)
                .addOnSuccessListener { snapshot ->
                    continuation.resume(Result.Success(Unit))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.Error(exception))
                }
        }

}

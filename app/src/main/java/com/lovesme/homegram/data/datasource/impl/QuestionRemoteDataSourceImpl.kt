package com.lovesme.homegram.data.datasource.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig
import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.model.Question
import com.lovesme.homegram.data.model.Result
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QuestionRemoteDataSourceImpl @Inject constructor() : QuestionRemoteDataSource {

    override suspend fun getQuestion(groupId: String): Result<List<Question>> =
        suspendCoroutine { continuation ->
            userId?.let { id ->
                val reference = database.reference
                    .child(DIRECTORY_DAILY)
                    .child(groupId)

                val questionList = mutableListOf<Question>()
                var contents: String? = null

                reference.get()
                    .addOnSuccessListener { snapshot ->
                        for (child in snapshot.children) {
                            val index = child.key
                            for (chatItem in child.children) {
                                if (chatItem.key == DIRECTORY_QUESTION_CONTENTS) {
                                    contents = chatItem.value.toString()
                                }
                            }
                            questionList.add(
                                Question(
                                    index,
                                    contents
                                )
                            )
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
            userId?.let { id ->
                val reference = database.reference
                    .child(com.lovesme.homegram.data.repository.DIRECTORY_USER)
                    .child(id)
                    .child(com.lovesme.homegram.data.repository.DIRECTORY_GROUP_ID)

                reference.get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(Result.Success(snapshot.value.toString()))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    companion object {
        val database = Firebase.database(BuildConfig.DATABASE_URL)
        private val userId = FirebaseAuth.getInstance().currentUser?.uid

        const val DIRECTORY_DAILY = "daily"
        const val DIRECTORY_QUESTION_CONTENTS = "contents"
    }
}

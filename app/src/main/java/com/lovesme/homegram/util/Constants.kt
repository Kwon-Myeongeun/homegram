package com.lovesme.homegram.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig

object Constants {
    const val PARCELABLE_QUESTION = "question"
    const val PARCELABLE_ANSWER_TEXT = "answer"
    const val PARCELABLE_CONTENT = "content"
    const val PARCELABLE_SERVICE_STOP = "stop"
    const val PARCELABLE_DATE = "date"
    const val PARCELABLE_NO = "no"


    val database = Firebase.database(BuildConfig.DATABASE_URL)
    val userId get() = FirebaseAuth.getInstance().currentUser?.uid

    val email get() = FirebaseAuth.getInstance().currentUser?.email ?: ""

    const val DIRECTORY_GROUP = "group"
    const val DIRECTORY_MEMBER = "member"
    const val DIRECTORY_USER = "user"
    const val DIRECTORY_GROUP_ID = "groupId"
    const val DIRECTORY_NAME = "name"
    const val DIRECTORY_BIRTH = "birth"
    const val DIRECTORY_DAILY = "daily"
    const val DIRECTORY_QUESTION_CONTENTS = "contents"
    const val DIRECTORY_QUESTION_MEMBER = "member"
    const val DIRECTORY_LOCATION = "location"
    const val DIRECTORY_TODO = "todo"
    const val DIRECTORY_QUESTION_NUM = "no"
    const val DIRECTORY_QUESTION_IS_DONE = "isDone"
    const val DIRECTORY_TOKEN = "token"
    const val DIRECTORY_EMAIL = "email"
    const val DIRECTORY_CHECK_CONNECT = ".info/connected"
}
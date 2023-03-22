package com.lovesme.homegram.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lovesme.homegram.BuildConfig

object Constants {
    const val PARCELABLE_QUESTION = "question"
    const val PARCELABLE_INVITE_ID = "inviteId"
    const val PARCELABLE_LOCATION = "location"

    val database = Firebase.database(BuildConfig.DATABASE_URL)
    val userId get() = FirebaseAuth.getInstance().currentUser?.uid

    val email get() = FirebaseAuth.getInstance().currentUser?.email ?: ""

    const val DIRECTORY_GROUP = "group"
    const val DIRECTORY_MEMBER = "member"
    const val DIRECTORY_USER = "user"
    const val DIRECTORY_GROUP_ID = "groupId"
    const val DIRECTORY_NAME = "name"
    const val DIRECTORY_BIRTH = "birth"
    const val DIRECTORY_QUESTION = "question"
    const val DIRECTORY_DAILY = "daily"
    const val DIRECTORY_QUESTION_CONTENTS = "contents"
    const val DIRECTORY_QUESTION_MEMBER = "member"
    const val DIRECTORY_LOCATION = "location"

    const val KEY_USERINFO = "userInfo"
}
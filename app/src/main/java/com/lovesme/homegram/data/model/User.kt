package com.lovesme.homegram.data.model

data class User(
    val email: String,
    val groupId: String,
    val name: String? = null,
    val birthday: String? = null,
)

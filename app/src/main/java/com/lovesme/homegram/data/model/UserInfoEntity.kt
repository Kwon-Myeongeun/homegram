package com.lovesme.homegram.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userInfo")
data class UserInfoEntity(
    @PrimaryKey val email: String,
    val groupId: String,
    val name: String = "",
    val birth: String = "",
    val token: String = "",
) {
}

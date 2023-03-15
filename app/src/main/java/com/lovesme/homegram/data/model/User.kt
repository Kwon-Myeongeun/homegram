package com.lovesme.homegram.data.model

data class User(
    val email: String = "",
    val groupId: String = "",
    val name: String = "",
    val birth: String = "",
    val token: String = "",
) {
    fun mapToUserInfoEntity(): UserInfoEntity =
        UserInfoEntity(
            email,
            groupId,
            name,
            birth,
            token,
        )
}

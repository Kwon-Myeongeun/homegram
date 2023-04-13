package com.lovesme.homegram.data.model

data class Group(
    val name: String = "",
) {
    fun mapToUserInfoEntity(): GroupEntity =
        GroupEntity(
            name,
        )
}

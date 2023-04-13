package com.lovesme.homegram.data.model

data class Todo(
    var key: String? = "",
    var date: String? = "",
    val name: String = "",
    val contents: String = ""
) {
    fun mapToTodoEntity(): TodoEntity =
        TodoEntity(
            key ?: "",
            date ?: "",
            name,
            contents
        )
}

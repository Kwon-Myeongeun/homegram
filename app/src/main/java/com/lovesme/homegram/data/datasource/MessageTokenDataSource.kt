package com.lovesme.homegram.data.datasource

import com.lovesme.homegram.data.model.Result

interface MessageTokenDataSource {
    suspend fun setMessageToken(token: String, groupId: String): Result<Unit>
}
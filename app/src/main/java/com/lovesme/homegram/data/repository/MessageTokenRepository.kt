package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Result

interface MessageTokenRepository {
    suspend fun setMessageToken(token: String): Result<Unit>
}
package com.lovesme.homegram.data.repository

interface SyncRepository {
    suspend fun startSync(userId: String)
}
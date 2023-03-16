package com.lovesme.homegram.data.repository

interface SyncRepository {
    suspend fun syncStart(userId: String)
}
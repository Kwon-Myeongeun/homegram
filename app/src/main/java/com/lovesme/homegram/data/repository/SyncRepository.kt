package com.lovesme.homegram.data.repository

import com.lovesme.homegram.data.model.Result

interface SyncRepository {
    suspend fun syncStart(): Result<Unit>
}
package com.lovesme.homegram.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lovesme.homegram.data.model.QuestionEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncAll(questionEntity: List<QuestionEntity?>)

    @Query("DELETE FROM question")
    suspend fun deleteAll()

    @Query("SELECT * FROM question")
    suspend fun selectAll(): List<QuestionEntity>
}
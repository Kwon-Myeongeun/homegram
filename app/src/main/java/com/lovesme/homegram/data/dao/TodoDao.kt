package com.lovesme.homegram.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lovesme.homegram.data.model.TodoEntity

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncAll(locationEntity: List<TodoEntity>)

    @Query("DELETE FROM todo")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo")
    suspend fun selectAll(): List<TodoEntity>
}
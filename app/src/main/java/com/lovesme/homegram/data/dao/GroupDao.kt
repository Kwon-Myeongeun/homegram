package com.lovesme.homegram.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lovesme.homegram.data.model.GroupEntity

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncAll(groupEntity: List<GroupEntity>)

    @Query("DELETE FROM groupInfo")
    suspend fun deleteAll()
}
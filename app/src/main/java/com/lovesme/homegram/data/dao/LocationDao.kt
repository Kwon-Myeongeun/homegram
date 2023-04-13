package com.lovesme.homegram.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lovesme.homegram.data.model.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncAll(locationEntity: List<LocationEntity>)

    @Query("DELETE FROM location")
    suspend fun deleteAll()

    @Query("SELECT * FROM location")
    suspend fun selectAll(): List<LocationEntity>
}
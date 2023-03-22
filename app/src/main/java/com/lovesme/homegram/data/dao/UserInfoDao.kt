package com.lovesme.homegram.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lovesme.homegram.data.model.UserInfoEntity

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncAll(userInfoEntity: UserInfoEntity)

    @Query("SELECT groupId FROM userInfo LIMIT 1")
    suspend fun getGroupId(): String

    @Query("SELECT name FROM userInfo LIMIT 1")
    suspend fun getUserName(): String

}
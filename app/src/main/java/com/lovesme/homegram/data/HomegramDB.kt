package com.lovesme.homegram.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lovesme.homegram.data.dao.UserInfoDao
import com.lovesme.homegram.data.model.UserInfoEntity

@Database(
    entities = [UserInfoEntity::class],
    version = 1,
    exportSchema = false
)

abstract class HomegramDB : RoomDatabase() {
    companion object {
        fun create(context: Context): HomegramDB {
            val databaseBuilder =
                Room.databaseBuilder(context, HomegramDB::class.java, "homegram.db")
            return databaseBuilder.fallbackToDestructiveMigration().build()
        }
    }

    abstract fun userInfoDao(): UserInfoDao
}
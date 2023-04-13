package com.lovesme.homegram.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lovesme.homegram.data.dao.*
import com.lovesme.homegram.data.model.*

@Database(
    entities = [UserInfoEntity::class, QuestionEntity::class, AnswerEntity::class, GroupEntity::class, LocationEntity::class, TodoEntity::class],
    version = 7,
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
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    abstract fun groupDao(): GroupDao
    abstract fun locationDao(): LocationDao
    abstract fun todoDao(): TodoDao
}
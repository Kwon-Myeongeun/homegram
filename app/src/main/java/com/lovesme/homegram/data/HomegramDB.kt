package com.lovesme.homegram.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lovesme.homegram.data.dao.AnswerDao
import com.lovesme.homegram.data.dao.QuestionDao
import com.lovesme.homegram.data.dao.UserInfoDao
import com.lovesme.homegram.data.model.AnswerEntity
import com.lovesme.homegram.data.model.QuestionEntity
import com.lovesme.homegram.data.model.UserInfoEntity

@Database(
    entities = [UserInfoEntity::class, QuestionEntity::class, AnswerEntity::class],
    version = 2,
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
}
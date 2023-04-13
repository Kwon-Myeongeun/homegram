package com.lovesme.homegram.di

import android.content.Context
import com.lovesme.homegram.data.HomegramDB
import com.lovesme.homegram.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncDataModule {
    @Provides
    @Singleton
    fun provideDB(
        @ApplicationContext context: Context
    ): HomegramDB = HomegramDB.create(context)

    @Provides
    @Singleton
    fun provideUserInfoDao(
        homegramDB: HomegramDB
    ): UserInfoDao = homegramDB.userInfoDao()

    @Provides
    @Singleton
    fun provideQuestionDao(
        homegramDB: HomegramDB
    ): QuestionDao = homegramDB.questionDao()

    @Provides
    @Singleton
    fun provideAnswerDao(
        homegramDB: HomegramDB
    ): AnswerDao = homegramDB.answerDao()

    @Provides
    @Singleton
    fun provideGroupDao(
        homegramDB: HomegramDB
    ): GroupDao = homegramDB.groupDao()

    @Provides
    @Singleton
    fun provideLocationDao(
        homegramDB: HomegramDB
    ): LocationDao = homegramDB.locationDao()

    @Provides
    @Singleton
    fun provideTodoDao(
        homegramDB: HomegramDB
    ): TodoDao = homegramDB.todoDao()
}
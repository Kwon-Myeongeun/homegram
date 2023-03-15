package com.lovesme.homegram.di

import android.content.Context
import com.lovesme.homegram.data.HomegramDB
import com.lovesme.homegram.data.dao.UserInfoDao
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
    fun provideCalendarDao(
        homegramDB: HomegramDB
    ): UserInfoDao = homegramDB.userInfoDao()
}
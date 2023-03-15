package com.lovesme.homegram.di

import com.lovesme.homegram.data.dao.UserInfoDao
import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.datasource.SyncDataSource
import com.lovesme.homegram.data.datasource.UserInfoLocalDataSource
import com.lovesme.homegram.data.datasource.impl.QuestionRemoteDataSourceImpl
import com.lovesme.homegram.data.datasource.impl.SignInRemoteDataSourceImpl
import com.lovesme.homegram.data.datasource.impl.SyncDataSourceImpl
import com.lovesme.homegram.data.datasource.impl.UserInfoLocalDataSourceImpl
import com.lovesme.homegram.data.repository.QuestionRepository
import com.lovesme.homegram.data.repository.SignInRepository
import com.lovesme.homegram.data.repository.SyncRepository
import com.lovesme.homegram.data.repository.impl.QuestionRepositoryImpl
import com.lovesme.homegram.data.repository.impl.SignInRepositoryImpl
import com.lovesme.homegram.data.repository.impl.SyncRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSignInRepository(signInDataSource: SignInRemoteDataSource): SignInRepository {
        return SignInRepositoryImpl(signInDataSource)
    }

    @Provides
    @Singleton
    fun provideSignInRemoteDataSource(): SignInRemoteDataSource {
        return SignInRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(questionDataSource: QuestionRemoteDataSource): QuestionRepository {
        return QuestionRepositoryImpl(questionDataSource)
    }

    @Provides
    @Singleton
    fun provideQuestionRemoteDataSource(): QuestionRemoteDataSource {
        return QuestionRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideSyncRepository(syncDataSource: SyncDataSource, userInfoLocalDataSource: UserInfoLocalDataSource): SyncRepository {
        return SyncRepositoryImpl(syncDataSource, userInfoLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideSyncDataSource(): SyncDataSource {
        return SyncDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideUserInfoLocalDataSource(userInfoDao: UserInfoDao): UserInfoLocalDataSource {
        return UserInfoLocalDataSourceImpl(userInfoDao)
    }
}
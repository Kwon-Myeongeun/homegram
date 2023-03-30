package com.lovesme.homegram.di

import com.lovesme.homegram.data.dao.AnswerDao
import com.lovesme.homegram.data.dao.QuestionDao
import com.lovesme.homegram.data.dao.UserInfoDao
import com.lovesme.homegram.data.datasource.*
import com.lovesme.homegram.data.datasource.impl.*
import com.lovesme.homegram.data.repository.*
import com.lovesme.homegram.data.repository.impl.*
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
    fun provideSignInRepository(
        signInDataSource: SignInRemoteDataSource,
        userInfoLocalDataSource: UserInfoLocalDataSource,
        questionDataSource: QuestionRemoteDataSource
    ): SignInRepository {
        return SignInRepositoryImpl(signInDataSource, userInfoLocalDataSource, questionDataSource)
    }

    @Provides
    @Singleton
    fun provideSignInRemoteDataSource(): SignInRemoteDataSource {
        return SignInRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(
        questionDataSource: QuestionRemoteDataSource,
        userInfoLocalDataSource: UserInfoLocalDataSource,
    ): QuestionRepository {
        return QuestionRepositoryImpl(questionDataSource, userInfoLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideQuestionRemoteDataSource(): QuestionRemoteDataSource {
        return QuestionRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideSyncRepository(
        syncDataSource: SyncDataSource,
        userInfoLocalDataSource: UserInfoLocalDataSource,
        dailyLocalDataSource: DailyLocalDataSource,
        questionDataSource: QuestionRemoteDataSource
    ): SyncRepository {
        return SyncRepositoryImpl(
            syncDataSource,
            userInfoLocalDataSource,
            dailyLocalDataSource,
            questionDataSource
        )
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

    @Provides
    @Singleton
    fun provideDailyLocalDataSource(
        questionDao: QuestionDao,
        answerDao: AnswerDao
    ): DailyLocalDataSource {
        return DailyLocalDataSourceImpl(questionDao, answerDao)
    }

    @Provides
    @Singleton
    fun provideLocationRemoteDataSource(): LocationRemoteDataSource {
        return LocationRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        userInfoLocalDataSource: UserInfoLocalDataSource,
        locationDataSource: LocationRemoteDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(userInfoLocalDataSource, locationDataSource)
    }

    @Provides
    @Singleton
    fun provideTodoRemoteDataSource(): TodoRemoteDataSource {
        return TodoRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(
        userInfoLocalDataSource: UserInfoLocalDataSource,
        todoDataSource: TodoRemoteDataSource
    ): TodoRepository {
        return TodoRepositoryImpl(userInfoLocalDataSource, todoDataSource)
    }

    @Provides
    @Singleton
    fun provideUserInfoRemoteDataSource(): UserInfoRemoteDataSource {
        return UserInfoRemoteDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideUserInfoRepository(
        userInfoDataSource: UserInfoRemoteDataSource
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(userInfoDataSource)
    }
}
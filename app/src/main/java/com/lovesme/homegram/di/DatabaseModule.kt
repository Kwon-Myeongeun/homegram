package com.lovesme.homegram.di

import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.datasource.impl.QuestionRemoteDataSourceImpl
import com.lovesme.homegram.data.datasource.impl.SignInRemoteDataSourceImpl
import com.lovesme.homegram.data.repository.QuestionRepository
import com.lovesme.homegram.data.repository.SignInRepository
import com.lovesme.homegram.data.repository.impl.QuestionRepositoryImpl
import com.lovesme.homegram.data.repository.impl.SignInRepositoryImpl
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
}
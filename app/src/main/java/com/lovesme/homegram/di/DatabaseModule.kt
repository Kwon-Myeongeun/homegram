package com.lovesme.homegram.di

import com.lovesme.homegram.data.datasource.MessageTokenDataSource
import com.lovesme.homegram.data.datasource.QuestionRemoteDataSource
import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.datasource.impl.MessageTokenDataSourceImpl
import com.lovesme.homegram.data.datasource.impl.QuestionRemoteDataSourceImpl
import com.lovesme.homegram.data.datasource.impl.SignInRemoteDataSourceImpl
import com.lovesme.homegram.data.repository.MessageTokenRepository
import com.lovesme.homegram.data.repository.QuestionRepository
import com.lovesme.homegram.data.repository.SignInRepository
import com.lovesme.homegram.data.repository.impl.MessageTokenRepositoryImpl
import com.lovesme.homegram.data.repository.impl.QuestionRepositoryImpl
import com.lovesme.homegram.data.repository.impl.SignInRepositoryImpl
import com.lovesme.homegram.data.usecase.SetMessageTokenUseCase
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
    fun provideSignInRepository(signInDataSource: SignInRemoteDataSource, setMessageTokenUseCase: SetMessageTokenUseCase): SignInRepository {
        return SignInRepositoryImpl(signInDataSource, setMessageTokenUseCase)
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
    fun provideMessageTokenRepository(questionDataSource: QuestionRemoteDataSource, messageTokenDataSource: MessageTokenDataSource): MessageTokenRepository {
        return MessageTokenRepositoryImpl(questionDataSource, messageTokenDataSource)
    }

    @Provides
    @Singleton
    fun provideMessageTokenRemoteDataSource(): MessageTokenDataSource {
        return MessageTokenDataSourceImpl()
    }
}
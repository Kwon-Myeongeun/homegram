package com.lovesme.homegram.di

import com.lovesme.homegram.data.datasource.SignInRemoteDataSource
import com.lovesme.homegram.data.datasource.SignInRemoteDataSourceImpl
import com.lovesme.homegram.data.repository.signin.SignInRepository
import com.lovesme.homegram.data.repository.signin.SignInRepositoryImpl
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
    fun providesignInDataSource(signInDataSource: SignInRemoteDataSourceImpl): SignInRepository {
        return SignInRepositoryImpl(signInDataSource)
    }

    @Provides
    @Singleton
    fun provideSignInRemoteDataSource(): SignInRemoteDataSource {
        return SignInRemoteDataSourceImpl()
    }
}
package com.example.mymemory.di

import android.content.Context
import com.example.mymemory.repository.GameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideGameRepository(
        @ApplicationContext context: Context,
    ): GameRepository {
        return GameRepository(context = context)
    }
}
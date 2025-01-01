package com.aydemir.glancestandingsapp.di

import android.content.Context
import com.aydemir.glancestandingsapp.local.StandingsDao
import com.aydemir.glancestandingsapp.local.StandingsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class AppCoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): StandingsDatabase =
        StandingsDatabase.getInstance(context = context)

    @Provides
    fun provideStandingsDao(database: StandingsDatabase): StandingsDao = database.standingsDao()

    @Provides
    @Singleton
    @AppCoroutineScope
    fun provideApplicationCoroutineScope(): CoroutineScope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher(),
    )
}
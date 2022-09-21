package com.devdaniel.marvelapp.di

import android.content.Context
import androidx.room.Room
import com.devdaniel.marvelapp.data.local.CharacterDatabase
import com.devdaniel.marvelapp.data.local.CharactersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun dataBaseProvides(@ApplicationContext context: Context): CharacterDatabase =
        Room.databaseBuilder(context, CharacterDatabase::class.java, "characters.db").build()

    @Singleton
    @Provides
    fun charactersDaoProvides(
        characterDatabase: CharacterDatabase
    ): CharactersDao = characterDatabase.charactersDao()
}

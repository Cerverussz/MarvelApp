package com.devdaniel.marvelapp.di

import com.devdaniel.marvelapp.data.local.CharacterDatabase
import com.devdaniel.marvelapp.data.local.CharactersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseTestModule {

    private val characterDatabase = mockk<CharacterDatabase>(relaxed = true)
    private val charactersDao = mockk<CharactersDao>(relaxed = true)

    @Singleton
    @Provides
    fun dataBaseProvides(): CharacterDatabase =
        characterDatabase

    @Singleton
    @Provides
    fun charactersDaoProvides(): CharactersDao = charactersDao
}

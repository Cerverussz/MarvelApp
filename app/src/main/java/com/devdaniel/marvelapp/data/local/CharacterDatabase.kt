package com.devdaniel.marvelapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devdaniel.marvelapp.data.local.entities.CharacterEntity

@Database(
    version = 1,
    entities = [CharacterEntity::class],
    exportSchema = false
)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}

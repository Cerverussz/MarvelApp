package com.devdaniel.marvelapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devdaniel.marvelapp.data.local.entities.CharacterEntity

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharacterEntity)

    @Query("SELECT * FROM character")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT count(character_id) FROM character WHERE character_id=:characterId")
    fun characterExist(characterId: Int): Boolean
}

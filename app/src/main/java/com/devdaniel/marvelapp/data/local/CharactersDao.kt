package com.devdaniel.marvelapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devdaniel.marvelapp.data.local.entities.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharacterEntity)

    @Query("SELECT * FROM character")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT count(character_id) FROM character WHERE character_id=:characterId")
    fun characterExist(characterId: Int): Boolean

    @Query("SELECT * FROM character WHERE character_id=:id")
    fun getCharacterBy(id: Int): Flow<CharacterEntity>
}

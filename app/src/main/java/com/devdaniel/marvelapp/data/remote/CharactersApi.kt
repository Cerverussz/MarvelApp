package com.devdaniel.marvelapp.data.remote

import com.devdaniel.marvelapp.data.model.CharacterDTO
import retrofit2.http.GET

interface CharactersApi {

    @GET(SERVICE_CHARACTERS)
    suspend fun getCharacters(): CharacterDTO
}

private const val SERVICE_CHARACTERS = "/v1/public/characters"

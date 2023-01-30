package com.devdaniel.marvelapp.data.remote

import com.devdaniel.marvelapp.data.model.CharacterDTO
import com.devdaniel.marvelapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApi {

    @GET(SERVICE_CHARACTERS)
    suspend fun getCharacters(
        @Query("name") name: String,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.timestamp,
        @Query("hash") hash: String = Constants.hash(),
        @Query("limit") limit: String = Constants.limit
    ): Response<CharacterDTO>
}

private const val SERVICE_CHARACTERS = "/v1/public/characters"

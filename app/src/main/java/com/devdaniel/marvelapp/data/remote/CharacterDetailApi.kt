package com.devdaniel.marvelapp.data.remote

import com.devdaniel.marvelapp.data.model.CharacterDTO
import com.devdaniel.marvelapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterDetailApi {

    @GET(SERVICE_CHARACTER_DETAIL)
    suspend fun getCharacterDetail(
        @Path("characterId") id: Int,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.timestamp,
        @Query("hash") hash: String = Constants.hash()
    ): Response<CharacterDTO>
}

private const val SERVICE_CHARACTER_DETAIL = "v1/public/characters/{characterId}"

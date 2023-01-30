package com.devdaniel.marvelapp.data.remote

import com.devdaniel.marvelapp.data.model.ComicCharacterDTO
import com.devdaniel.marvelapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterDetailApi {

    @GET(SERVICE_COMICS_CHARACTER_DETAIL)
    suspend fun getComicsCharacterDetail(
        @Path("characterId") id: Int,
        @Query("format") format: String = "comic",
        @Query("limit") limit: Int = 10,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.timestamp,
        @Query("hash") hash: String = Constants.hash()
    ): Response<ComicCharacterDTO>
}

private const val SERVICE_COMICS_CHARACTER_DETAIL = "/v1/public/characters/{characterId}/comics"

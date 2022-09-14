package com.devdaniel.marvelapp.data.model

import com.squareup.moshi.Json

data class CharacterDTO(
    @Json(name = "code")
    val code: Int,
    @Json(name = "status")
    val status: String,
    @Json(name = "copyright")
    val copyright: String,
    @Json(name = "data")
    val data: Data
)

data class Data(
    @Json(name = "results")
    val infoCharacter: List<InfoCharacter>
)

data class InfoCharacter(
    val id: Int,
    val description: String,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val thumbnail: Thumbnail,
    val comics: Comics,
    val stories: Stories,
    val urls: List<Urls>
) {

    data class Series(
        val available: Int
    )

    data class Thumbnail(
        val path: String,
        val extension: String
    )

    data class Comics(
        val available: Int,
        val items: List<Items>
    ) {
        data class Items(
            val name: String
        )
    }

    data class Stories(
        val available: Int
    )

    data class Urls(
        val type: String,
        val url: String
    )
}

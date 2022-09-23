package com.devdaniel.marvelapp.ui.characters

import androidx.annotation.StringRes
import com.devdaniel.marvelapp.domain.model.Character

data class CharactersState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    @StringRes val errorMessage: Int? = null
)

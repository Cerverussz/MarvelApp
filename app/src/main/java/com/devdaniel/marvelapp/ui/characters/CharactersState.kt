package com.devdaniel.marvelapp.ui.characters

import androidx.annotation.StringRes
import com.devdaniel.marvelapp.domain.model.Character

sealed class CharactersState {
    object Loading : CharactersState()
    data class CharactersSuccess(val characters: List<Character>) : CharactersState()
    data class CharactersError(@StringRes val errorMessage: Int? = null) : CharactersState()
}

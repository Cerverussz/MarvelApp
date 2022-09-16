package com.devdaniel.marvelapp.ui.detail

import androidx.annotation.StringRes
import com.devdaniel.marvelapp.domain.model.CharacterDetail

data class CharacterDetailState(
    val isLoading: Boolean = false,
    val data: CharacterDetail? = null,
    @StringRes val errorMessage: Int? = null
)

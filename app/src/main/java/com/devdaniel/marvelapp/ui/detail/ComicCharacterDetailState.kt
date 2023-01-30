package com.devdaniel.marvelapp.ui.detail

import androidx.annotation.StringRes
import com.devdaniel.marvelapp.domain.model.ComicCharacterDetail

data class ComicCharacterDetailState(
    val isLoading: Boolean = false,
    val data: ComicCharacterDetail? = null,
    @StringRes val errorMessage: Int? = null,
    val isError: Boolean = false
)

package com.devdaniel.marvelapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.usecase.CharacterDetailUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterDetailUC: CharacterDetailUC
) : ViewModel() {

    private val _comicCharacterDetailState: MutableStateFlow<ComicCharacterDetailState> =
        MutableStateFlow(
            ComicCharacterDetailState()
        )

    val comicCharacterDetailState: StateFlow<ComicCharacterDetailState>
        get() = _comicCharacterDetailState.asStateFlow()

    fun getComicsCharacterDetail(characterId: Int) {
        _comicCharacterDetailState.update { currentState ->
            currentState.copy(
                isLoading = true,
                isError = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            characterDetailUC.getComicsCharacterDetail(characterId).fold(
                onSuccess = { comic ->
                    _comicCharacterDetailState.update { currentState ->
                        currentState.copy(
                            data = comic,
                            isLoading = false,
                            isError = false
                        )
                    }
                },
                onError = { _, _ ->
                    _comicCharacterDetailState.update { currentState ->
                        currentState.copy(isError = true, isLoading = false)
                    }
                },
                onException = {
                    _comicCharacterDetailState.update { currentState ->
                        currentState.copy(isError = true, isLoading = false)
                    }
                }
            )
        }
    }
}

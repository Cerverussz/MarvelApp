package com.devdaniel.marvelapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.usecase.CharacterDetailUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterDetailUC: CharacterDetailUC
) : ViewModel() {

    private val _characterDetailState: MutableStateFlow<CharacterDetailState> = MutableStateFlow(
        CharacterDetailState()
    )
    val characterDetailState: StateFlow<CharacterDetailState>
        get() = _characterDetailState.asStateFlow()

    fun getCharacterDetail(characterId: Int) {
        _characterDetailState.update { currentState ->
            currentState.copy(
                isLoading = true,
                isError = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            characterDetailUC.getCharacterDetail(characterId).fold(
                onSuccess = { characterDetail ->
                    _characterDetailState.update { currentState ->
                        currentState.copy(
                            data = characterDetail,
                            isLoading = false,
                            isError = false
                        )
                    }
                },
                onError = { _, _ ->
                    _characterDetailState.update { currentState ->
                        currentState.copy(isError = true, isLoading = false)
                    }
                },
                onException = {
                    _characterDetailState.update { currentState ->
                        currentState.copy(isError = true, isLoading = false)
                    }
                }
            )
        }
    }

    fun getLocalCharacterDetail(characterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            characterDetailUC.getLocalCharacterDetail(characterId = characterId)
                .onStart {
                    _characterDetailState.update { currentState ->
                        currentState.copy(isLoading = true, isError = false)
                    }
                }
                .catch {
                    _characterDetailState.update { currentState ->
                        currentState.copy(
                            isError = true,
                            isLoading = false,
                            errorMessage = R.string.not_found_error
                        )
                    }
                }.collect { character ->
                    _characterDetailState.update { currentState ->
                        currentState.copy(
                            data = character,
                            isLoading = false,
                            isError = false
                        )
                    }
                }
        }
    }
}

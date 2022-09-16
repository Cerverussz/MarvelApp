package com.devdaniel.marvelapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.domain.common.Error
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.common.toError
import com.devdaniel.marvelapp.domain.common.validateHttpCodeErrorCode
import com.devdaniel.marvelapp.domain.usecase.GetCharacterDetailUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailUC: GetCharacterDetailUC,
    private val _characterDetailState: MutableStateFlow<CharacterDetailState>
) : ViewModel() {

    val characterDetailState: StateFlow<CharacterDetailState>
        get() = _characterDetailState.asStateFlow()

    fun getCharacterDetail(characterId: Int) {
        _characterDetailState.update { currentState -> currentState.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            getCharacterDetailUC(characterId).fold(
                onSuccess = { characterDetail ->
                    _characterDetailState.update { currentState ->
                        currentState.copy(data = characterDetail)
                    }
                },
                onError = { errorCode, _ ->
                    val error = handleError(errorCode.validateHttpCodeErrorCode())
                    _characterDetailState.update { currentState ->
                        currentState.copy(errorMessage = error.errorMessage, isLoading = false)
                    }
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _characterDetailState.update { currentState ->
                        currentState.copy(errorMessage = error.errorMessage)
                    }
                }
            )
        }
    }

    private fun handleError(error: Error): CharacterDetailState {
        return when (error) {
            Error.Connectivity -> CharacterDetailState(
                errorMessage = R.string.connectivity_error
            )
            is Error.HttpException -> CharacterDetailState(
                errorMessage = error.messageResId
            )
            is Error.Unknown -> CharacterDetailState(
                errorMessage = R.string.connectivity_error
            )
        }
    }
}

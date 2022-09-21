package com.devdaniel.marvelapp.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.domain.common.Error
import com.devdaniel.marvelapp.domain.common.Error.Connectivity
import com.devdaniel.marvelapp.domain.common.Error.HttpException
import com.devdaniel.marvelapp.domain.common.Error.Unknown
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.common.toError
import com.devdaniel.marvelapp.domain.common.validateHttpCodeErrorCode
import com.devdaniel.marvelapp.domain.usecase.CharactersUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUC: CharactersUC,
    private val _charactersState: MutableStateFlow<CharactersState>
) : ViewModel() {

    val charactersState: StateFlow<CharactersState>
        get() = _charactersState.asStateFlow()

    val localCharacters = charactersUC.getLocalCharacters()

    fun getCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            _charactersState.value = CharactersState.Loading
            charactersUC.getRemoteCharacters().fold(
                onSuccess = { characters ->
                    _charactersState.update { CharactersState.CharactersSuccess(characters) }
                },
                onError = { errorCode: Int, _: String? ->
                    val error = handleError(errorCode.validateHttpCodeErrorCode())
                    _charactersState.update {
                        CharactersState.CharactersError(
                            errorMessage = error.errorMessage
                        )
                    }
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _charactersState.update {
                        CharactersState.CharactersError(
                            errorMessage = error.errorMessage
                        )
                    }
                }
            )
        }
    }

    private fun handleError(error: Error): CharactersState.CharactersError {
        return when (error) {
            Connectivity -> CharactersState.CharactersError(
                errorMessage = R.string.connectivity_error
            )
            is HttpException -> CharactersState.CharactersError(
                errorMessage = error.messageResId
            )
            is Unknown -> CharactersState.CharactersError(
                errorMessage = R.string.connectivity_error
            )
        }
    }
}

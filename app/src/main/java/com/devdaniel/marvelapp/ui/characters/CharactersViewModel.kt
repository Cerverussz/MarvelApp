package com.devdaniel.marvelapp.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.di.IoDispatcher
import com.devdaniel.marvelapp.domain.common.Error
import com.devdaniel.marvelapp.domain.common.Error.Connectivity
import com.devdaniel.marvelapp.domain.common.Error.HttpException
import com.devdaniel.marvelapp.domain.common.Error.Unknown
import com.devdaniel.marvelapp.domain.common.fold
import com.devdaniel.marvelapp.domain.common.toError
import com.devdaniel.marvelapp.domain.common.validateHttpCodeErrorCode
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.domain.usecase.CharactersUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUC: CharactersUC,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _charactersState: MutableStateFlow<CharactersState> =
        MutableStateFlow(CharactersState.Loading)

    val charactersState: StateFlow<CharactersState>
        get() = _charactersState.asStateFlow()

    val localCharacters: Flow<List<Character>> by lazy { charactersUC.getLocalCharacters() }

    fun getCharacters() {
        viewModelScope.launch(dispatcher) {
            _charactersState.update { CharactersState.Loading }
            charactersUC.getRemoteCharacters().fold(
                onSuccess = { characters ->
                    _charactersState.update {
                        CharactersState.CharactersSuccess(characters = characters)
                    }
                },
                onError = { errorCode: Int, _: String? ->
                    val error = handleError(errorCode.validateHttpCodeErrorCode())
                    _charactersState.update {
                        CharactersState.CharactersError(errorMessage = error)
                    }
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _charactersState.update {
                        CharactersState.CharactersError(errorMessage = error)
                    }
                }
            )
        }
    }

    private fun handleError(error: Error): Int {
        return when (error) {
            Connectivity -> R.string.connectivity_error
            is HttpException -> error.messageResId
            is Unknown -> R.string.connectivity_error
        }
    }
}

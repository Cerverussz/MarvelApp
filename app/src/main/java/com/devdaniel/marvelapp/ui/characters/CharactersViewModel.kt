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
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUC: CharactersUC,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _charactersState: MutableStateFlow<CharactersState> =
        MutableStateFlow(CharactersState.Loading)

    private val mainCharacters = listOf("Iron Man", "Thor", "Hulk", "Captain America")
    private var currentCharacters = arrayListOf<Character>()

    val charactersState: StateFlow<CharactersState>
        get() = _charactersState.asStateFlow()

    val localCharacters: Flow<List<Character>> by lazy { charactersUC.getLocalCharacters() }

    fun getCharactersByName() {
        mainCharacters.forEach { name ->
            viewModelScope.launch {
                if (currentCharacters.count() <= 3) {
                    getCharacters(name) { characters ->
                        if (currentCharacters.contains(characters.first()).not()) {
                            currentCharacters.add(characters.first())
                            if (name == mainCharacters.last()) {
                                _charactersState.update {
                                    CharactersState.CharactersSuccess(characters = currentCharacters)
                                }
                            }
                        }
                    }
                } else {
                    _charactersState.update {
                        CharactersState.CharactersSuccess(characters = currentCharacters)
                    }
                }
            }
        }
    }

    fun getCharacters(name: String, action: (List<Character>) -> Unit = {}) {
        viewModelScope.launch(dispatcher) {
            _charactersState.update { CharactersState.Loading }
            charactersUC.getRemoteCharacters(name).fold(
                onSuccess = { characters ->
                    action.invoke(characters)
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

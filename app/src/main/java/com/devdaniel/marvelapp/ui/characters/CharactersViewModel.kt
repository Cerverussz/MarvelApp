package com.devdaniel.marvelapp.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdaniel.marvelapp.domain.usecase.GetCharactersUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUC: GetCharactersUC
) : ViewModel() {

    fun getCharacters() = viewModelScope.launch(Dispatchers.IO) {
        getCharactersUC().onStart {
            println("daniel--- loading")
        }.catch {
            println("daniel- $it")
        }.map {
            println("daniel--- ${it.getOrNull()?.first()?.name}")
        }
    }
}

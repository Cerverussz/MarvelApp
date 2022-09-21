package com.devdaniel.marvelapp.di

import com.devdaniel.marvelapp.data.local.CharactersDao
import com.devdaniel.marvelapp.data.remote.CharactersApi
import com.devdaniel.marvelapp.data.repository.CharactersRepositoryImpl
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import com.devdaniel.marvelapp.domain.usecase.CharactersUC
import com.devdaniel.marvelapp.ui.characters.CharactersState
import com.devdaniel.marvelapp.ui.characters.CharactersViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object CharactersModule {

    @Provides
    fun charactersViewModelProvider(
        getCharactersUC: CharactersUC
    ) = CharactersViewModel(getCharactersUC, MutableStateFlow(CharactersState.Loading))

    @Provides
    @ViewModelScoped
    fun getCharactersUCProvider(
        charactersRepository: CharactersRepository
    ): CharactersUC = CharactersUC(charactersRepository)

    @Provides
    @ViewModelScoped
    fun charactersRepositoryProvider(
        charactersApi: CharactersApi,
        charactersDao: CharactersDao
    ): CharactersRepository = CharactersRepositoryImpl(charactersApi, charactersDao)

    @Provides
    @ViewModelScoped
    fun charactersApiProvider(
        retrofit: Retrofit
    ): CharactersApi = retrofit.create(CharactersApi::class.java)
}

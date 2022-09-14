package com.devdaniel.marvelapp.di

import com.devdaniel.marvelapp.data.remote.CharactersApi
import com.devdaniel.marvelapp.data.repository.CharactersRepositoryImpl
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import com.devdaniel.marvelapp.domain.usecase.GetCharactersUC
import com.devdaniel.marvelapp.ui.characters.CharactersViewModel
import com.devdaniel.marvelapp.ui.detail.CharactersState
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
        getCharactersUC: GetCharactersUC
    ) = CharactersViewModel(getCharactersUC, MutableStateFlow(CharactersState.Loading))

    @Provides
    @ViewModelScoped
    fun getCharactersUCProvider(
        charactersRepository: CharactersRepository
    ): GetCharactersUC = GetCharactersUC(charactersRepository)

    @Provides
    @ViewModelScoped
    fun charactersRepositoryProvider(
        charactersApi: CharactersApi
    ): CharactersRepository = CharactersRepositoryImpl(charactersApi)

    @Provides
    @ViewModelScoped
    fun charactersApiProvider(
        retrofit: Retrofit
    ): CharactersApi = retrofit.create(CharactersApi::class.java)
}

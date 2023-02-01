package com.devdaniel.marvelapp.di

import com.devdaniel.marvelapp.data.remote.CharacterDetailApi
import com.devdaniel.marvelapp.data.repository.ComicsCharacterRepositoryImpl
import com.devdaniel.marvelapp.domain.repository.ComicsCharacterRepository
import com.devdaniel.marvelapp.domain.usecase.ComicsCharacterUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object CharacterDetailModule {

    @Provides
    @ViewModelScoped
    fun getCharacterDetailUCProvider(
        characterDetailRepository: ComicsCharacterRepository
    ): ComicsCharacterUC = ComicsCharacterUC(characterDetailRepository)

    @Provides
    @ViewModelScoped
    fun characterDetailRepositoryProvider(
        characterDetailApi: CharacterDetailApi
    ): ComicsCharacterRepository = ComicsCharacterRepositoryImpl(characterDetailApi)

    @Provides
    @ViewModelScoped
    fun characterDetailApiProvider(
        retrofit: Retrofit
    ): CharacterDetailApi = retrofit.create(CharacterDetailApi::class.java)
}

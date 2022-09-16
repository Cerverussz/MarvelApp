package com.devdaniel.marvelapp.di

import com.devdaniel.marvelapp.data.remote.CharacterDetailApi
import com.devdaniel.marvelapp.data.repository.CharacterDetailRepositoryImpl
import com.devdaniel.marvelapp.domain.repository.CharacterDetailRepository
import com.devdaniel.marvelapp.domain.usecase.GetCharacterDetailUC
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
        characterDetailRepository: CharacterDetailRepository
    ): GetCharacterDetailUC = GetCharacterDetailUC(characterDetailRepository)

    @Provides
    @ViewModelScoped
    fun characterDetailRepositoryProvider(
        characterDetailApi: CharacterDetailApi
    ): CharacterDetailRepository = CharacterDetailRepositoryImpl(characterDetailApi)

    @Provides
    @ViewModelScoped
    fun characterDetailApiProvider(
        retrofit: Retrofit
    ): CharacterDetailApi = retrofit.create(CharacterDetailApi::class.java)
}

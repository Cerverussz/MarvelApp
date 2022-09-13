package com.devdaniel.marvelapp.di

import com.devdaniel.marvelapp.data.remote.CharactersApi
import com.devdaniel.marvelapp.data.repository.CharactersRepositoryImpl
import com.devdaniel.marvelapp.data.repository.exception.ExceptionCharacterRepositoryImpl
import com.devdaniel.marvelapp.domain.repository.CharactersRepository
import com.devdaniel.marvelapp.domain.repository.DomainExceptionRepository
import com.devdaniel.marvelapp.domain.usecase.GetCharactersUC
import com.devdaniel.marvelapp.ui.characters.CharactersViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object CharactersModule {

    @Provides
    fun charactersViewModelProvider(
        getCharactersUC: GetCharactersUC
    ) = CharactersViewModel(getCharactersUC)

    @ViewModelScoped
    @Provides
    fun getCharactersUCProvider(
        charactersRepository: CharactersRepository
    ): GetCharactersUC = GetCharactersUC(charactersRepository)

    @Named(EXCEPTION_CHARACTERS_REPOSITORY)
    @ViewModelScoped
    @Provides
    fun exceptionCharactersRepository(): DomainExceptionRepository =
        ExceptionCharacterRepositoryImpl()

    @ViewModelScoped
    @Provides
    fun charactersRepositoryProvider(
        charactersApi: CharactersApi,
        @Named(EXCEPTION_CHARACTERS_REPOSITORY) exceptionRepository: DomainExceptionRepository
    ): CharactersRepository = CharactersRepositoryImpl(charactersApi, exceptionRepository)

    @ViewModelScoped
    @Provides
    fun charactersApiProvider(
        retrofit: Retrofit
    ): CharactersApi = retrofit.create(CharactersApi::class.java)
}

private const val EXCEPTION_CHARACTERS_REPOSITORY = "exceptionCharactersRepository"

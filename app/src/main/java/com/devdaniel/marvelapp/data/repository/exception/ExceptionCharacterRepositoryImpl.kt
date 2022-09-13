package com.devdaniel.marvelapp.data.repository.exception

import com.devdaniel.marvelapp.data.exception.HttpErrors.getHttpError
import com.devdaniel.marvelapp.domain.exception.CommonErrors
import com.devdaniel.marvelapp.domain.exception.DomainException
import com.devdaniel.marvelapp.domain.repository.DomainExceptionRepository
import retrofit2.HttpException

class ExceptionCharacterRepositoryImpl : CommonErrors(), DomainExceptionRepository {
    override fun manageError(error: Throwable): DomainException {
        return if (error is HttpException) {
            getHttpError(error)
        } else {
            manageException(error)
        }
    }
}

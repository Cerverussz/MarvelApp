package com.devdaniel.marvelapp.domain.repository

import com.devdaniel.marvelapp.domain.exception.DomainException

interface DomainExceptionRepository {
    fun manageError(error: Throwable): DomainException
}

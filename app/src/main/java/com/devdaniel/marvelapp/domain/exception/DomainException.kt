package com.devdaniel.marvelapp.domain.exception

import java.io.IOException

open class DomainException(override val message: String = "") : Throwable(message)
object NotFoundException : DomainException()
object BadRequestException : DomainException()
object InternalErrorException : DomainException()
object UnknownError : DomainException()
object TimeOutException : DomainException()
object ParseException : DomainException()
object UnknownUser : DomainException()
object Unauthorized : DomainException()
object NoConnectivityDomainException : DomainException()
object NoConnectivityException : IOException()
data class HttpErrorCode(val code: Int, override val message: String) : DomainException()

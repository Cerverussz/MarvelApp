package com.devdaniel.marvelapp.data.exception

import com.devdaniel.marvelapp.domain.exception.BadRequestException
import com.devdaniel.marvelapp.domain.exception.DomainException
import com.devdaniel.marvelapp.domain.exception.HttpErrorCode
import com.devdaniel.marvelapp.domain.exception.InternalErrorException
import com.devdaniel.marvelapp.domain.exception.NotFoundException
import com.devdaniel.marvelapp.domain.exception.Unauthorized
import com.devdaniel.marvelapp.util.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

object HttpErrors {

    private val moshi = Moshi.Builder().build()
    private val jsonAdapter: JsonAdapter<DomainException> =
        moshi.adapter(DomainException::class.java)

    private val httpErrors = mapOf(
        HttpsURLConnection.HTTP_BAD_REQUEST to BadRequestException,
        HttpsURLConnection.HTTP_NOT_FOUND to NotFoundException,
        HttpsURLConnection.HTTP_INTERNAL_ERROR to InternalErrorException,
        HttpsURLConnection.HTTP_UNAUTHORIZED to Unauthorized
    )

    fun getHttpError(error: HttpException): DomainException {
        return if (httpErrors.containsKey(error.code())) {
            httpErrors.getValue(error.code())
        } else {
            HttpErrorCode(error.code(), getMessage(error).message)
        }
    }

    private fun getMessage(exception: HttpException): DomainException =
        runCatching {
            var jsonString = exception.response()?.errorBody()?.string()
            if (jsonString.isNullOrEmpty()) jsonString = Constants.JsonValues.JSON_EMPTY
            jsonAdapter.fromJson(jsonString)!!
        }.getOrDefault(DomainException(Constants.JsonValues.EMPTY))
}

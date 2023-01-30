package com.devdaniel.marvelapp.util

import com.devdaniel.marvelapp.util.Constants.HashValues.LENGTH
import com.devdaniel.marvelapp.util.Constants.HashValues.RADIX
import com.devdaniel.marvelapp.util.Constants.HashValues.SIG_NUM
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object Constants {

    const val BASE_URL = "https://gateway.marvel.com"
    val timestamp = Timestamp(System.currentTimeMillis()).time.toString()

    const val API_KEY = "e5353166155e4561e37e207ae1bff612"
    private const val PRIVATE_KEY = "bad94acd3b4fe23b47439719c97009a5f4ea274b"
    const val limit = "100"

    fun hash(): String {
        val input = timestamp.plus(PRIVATE_KEY).plus(API_KEY)
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(SIG_NUM, md.digest(input.toByteArray()))
            .toString(RADIX)
            .padStart(LENGTH, '0')
    }

    object HashValues {
        const val SIG_NUM = 1
        const val RADIX = 16
        const val LENGTH = 32
    }
}

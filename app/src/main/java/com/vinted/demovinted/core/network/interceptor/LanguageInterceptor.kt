package com.vinted.demovinted.core.network.interceptor

import okhttp3.Interceptor // HTTP middleware interface
import okhttp3.Response // an HTTP response
import java.util.Locale // language/region helper

class LanguageInterceptor : Interceptor { // tags each request with a language (defined but unused in main)

    override fun intercept(chain: Interceptor.Chain): Response { // runs for every outgoing request
        val requestBuilder = chain.request().newBuilder() // start editing the request
        requestBuilder.addHeader(HTTP_HEADER_ACCEPT_LANGUAGE, Locale.ENGLISH.toLanguageTag()) // add "Accept-Language: en"
        return chain.proceed(requestBuilder.build()) // send the modified request onward
    }

    companion object { // constants on the type itself
        private const val HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language" // the HTTP header name
    }
}
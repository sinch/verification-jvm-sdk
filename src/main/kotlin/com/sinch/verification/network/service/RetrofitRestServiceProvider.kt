package com.sinch.verification.network.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sinch.verification.network.ApiCallback
import com.sinch.verification.network.RetrofitCallback
import com.sinch.verification.network.auth.AuthorizationInterceptor
import com.sinch.verification.network.auth.AuthorizationMethod
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RetrofitRestServiceProvider(private val authorizationMethod: AuthorizationMethod) : RestServiceProvider {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(AuthorizationInterceptor(authorizationMethod))
            .addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://verificationapi-v1.sinch.com/verification/v1/") //TODO change to environment vars
            .addConverterFactory(
                Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
                    .asConverterFactory("application/json".toMediaType())
            )
            .client(okHttpClient)
            .build()
    }

    override fun <ServiceType> createService(service: Class<ServiceType>): ServiceType =
        retrofit.create(service)

    override fun <T> createCallback(apiCallback: ApiCallback<T>): RetrofitCallback<T> =
        RetrofitCallback(retrofit, apiCallback)

}
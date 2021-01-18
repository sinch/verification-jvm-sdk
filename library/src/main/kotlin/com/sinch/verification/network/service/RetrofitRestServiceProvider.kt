package com.sinch.verification.network.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sinch.verification.Constants
import com.sinch.verification.network.ApiCallback
import com.sinch.verification.network.RetrofitCallback
import com.sinch.verification.network.auth.AuthorizationInterceptor
import com.sinch.verification.network.auth.AuthorizationMethod
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

internal open class RetrofitRestServiceProvider(private val authorizationMethod: AuthorizationMethod) :
    RestServiceProvider {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(AuthorizationInterceptor(authorizationMethod))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.API_URL)
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
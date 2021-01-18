package com.sinch.verification.network.service

import com.sinch.verification.network.ApiCallback
import retrofit2.Callback

interface RestServiceProvider {
    fun <ServiceType> createService(service: Class<ServiceType>): ServiceType
    fun <T> createCallback(apiCallback: ApiCallback<T>): Callback<T>
}
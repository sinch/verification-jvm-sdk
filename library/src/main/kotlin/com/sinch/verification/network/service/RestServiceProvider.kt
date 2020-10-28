package com.sinch.verification.network.service

import com.sinch.verification.network.ApiCallback
import com.sinch.verification.network.RetrofitCallback

interface RestServiceProvider {
    fun <ServiceType> createService(service: Class<ServiceType>): ServiceType
    fun <T> createCallback(apiCallback: ApiCallback<T>): RetrofitCallback<T>
}
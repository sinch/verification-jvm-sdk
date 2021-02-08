package com.sinch.verification.network.service

import com.sinch.verification.network.ApiCallback
import okhttp3.ResponseBody
import retrofit2.Callback

//TODO Can we remove okhttp / retrofit dependencies from here?

interface RestServiceProvider {
    fun <ServiceType> createService(service: Class<ServiceType>): ServiceType
    fun <T> createCallback(apiCallback: ApiCallback<T>): Callback<T>
    fun createException(errorBody: ResponseBody?): Exception
}
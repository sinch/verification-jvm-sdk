package com.sinch.verification.process.method.initiation

import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.network.ApiCallback
import com.sinch.verification.process.listener.InitiationListener
import retrofit2.Response

class InitiationApiCallback(private val initiationListener: InitiationListener) : ApiCallback<InitiationResponseData> {

    override fun onSuccess(data: InitiationResponseData, response: Response<InitiationResponseData>) {
        initiationListener.onInitiated(data)
    }

    override fun onError(t: Throwable) {
        initiationListener.onInitializationFailed(t)
    }

}
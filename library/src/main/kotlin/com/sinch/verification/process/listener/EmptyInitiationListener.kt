package com.sinch.verification.process.listener

import com.sinch.verification.model.initiation.InitiationResponseData

open class EmptyInitiationListener : InitiationListener {

    override fun onInitiated(data: InitiationResponseData) {}

    override fun onInitializationFailed(t: Throwable) {}

}
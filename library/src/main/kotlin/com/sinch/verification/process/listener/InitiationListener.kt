package com.sinch.verification.process.listener

import com.sinch.verification.model.initiation.InitiationResponseData

/**
 * Interface defining methods notifying about verification initiation process result.
 */
interface InitiationListener {

    /**
     * Called if the initiation process has finished successfully.
     * @param data Extra data that might be required during actual verification process.
     */
    fun onInitiated(data: InitiationResponseData)

    /**
     * Called when the initiation process has failed.
     * @param t Error data.
     */
    fun onInitializationFailed(t: Throwable)
}
package com.sinch.verification.process.method.verification

import com.sinch.verification.model.verification.VerificationResponseData
import com.sinch.verification.model.verification.VerificationStatus
import com.sinch.verification.network.ApiCallback
import com.sinch.verification.process.listener.VerificationListener
import retrofit2.Response

class VerificationApiCallback(private val verificationListener: VerificationListener) :
    ApiCallback<VerificationResponseData> {

    override fun onSuccess(data: VerificationResponseData, response: Response<VerificationResponseData>) {
        /*
           In some case even though we got 200 status code the status field is set to ERROR.
            */
        if (data.status == VerificationStatus.SUCCESSFUL) {
            handleSuccessfulVerification()
        } else {
            handleError(VerificationException(data.errorReason.orEmpty()))
        }
    }

    override fun onError(t: Throwable) {
        handleError(t)
    }

    private fun handleSuccessfulVerification() {
        verificationListener.onVerified()
    }

    private fun handleError(t: Throwable) {
        verificationListener.onVerificationFailed(t)
    }

}
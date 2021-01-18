package com.sinch.verification.process.method.verification

import com.sinch.verification.model.VerificationState
import com.sinch.verification.model.VerificationStateStatus
import com.sinch.verification.model.verification.VerificationResponseData
import com.sinch.verification.model.verification.VerificationStatus
import com.sinch.verification.network.ApiCallback
import com.sinch.verification.process.listener.VerificationListener
import com.sinch.verification.process.method.VerificationStateListener
import retrofit2.Response

class VerificationApiCallback(
    private val verificationListener: VerificationListener,
    private val verificationStateListener: VerificationStateListener
) :
    ApiCallback<VerificationResponseData> {

    override fun onSuccess(data: VerificationResponseData, response: Response<VerificationResponseData>) {
        ifNotAlreadyVerified {
            /*
              In some case even though we got 200 status code the status field is set to ERROR.
               */
            if (data.status == VerificationStatus.SUCCESSFUL) {
                handleSuccessfulVerification()
            } else {
                handleError(VerificationException(data.errorReason.orEmpty()))
            }
        }
    }

    override fun onError(t: Throwable) {
        ifNotAlreadyVerified {
            handleError(t)
        }
    }

    private fun handleSuccessfulVerification() {
        verificationStateListener.update(
            VerificationState.Verification(
                VerificationStateStatus.SUCCESS
            )
        )
        verificationListener.onVerified()
    }

    private fun handleError(t: Throwable) {
        verificationStateListener.update(VerificationState.Verification(VerificationStateStatus.ERROR))
        verificationListener.onVerificationFailed(t)
    }

    /**
     * Makes sure the user has not already been verified by the verification instance.
     * @param f callback invoked if the verification process has not finished.
     */
    private fun ifNotAlreadyVerified(f: () -> Unit) {
        if (!verificationStateListener.verificationState.isVerified) {
            f()
        }
    }

}
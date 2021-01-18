package com.sinch.verification.process.method.initiation

import com.sinch.verification.model.VerificationLanguage
import com.sinch.verification.model.VerificationState
import com.sinch.verification.model.VerificationStateStatus
import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.model.initiation.withVerificationLanguages
import com.sinch.verification.network.ApiCallback
import com.sinch.verification.process.listener.InitiationListener
import com.sinch.verification.process.method.VerificationStateListener
import retrofit2.Response

class InitiationApiCallback(
    private val initiationListener: InitiationListener,
    private val verificationStateListener: VerificationStateListener
) : ApiCallback<InitiationResponseData> {

    override fun onSuccess(data: InitiationResponseData, response: Response<InitiationResponseData>) {
        ifNotManuallyStopped {
            verificationStateListener.update(VerificationState.Initialization(VerificationStateStatus.SUCCESS))
            initiationListener.onInitiated(
                data.withVerificationLanguages(
                    VerificationLanguage.fromContentLanguageTagHeader(response.headers()["content-language"])
                )
            )
        }
    }

    override fun onError(t: Throwable) {
        ifNotManuallyStopped {
            verificationStateListener.update(VerificationState.Initialization(VerificationStateStatus.ERROR))
            initiationListener.onInitializationFailed(t)
        }
    }

    private fun ifNotManuallyStopped(f: () -> Unit) {
        if (verificationStateListener.verificationState != VerificationState.ManuallyStopped) {
            f()
        }
    }

}
package com.sinch.verification.utils

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.verification.VerificationData
import com.sinch.verification.network.VerificationService
import com.sinch.verification.network.auth.AppKeyAuthorizationMethod
import com.sinch.verification.network.service.RestServiceProvider
import com.sinch.verification.network.service.RetrofitRestServiceProvider
import com.sinch.verification.process.listener.VerificationListener
import com.sinch.verification.process.method.EmptyVerificationStateListener
import com.sinch.verification.process.method.verification.VerificationApiCallback

/**
 * Helper object capable of making Sinch API calls directly without creating Verification instances.
 */
object VerificationCallUtils {

    fun verifyById(
        verificationId: String,
        verificationCode: String,
        method: VerificationMethodType,
        verificationListener: VerificationListener,
        appHash: String,
        restServiceProvider: RestServiceProvider = RetrofitRestServiceProvider(AppKeyAuthorizationMethod(appHash))
    ) {
        val service = restServiceProvider.createService(
            VerificationService::class.java
        )
        service.verifyById(
            subVerificationId = verificationId,
            data = VerificationData.forMethod(method, verificationCode)
        ).enqueue(
            restServiceProvider.createCallback(
                VerificationApiCallback(
                    verificationListener = verificationListener,
                    verificationStateListener = EmptyVerificationStateListener()
                )
            )
        )
    }

}
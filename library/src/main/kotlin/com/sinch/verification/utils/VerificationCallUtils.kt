package com.sinch.verification.utils

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.model.initiation.VerificationIdentity
import com.sinch.verification.model.initiation.VerificationInitiationData
import com.sinch.verification.model.verification.VerificationData
import com.sinch.verification.model.verification.VerificationResponseData
import com.sinch.verification.network.VerificationService
import com.sinch.verification.network.auth.AppKeyAuthorizationMethod
import com.sinch.verification.network.service.RestServiceProvider
import com.sinch.verification.network.service.RetrofitRestServiceProvider
import com.sinch.verification.process.method.VerificationMethod
import retrofit2.Response

/**
 * Helper object capable of making Sinch API calls directly without creating Verification instances.
 * This object was created as a workaround to solve problems connected with asynchronous handling of responses by [VerificationMethod]
 * (by 3rd party SDKs). It is advised to use [VerificationMethod] builder directly to create and verify phone numbers.
 */
object VerificationCallUtils {

    @JvmStatic
    @JvmOverloads
    @Throws
    fun initiateSynchronically(
        appHash: String,
        method: VerificationMethodType,
        number: String,
        custom: String? = null,
        honourEarlyReject: Boolean = true,
        reference: String? = null,
        acceptedLanguages: String? = null,
        restServiceProvider: RestServiceProvider = RetrofitRestServiceProvider(AppKeyAuthorizationMethod(appHash))
    ): InitiationResponseData {
        val service = restServiceProvider.createService(
            VerificationService::class.java
        )
        return service.initializeVerification(
            VerificationInitiationData(
                identity = VerificationIdentity(number),
                method = method,
                custom = custom,
                reference = reference,
                honourEarlyReject = honourEarlyReject
            ), acceptedLanguages
        ).execute().mapToBodyDataOrThrowException(restServiceProvider)
    }

    @JvmStatic
    @JvmOverloads
    @Throws
    fun verifySynchronicallyById(
        appHash: String,
        verificationId: String,
        verificationCode: String,
        method: VerificationMethodType,
        restServiceProvider: RestServiceProvider =
            RetrofitRestServiceProvider(AppKeyAuthorizationMethod(appHash))
    ): VerificationResponseData? {
        val service = restServiceProvider.createService(
            VerificationService::class.java
        )
        return service.verifyById(
            subVerificationId = verificationId,
            data = VerificationData.forMethod(method, verificationCode)
        ).execute().mapToBodyDataOrThrowException(restServiceProvider)
    }

    private fun <T> Response<T>.mapToBodyDataOrThrowException(serviceProvider: RestServiceProvider): T {
        val successBody = this.body()
        if (successBody != null) {
            return successBody
        } else {
            throw serviceProvider.createException(this.errorBody())
        }
    }

}
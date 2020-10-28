package com.sinch.verification.network

import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.model.initiation.VerificationInitiationData
import com.sinch.verification.model.verification.VerificationData
import com.sinch.verification.model.verification.VerificationResponseData
import retrofit2.Call
import retrofit2.http.*

interface VerificationService {

    @POST("verifications")
    fun initializeVerification(
        @Body data: VerificationInitiationData,
        @Header("Accept-Language") acceptedLanguages: String?
    ): Call<InitiationResponseData>

    /**
     * Verifies if given code is correct.
     * @param number Number to be verified.
     * @param data Verification data required for sms verification API call.
     * @return A [Call] object for the request.
     */
    @PUT("verifications/number/{number}")
    fun verifyNumber(
        @Path("number") number: String,
        @Body data: VerificationData
    ): Call<VerificationResponseData>
}
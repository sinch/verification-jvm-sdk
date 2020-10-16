package com.sinch.verification.network

import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.model.initiation.VerificationInitiationData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface VerificationService {

    @POST("verifications")
    fun initializeVerification(
        @Body data: VerificationInitiationData,
        @Header("Accept-Language") acceptedLanguages: String?
    ): Call<InitiationResponseData>

}
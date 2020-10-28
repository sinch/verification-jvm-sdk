package com.sinch.verification.model.initiation

import com.sinch.verification.model.VerificationLanguage
import com.sinch.verification.model.VerificationMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class InitiationResponseData(
    @SerialName("id") val id: String,
    @SerialName("method") val method: VerificationMethodType,
    @Transient val verificationLanguage: VerificationLanguage? = null
)

fun InitiationResponseData.withVerificationLanguages(verificationLanguage: VerificationLanguage?): InitiationResponseData =
    InitiationResponseData(
        id = this.id,
        method = this.method,
        verificationLanguage = verificationLanguage
    )
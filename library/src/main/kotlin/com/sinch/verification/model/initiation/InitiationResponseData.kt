package com.sinch.verification.model.initiation

import com.sinch.verification.model.VerificationLanguage
import com.sinch.verification.model.VerificationMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Class containing data returned by the API in response to verification initiation request.
 * @property id Id of the initiated verification.
 * @property method [VerificationMethodType] that will be used to verify the phone number.
 * @property verificationLanguage Language used by the verification method (ex. text message in case of [VerificationMethodType.SMS]).
 */
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
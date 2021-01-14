package com.sinch.verification.model.initiation

import com.sinch.verification.model.VerificationLanguage
import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.initiation.methods.*
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
    @SerialName("auto") val autoDetails: AutoInitializationResponseDetails = AutoInitializationResponseDetails(
        methodsOrder = emptyList()
    ),
    @SerialName("sms") val smsDetails: SmsInitializationDetails? = null,
    @SerialName("flashCall") val flashcallDetails: FlashCallInitializationDetails? = null,
    @SerialName("callout") val calloutDetails: CalloutInitializationDetails? = null,
    @SerialName("seamless") val seamlessDetails: SeamlessInitializationDetails? = null,
    @SerialName("method") val method: VerificationMethodType,
    @Transient val verificationLanguage: VerificationLanguage? = null
)

fun InitiationResponseData.withVerificationLanguages(verificationLanguage: VerificationLanguage?): InitiationResponseData =
    InitiationResponseData(
        id = this.id,
        method = this.method,
        verificationLanguage = verificationLanguage,
        smsDetails = this.smsDetails,
        flashcallDetails = this.flashcallDetails,
        calloutDetails = this.calloutDetails,
        seamlessDetails = this.seamlessDetails
    )

fun InitiationResponseData.subVerificationDetails(methodType: VerificationMethodType): InitiationDetails? =
    when (methodType) {
        VerificationMethodType.SMS -> smsDetails
        VerificationMethodType.FLASHCALL -> flashcallDetails
        VerificationMethodType.CALLOUT -> calloutDetails
        VerificationMethodType.SEAMLESS -> seamlessDetails
        else -> null
    }
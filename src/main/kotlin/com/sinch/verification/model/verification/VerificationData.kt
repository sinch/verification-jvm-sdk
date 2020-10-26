package com.sinch.verification.model.verification

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.verification.methods.FlashCallVerificationDetails
import com.sinch.verification.model.verification.methods.SmsVerificationDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationData(
    @SerialName("method") private val method: VerificationMethodType,
    @SerialName("sms") private val smsDetails: SmsVerificationDetails? = null,
    @SerialName("flashcall") private val flashCallDetails: FlashCallVerificationDetails? = null,
    @SerialName("code") private val calloutCode: String? = null
) {

    companion object {
        fun forMethod(method: VerificationMethodType, code: String) = when (method) {
            VerificationMethodType.SMS -> VerificationData(smsDetails = SmsVerificationDetails(code = code))
            VerificationMethodType.CALLOUT -> VerificationData(calloutCode = code)
            VerificationMethodType.FLASHCALL -> VerificationData(flashCallDetails = FlashCallVerificationDetails(cli = code))
            else -> error("Verification of type $method not supported")
        }
    }

    constructor(smsDetails: SmsVerificationDetails) : this(
        method = VerificationMethodType.SMS,
        smsDetails = smsDetails
    )

    constructor(calloutCode: String) : this(
        method = VerificationMethodType.CALLOUT,
        calloutCode = calloutCode
    )

    constructor(flashCallDetails: FlashCallVerificationDetails) : this(
        method = VerificationMethodType.FLASHCALL,
        flashCallDetails = flashCallDetails
    )

}
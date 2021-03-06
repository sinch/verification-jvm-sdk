package com.sinch.verification.model.verification

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.verification.methods.CalloutVerificationDetails
import com.sinch.verification.model.verification.methods.FlashCallVerificationDetails
import com.sinch.verification.model.verification.methods.SmsVerificationDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Class containing data that is passed to the backend as the actual code verification check.
 * @property method Method used to verify the phone number.
 * @property smsDetails Sms verification code details passed in case of [VerificationMethodType.SMS] method].
 * @property flashCallDetails Flashcall verification code details passed in case of [VerificationMethodType.FLASHCALL] method].
 * @property calloutDetails Callout verification code spoken by text-to-speech software in case of [VerificationMethodType.CALLOUT] method].
 */
@Serializable
data class VerificationData(
    @SerialName("method") val method: VerificationMethodType,
    @SerialName("sms") val smsDetails: SmsVerificationDetails? = null,
    @SerialName("flashcall") val flashCallDetails: FlashCallVerificationDetails? = null,
    @SerialName("callout") val calloutDetails: CalloutVerificationDetails? = null
) {

    companion object {
        fun forMethod(method: VerificationMethodType, code: String) = when (method) {
            VerificationMethodType.SMS -> VerificationData(smsDetails = SmsVerificationDetails(code = code))
            VerificationMethodType.CALLOUT -> VerificationData(calloutDetails = CalloutVerificationDetails(code = code))
            VerificationMethodType.FLASHCALL -> VerificationData(flashCallDetails = FlashCallVerificationDetails(cli = code))
            else -> error("Verification of type $method not supported")
        }
    }

    constructor(smsDetails: SmsVerificationDetails) : this(
        method = VerificationMethodType.SMS,
        smsDetails = smsDetails
    )

    constructor(calloutDetails: CalloutVerificationDetails) : this(
        method = VerificationMethodType.CALLOUT,
        calloutDetails = calloutDetails
    )

    constructor(flashCallDetails: FlashCallVerificationDetails) : this(
        method = VerificationMethodType.FLASHCALL,
        flashCallDetails = flashCallDetails
    )

}
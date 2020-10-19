package com.sinch.verification.model.verification

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.verification.methods.SmsVerificationDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationData(
    @SerialName("method") private val method: VerificationMethodType,
    @SerialName("sms") private val smsDetails: SmsVerificationDetails
) {

    companion object {
        fun forMethod(method: VerificationMethodType, code: String) = when (method) {
            VerificationMethodType.SMS -> VerificationData(smsCode = code)
            else -> VerificationData(smsCode = code)
        }
    }

    constructor(smsCode: String) : this(
        method = VerificationMethodType.SMS,
        smsDetails = SmsVerificationDetails(code = smsCode)
    )

}
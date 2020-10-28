package com.sinch.verification.model.verification

import com.sinch.verification.model.VerificationMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Class holding data about the initiated verification.
 * @param id Id of the verification process.
 * @param status Status of the verification.
 * @param method Method used for the verification.
 * @param reference Reference id that was passed (optionally) together with the verification request.
 * @param errorReason Error message if the verification process has failed, was denied or aborted null otherwise. [See](https://developers.sinch.com/docs/verification-rest-verification-api#verification-request).
 */
@Serializable
data class VerificationResponseData(
    @SerialName("id") val id: String,
    @SerialName("status") val status: VerificationStatus,
    @SerialName("method") val method: VerificationMethodType,
    @SerialName("reason") val errorReason: String? = null,
    @SerialName("reference") val reference: String? = null
)
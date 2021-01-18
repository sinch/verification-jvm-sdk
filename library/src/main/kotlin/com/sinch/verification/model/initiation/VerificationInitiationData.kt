package com.sinch.verification.model.initiation

import com.sinch.verification.model.VerificationMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data passed with verification initiation REST API call containing
 * information about the verification that will be constructed.
 * @property identity Client identity, currently object holding the phone number that should be verified.
 * @property method Method that should be used by the verification process.
 * @property honourEarlyReject Flag indicating if the verification process should honour early rejection rules.
 * @property custom Custom string that is passed with the initiation request.
 * @property reference Reference string that might be added for verification tracking purposes.
 */
@Serializable
data class VerificationInitiationData(
    @SerialName("identity") val identity: VerificationIdentity,
    @SerialName("method") val method: VerificationMethodType,
    @SerialName("honourEarlyReject") val honourEarlyReject: Boolean,
    @SerialName("custom") val custom: String?,
    @SerialName("reference") val reference: String?
)
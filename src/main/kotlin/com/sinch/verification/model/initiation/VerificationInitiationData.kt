package com.sinch.verification.model.initiation

import com.sinch.verification.model.VerificationMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationInitiationData(
    @SerialName("identity") val identity: VerificationIdentity,
    @SerialName("method") val method: VerificationMethodType,
    @SerialName("honourEarlyReject") val honourEarlyReject: Boolean,
    @SerialName("custom") val custom: String?,
    @SerialName("reference") val reference: String?
)
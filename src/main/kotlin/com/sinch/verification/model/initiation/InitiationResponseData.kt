package com.sinch.verification.model.initiation

import com.sinch.verification.model.VerificationMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiationResponseData(
    @SerialName("id") val id: String,
    @SerialName("method") val method: VerificationMethodType
)
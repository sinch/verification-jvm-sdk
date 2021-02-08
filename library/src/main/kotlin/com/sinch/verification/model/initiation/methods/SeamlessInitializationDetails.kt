package com.sinch.verification.model.initiation.methods

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.initiation.InitiationDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Class containing details (returned by the API) about the initiated seamless verification process.
 * @property targetUri URI address at which the client has to make a GET call.
 * @property subVerificationId Id assigned to each verification method that can be used in case of [VerificationMethodType.AUTO]
 */
@Serializable
data class SeamlessInitializationDetails(
    @SerialName("targetUri") val targetUri: String,
    @SerialName("subVerificationId") override val subVerificationId: String? = null
) : InitiationDetails
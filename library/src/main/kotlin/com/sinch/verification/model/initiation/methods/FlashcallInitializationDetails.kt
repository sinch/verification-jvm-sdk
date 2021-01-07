package com.sinch.verification.model.initiation.methods

import com.sinch.verification.model.initiation.InitiationDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.sinch.verification.model.VerificationMethodType

/**
 * Class containing details (returned by the API) about the initiated sms verification process.
 * @property subVerificationId Id assigned to each verification method that can be used in case of [VerificationMethodType.AUTO]
 */
@Serializable
data class FlashCallInitializationDetails(
    @SerialName("subVerificationId") override val subVerificationId: String? = null
) : InitiationDetails
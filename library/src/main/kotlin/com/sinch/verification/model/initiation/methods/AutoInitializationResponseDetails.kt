package com.sinch.verification.model.initiation.methods

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.initiation.InitiationDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutoInitializationResponseDetails(
    @SerialName("subVerificationId") override val subVerificationId: String? = null,
    @SerialName("methodsOrder") val methodsOrder: List<VerificationMethodType>
) :
    InitiationDetails {

    fun methodAfter(method: VerificationMethodType?): VerificationMethodType? {
        return if (method == null) {
            methodsOrder.firstOrNull()
        } else {
            methodsOrder.getOrNull(methodsOrder.indexOf(method) + 1)
        }
    }

}
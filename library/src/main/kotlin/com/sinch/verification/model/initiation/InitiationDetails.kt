package com.sinch.verification.model.initiation

import com.sinch.verification.model.VerificationMethodType

/**
 * Interface defining properties of specific verification method.
 */
interface InitiationDetails {

    /**
     * Id assigned to each verification method that can be used in case of [VerificationMethodType.AUTO]
     */
    val subVerificationId: String?

}
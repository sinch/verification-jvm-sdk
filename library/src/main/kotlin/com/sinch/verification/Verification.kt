package com.sinch.verification

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.VerificationState

/**
 * Interface collecting requirements each verification method must follow.
 * After constructing specific verification instance it has to be successfully initiated. Then depending on
 * specific method the verification code needs to be passed back to backend: either automatically intercepted
 * or manually typed by the user.
 */
interface Verification {

    /**
     * Current state of verification process.
     */
    val verificationState: VerificationState

    /**
     * Initiates the verification process.
     */
    fun initiate()

    /**
     * Verifies if provided code is correct.
     * @param verificationCode Code to be verified.
     * @param method Method of the verification if multiple sub methods are available (auto verification). For other
     * verification methods this parameter is ignored.
     */
    fun verify(verificationCode: String, method: VerificationMethodType? = null)

    /**
     * Stops the verification process. You can still verify the code manually for given verification, however all
     * the automatic interceptors are stopped.
     */
    fun stop()
}
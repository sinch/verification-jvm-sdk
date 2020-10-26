package com.sinch.verification.process.method

import com.sinch.verification.model.VerificationState

interface VerificationStateListener {
    fun update(newState: VerificationState)
    var verificationState: VerificationState
}
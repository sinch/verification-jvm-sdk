package com.sinch.verification.process.method

import com.sinch.verification.model.VerificationState

interface VerificationStateListener {
    fun update(newState: VerificationState)
    var verificationState: VerificationState
}

class EmptyVerificationStateListener : VerificationStateListener {

    override fun update(newState: VerificationState) {}

    override var verificationState: VerificationState
        get() = VerificationState.IDLE
        set(value) {}
}
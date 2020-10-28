package com.sinch.verification.process.method

import com.sinch.verification.Verification
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.listener.InitiationListener
import com.sinch.verification.process.listener.VerificationListener

interface VerificationConfigSetter {
    fun verificationConfig(verificationMethodConfig: VerificationMethodConfig): VerificationMethodFieldsSetter
}

interface VerificationMethodFieldsSetter {
    fun initiationListener(initiationListener: InitiationListener): VerificationMethodFieldsSetter
    fun verificationListener(verificationListener: VerificationListener): VerificationMethodFieldsSetter
    fun build(): Verification
}
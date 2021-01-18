package com.sinch.verification.process.listener

open class EmptyVerificationListener : VerificationListener {

    override fun onVerified() {}

    override fun onVerificationFailed(t: Throwable) {}

}
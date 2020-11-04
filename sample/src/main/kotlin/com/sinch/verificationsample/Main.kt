package com.sinch.verificationsample

import com.sinch.verification.Verification
import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.network.auth.AppKeyAuthorizationMethod
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.listener.InitiationListener
import com.sinch.verification.process.listener.VerificationListener
import com.sinch.verification.process.method.VerificationMethod

class Main {

    companion object : VerificationListener, InitiationListener {

        private const val APP_KEY = "9e556452-e462-4006-aab0-8165ca04de66"
        private var verification: Verification? = null

        @JvmStatic
        fun main(args: Array<String>) {
            val phoneNumber = inputWithMessage("Enter number that needs to be verified")
            val verificationConfig = VerificationMethodConfig.Builder.instance
                .authorizationMethod(AppKeyAuthorizationMethod(appKey = APP_KEY))
                .verificationMethod(VerificationMethodType.SMS)
                .number(phoneNumber)
                .build()

            verification = VerificationMethod.Builder.instance
                .verificationConfig(verificationConfig)
                .verificationListener(this)
                .initiationListener(this)
                .build()

            verification?.initiate()
        }

        override fun onVerified() {
            println("Successfully verified phone number!")
        }

        override fun onVerificationFailed(t: Throwable) {
            println("Error while verifying the code: ${t.localizedMessage}")
        }

        override fun onInitiated(data: InitiationResponseData) {
            verification?.verify(inputWithMessage("Enter received code"))
        }

        override fun onInitializationFailed(t: Throwable) {
            println("Error while initiation verification: ${t.localizedMessage}")
        }

        private fun inputWithMessage(msg: String): String {
            println(msg)
            return readLine().orEmpty()
        }

    }

}
package com.sinch.verification

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.network.auth.AppKeyAuthorizationMethod
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.method.VerificationMethod

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Kotlin main is running here!")
            val config = VerificationMethodConfig.Builder()
                .authorizationMethod(AppKeyAuthorizationMethod("9e556452-e462-4006-aab0-8165ca04de66"))
                .verificationMethod(VerificationMethodType.SMS)
                .number("+48509873255").build()
            val verification = VerificationMethod.Builder()
                .verificationConfig(config)
                .build()
            verification.initiate()

        }
    }

}
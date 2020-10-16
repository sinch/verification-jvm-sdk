package com.sinch.verification.process.method

import com.sinch.verification.Verification
import com.sinch.verification.model.VerificationState
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.listener.EmptyInitiationListener
import com.sinch.verification.process.listener.EmptyVerificationListener
import com.sinch.verification.process.listener.InitiationListener
import com.sinch.verification.process.listener.VerificationListener

class VerificationMethod private constructor(
    private val config: VerificationMethodConfig,
    private val initiationListener: InitiationListener = EmptyInitiationListener(),
    private val verificationListener: VerificationListener = EmptyVerificationListener()
) : Verification {

    override val verificationState: VerificationState
        get() = TODO("Not yet implemented")

    override fun initiate() {
        TODO("Not yet implemented")
    }

    override fun verify(verificationCode: String) {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    class Builder private constructor() : VerificationConfigSetter, VerificationMethodFieldsSetter {

        companion object {
            /**
             * Instance of builder that should be used to create [Verification] object.
             */
            @JvmStatic
            val instance: VerificationConfigSetter
                get() = Builder()

            operator fun invoke() = instance
        }

        private lateinit var verificationMethodConfig: VerificationMethodConfig

        private var initiationListener: InitiationListener = EmptyInitiationListener()
        private var verificationListener: VerificationListener = EmptyVerificationListener()

        override fun verificationConfig(verificationMethodConfig: VerificationMethodConfig): VerificationMethodFieldsSetter =
            apply {
                this.verificationMethodConfig = verificationMethodConfig
            }

        override fun initiationListener(initiationListener: InitiationListener): VerificationMethodFieldsSetter =
            apply {
                this.initiationListener = initiationListener
            }

        override fun verificationListener(verificationListener: VerificationListener): VerificationMethodFieldsSetter =
            apply {
                this.verificationListener = verificationListener
            }

        override fun build(): Verification = VerificationMethod(
            config = verificationMethodConfig,
            initiationListener = initiationListener,
            verificationListener = verificationListener
        )

    }

}
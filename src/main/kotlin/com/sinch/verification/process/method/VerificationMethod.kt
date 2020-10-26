package com.sinch.verification.process.method

import com.sinch.verification.Verification
import com.sinch.verification.model.VerificationState
import com.sinch.verification.model.VerificationStateStatus
import com.sinch.verification.model.asLanguagesString
import com.sinch.verification.model.verification.VerificationData
import com.sinch.verification.network.VerificationService
import com.sinch.verification.network.service.RetrofitRestServiceProvider
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.listener.EmptyInitiationListener
import com.sinch.verification.process.listener.EmptyVerificationListener
import com.sinch.verification.process.listener.InitiationListener
import com.sinch.verification.process.listener.VerificationListener
import com.sinch.verification.process.method.initiation.InitiationApiCallback
import com.sinch.verification.process.method.verification.VerificationApiCallback

class VerificationMethod private constructor(
    private val config: VerificationMethodConfig,
    private val initiationListener: InitiationListener = EmptyInitiationListener(),
    private val verificationListener: VerificationListener = EmptyVerificationListener(),
    private val serviceProvider: RetrofitRestServiceProvider = RetrofitRestServiceProvider(config.authorizationMethod)
) : Verification, VerificationStateListener {

    private val service by lazy {
        serviceProvider.createService(VerificationService::class.java)
    }

    override var verificationState: VerificationState = VerificationState.IDLE

    override fun initiate() {
        if (!verificationState.canInitiate) {
            return
        }
        update(VerificationState.Initialization(VerificationStateStatus.ONGOING))
        service.initializeVerification(
            data = config.initiationData,
            acceptedLanguages = config.acceptedLanguages.asLanguagesString()
        ).enqueue(
            serviceProvider.createCallback(
                InitiationApiCallback(
                    initiationListener = initiationListener,
                    verificationStateListener = this
                )
            )
        )
    }

    override fun verify(verificationCode: String) {
        service.verifyNumber(
            number = config.number,
            data = VerificationData.forMethod(config.verificationMethod, code = verificationCode)
        ).enqueue(
            serviceProvider.createCallback(
                VerificationApiCallback(
                    verificationListener = verificationListener,
                    verificationStateListener = this
                )
            )
        )
    }

    override fun update(newState: VerificationState) {
        this.verificationState = newState
    }

    override fun stop() {
        if (verificationState.isVerificationProcessFinished) {
            return
        }
        update(VerificationState.ManuallyStopped)
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
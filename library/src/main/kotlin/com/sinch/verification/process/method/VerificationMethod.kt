package com.sinch.verification.process.method

import com.sinch.verification.Verification
import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.VerificationState
import com.sinch.verification.model.VerificationStateStatus
import com.sinch.verification.model.asLanguagesString
import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.model.initiation.subVerificationDetails
import com.sinch.verification.model.verification.VerificationData
import com.sinch.verification.network.VerificationService
import com.sinch.verification.network.service.RestServiceProvider
import com.sinch.verification.network.service.RetrofitRestServiceProvider
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.listener.EmptyInitiationListener
import com.sinch.verification.process.listener.EmptyVerificationListener
import com.sinch.verification.process.listener.InitiationListener
import com.sinch.verification.process.listener.VerificationListener
import com.sinch.verification.process.method.initiation.InitiationApiCallback
import com.sinch.verification.process.method.verification.VerificationApiCallback
import com.sinch.verification.process.method.verification.VerificationException

class VerificationMethod internal constructor(
    internal val config: VerificationMethodConfig,
    internal val initiationListener: InitiationListener = EmptyInitiationListener(),
    internal val verificationListener: VerificationListener = EmptyVerificationListener(),
    internal val serviceProvider: RestServiceProvider = RetrofitRestServiceProvider(config.authorizationMethod)
) : Verification, VerificationStateListener, InitiationListener {

    private val service by lazy {
        serviceProvider.createService(VerificationService::class.java)
    }

    override var verificationState: VerificationState = VerificationState.IDLE

    private var initiationResponseData: InitiationResponseData? = null

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
                    initiationListener = this,
                    verificationStateListener = this
                )
            )
        )
    }

    override fun verify(verificationCode: String, method: VerificationMethodType?) {
        if (!verificationState.canVerify) {
            return
        }
        update(VerificationState.Verification(VerificationStateStatus.ONGOING))
        if (config.verificationMethod == VerificationMethodType.AUTO) {
            verifyBySubId(verificationCode, method)
        } else {
            verifyByNumber(verificationCode)
        }
    }

    private fun verifyByNumber(verificationCode: String) {
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

    private fun verifySeamlessly(targetUri: String) {
        service.verifySeamless(targetUri).enqueue(
            serviceProvider.createCallback(
                VerificationApiCallback(
                    verificationListener = verificationListener,
                    verificationStateListener = this
                )
            )
        )
    }

    private fun verifyBySubId(verificationCode: String, method: VerificationMethodType?) {
        if (method == null) {
            verificationListener.onVerificationFailed(VerificationException("Verification method has to be specified"))
            return
        }
        val subVerificationId = initiationResponseData?.subVerificationDetails(method)?.subVerificationId
        if (subVerificationId == null) {
            verificationListener.onVerificationFailed(VerificationException("Cannot create verification data for $method"))
            return
        }
        service.verifyById(
            subVerificationId = subVerificationId,
            data = VerificationData.forMethod(method, code = verificationCode)
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

    override fun onInitiated(data: InitiationResponseData) {
        initiationResponseData = data
        //If InitiationResponse contains seamless details why try verify seamlessly everytime.
        if (data.seamlessDetails != null) {
            verifySeamlessly(data.seamlessDetails.targetUri)
        }
        initiationListener.onInitiated(data)
    }

    override fun onInitializationFailed(t: Throwable) {
        initiationListener.onInitializationFailed(t)
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
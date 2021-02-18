package com.sinch.verification.process.config

import com.sinch.verification.metadata.factory.DefaultJVMMetadataFactory
import com.sinch.verification.metadata.factory.MetadataFactory
import com.sinch.verification.metadata.model.Metadata
import com.sinch.verification.model.VerificationLanguage
import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.model.initiation.VerificationIdentity
import com.sinch.verification.model.initiation.VerificationInitiationData
import com.sinch.verification.network.auth.AuthorizationMethod

class VerificationMethodConfig internal constructor(
    val authorizationMethod: AuthorizationMethod,
    val number: String,
    val verificationMethod: VerificationMethodType,
    val custom: String?,
    val reference: String?,
    val honourEarlyReject: Boolean,
    val acceptedLanguages: List<VerificationLanguage>,
    val metadata: Metadata
) {

    internal val initiationData by lazy {
        VerificationInitiationData(
            identity = VerificationIdentity(endpoint = this.number),
            honourEarlyReject = this.honourEarlyReject,
            reference = this.reference,
            custom = this.custom,
            method = this.verificationMethod,
            metadata = this.metadata
        )
    }

    class Builder private constructor() : NumberSetter, AuthMethodSetter, VerificationMethodSetter,
        ConfigFieldsSetter {

        companion object {
            /**
             * Instance of builder that should be used to create [VerificationMethodConfig.Builder] object.
             */
            @JvmStatic
            val instance: AuthMethodSetter
                get() = Builder()

            operator fun invoke() = instance
        }

        private lateinit var authorizationMethod: AuthorizationMethod
        private lateinit var number: String
        private lateinit var verificationMethod: VerificationMethodType

        private var custom: String? = null
        private var reference: String? = null
        private var honourEarlyReject = true
        private var acceptedLanguages: List<VerificationLanguage> = emptyList()
        private var metadataFactory: MetadataFactory = DefaultJVMMetadataFactory()

        override fun authorizationMethod(authorizationMethod: AuthorizationMethod): VerificationMethodSetter = apply {
            this.authorizationMethod = authorizationMethod
        }

        override fun verificationMethod(verificationMethod: VerificationMethodType): NumberSetter = apply {
            this.verificationMethod = verificationMethod
        }

        override fun number(number: String): ConfigFieldsSetter = apply {
            this.number = number
        }

        override fun honourEarlyReject(honourEarlyReject: Boolean): ConfigFieldsSetter = apply {
            this.honourEarlyReject = honourEarlyReject
        }

        override fun custom(custom: String?): ConfigFieldsSetter = apply {
            this.custom = custom
        }

        override fun reference(reference: String?): ConfigFieldsSetter = apply {
            this.reference = reference
        }

        override fun acceptedLanguages(acceptedLanguages: List<VerificationLanguage>): ConfigFieldsSetter = apply {
            this.acceptedLanguages = acceptedLanguages
        }

        override fun metadataFactory(metadataFactory: MetadataFactory): ConfigFieldsSetter = apply {
            this.metadataFactory = metadataFactory
        }

        override fun build(): VerificationMethodConfig =
            VerificationMethodConfig(
                authorizationMethod = authorizationMethod,
                number = number,
                verificationMethod = verificationMethod,
                custom = custom,
                reference = reference,
                honourEarlyReject = honourEarlyReject,
                acceptedLanguages = acceptedLanguages,
                metadata = metadataFactory.create()
            )

    }
}
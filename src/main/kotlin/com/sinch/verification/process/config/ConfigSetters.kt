package com.sinch.verification.process.config

import com.sinch.verification.model.VerificationMethodType
import com.sinch.verification.network.auth.AuthorizationMethod

interface AuthMethodSetter {
    fun authorizationMethod(authorizationMethod: AuthorizationMethod): VerificationMethodSetter
}

interface VerificationMethodSetter {
    fun verificationMethod(verificationMethod: VerificationMethodType): NumberSetter
}

interface NumberSetter {
    fun number(number: String): ConfigFieldsSetter
}

interface ConfigFieldsSetter {
    fun honourEarlyReject(honourEarlyReject: Boolean): ConfigFieldsSetter
    fun custom(custom: String?): ConfigFieldsSetter
    fun reference(reference: String?): ConfigFieldsSetter
    fun build(): VerificationMethodConfig
}

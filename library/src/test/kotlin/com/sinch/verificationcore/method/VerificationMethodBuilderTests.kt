package com.sinch.verificationcore.method

import com.sinch.verification.network.auth.AppKeyAuthorizationMethod
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.listener.EmptyInitiationListener
import com.sinch.verification.process.listener.EmptyVerificationListener
import com.sinch.verification.process.method.VerificationMethod
import com.sinch.verificationcore.TestConstants
import org.junit.Assert
import org.junit.Test

class VerificationMethodBuilderTests {

    companion object {
        val defaultConfig = VerificationMethodConfig(
            authorizationMethod = AppKeyAuthorizationMethod(""),
            honourEarlyReject = true,
            number = TestConstants.TEST_PHONE_NUMBER,
            reference = TestConstants.TEST_REFERENCE,
            custom = TestConstants.TEST_CUSTOM,
            verificationMethod = TestConstants.TEST_DEFAULT_METHOD,
            acceptedLanguages = emptyList(),
            metadata = TestConstants.TEST_METADATA
        )
    }

    @Test
    fun testMinimalConfigBuilderUsage() {
        val verification: VerificationMethod = VerificationMethod
            .Builder
            .instance
            .verificationConfig(defaultConfig)
            .build() as VerificationMethod

        Assert.assertEquals(verification.config, defaultConfig)
        Assert.assertTrue(verification.initiationListener is EmptyInitiationListener)
        Assert.assertTrue(verification.verificationListener is EmptyVerificationListener)
    }

    @Test
    fun testFullConfigBuilderUsage() {
        val initiationListener = EmptyInitiationListener()
        val verificationListener = EmptyVerificationListener()
        val verification: VerificationMethod = VerificationMethod
            .Builder
            .instance
            .verificationConfig(defaultConfig)
            .initiationListener(initiationListener)
            .verificationListener(verificationListener)
            .build() as VerificationMethod

        Assert.assertEquals(verification.config, defaultConfig)
        Assert.assertEquals(verification.initiationListener, initiationListener)
        Assert.assertEquals(verification.verificationListener, verificationListener)
    }
}
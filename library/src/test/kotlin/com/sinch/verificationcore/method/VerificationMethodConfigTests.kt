package com.sinch.verificationcore.method

import com.sinch.verification.metadata.collector.BasicLocaleCollector
import com.sinch.verification.metadata.factory.DefaultJVMMetadataFactory
import com.sinch.verification.metadata.model.Metadata
import com.sinch.verification.model.VerificationLanguage
import com.sinch.verification.model.initiation.VerificationIdentity
import com.sinch.verification.model.initiation.VerificationInitiationData
import com.sinch.verification.network.auth.AppKeyAuthorizationMethod
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verificationcore.TestConstants.TEST_APP_KEY
import com.sinch.verificationcore.TestConstants.TEST_CUSTOM
import com.sinch.verificationcore.TestConstants.TEST_DEFAULT_METHOD
import com.sinch.verificationcore.TestConstants.TEST_PHONE_NUMBER
import com.sinch.verificationcore.TestConstants.TEST_REFERENCE
import org.junit.Assert
import org.junit.Test

class VerificationMethodConfigTests {

    private val defaultAuthMethod by lazy {
        AppKeyAuthorizationMethod(TEST_APP_KEY)
    }

    @Test
    fun testMinimalConfigBuilderUsage() {
        val builtConfig = VerificationMethodConfig.Builder
            .instance
            .authorizationMethod(defaultAuthMethod)
            .verificationMethod(TEST_DEFAULT_METHOD)
            .number(TEST_PHONE_NUMBER)
            .build()

        Assert.assertEquals(TEST_PHONE_NUMBER, builtConfig.number)
        Assert.assertEquals(defaultAuthMethod, builtConfig.authorizationMethod)
        Assert.assertEquals(TEST_DEFAULT_METHOD, builtConfig.verificationMethod)
        Assert.assertEquals(emptyList<VerificationLanguage>(), builtConfig.acceptedLanguages)
        Assert.assertEquals(null, builtConfig.custom)
        Assert.assertEquals(true, builtConfig.honourEarlyReject)
        Assert.assertEquals(null, builtConfig.reference)
    }

    @Test
    fun testFullConfigBuilderUsage() {
        val verificationLanguage = VerificationLanguage("es", "ES")
        val testHonourEarlyReject = false

        val builtConfig = VerificationMethodConfig.Builder
            .instance
            .authorizationMethod(defaultAuthMethod)
            .verificationMethod(TEST_DEFAULT_METHOD)
            .number(TEST_PHONE_NUMBER)
            .acceptedLanguages(listOf(verificationLanguage))
            .custom(TEST_CUSTOM)
            .reference(TEST_REFERENCE)
            .honourEarlyReject(testHonourEarlyReject)
            .build()

        Assert.assertEquals(TEST_PHONE_NUMBER, builtConfig.number)
        Assert.assertEquals(defaultAuthMethod, builtConfig.authorizationMethod)
        Assert.assertEquals(TEST_DEFAULT_METHOD, builtConfig.verificationMethod)
        Assert.assertEquals(listOf(verificationLanguage), builtConfig.acceptedLanguages)
        Assert.assertEquals(TEST_CUSTOM, builtConfig.custom)
        Assert.assertEquals(testHonourEarlyReject, builtConfig.honourEarlyReject)
        Assert.assertEquals(TEST_REFERENCE, builtConfig.reference)
    }

    @Test
    fun testConfigConstructsProperInitiationData() {
        val verificationLanguage = VerificationLanguage("es", "ES")
        val testCustom = "customTest"
        val testReference = "referenceTest"
        val testHonourEarlyReject = false
        val testPlatformMetadata = "TEST_PLATFORM"

        val builtConfig = VerificationMethodConfig.Builder
            .instance
            .authorizationMethod(defaultAuthMethod)
            .verificationMethod(TEST_DEFAULT_METHOD)
            .number(TEST_PHONE_NUMBER)
            .acceptedLanguages(listOf(verificationLanguage))
            .custom(testCustom)
            .reference(testReference)
            .metadataFactory(DefaultJVMMetadataFactory(testPlatformMetadata))
            .honourEarlyReject(testHonourEarlyReject)
            .build()

        val configConstructedData = builtConfig.initiationData
        val constructorInitiationData = VerificationInitiationData(
            identity = VerificationIdentity(endpoint = TEST_PHONE_NUMBER),
            method = TEST_DEFAULT_METHOD,
            custom = testCustom,
            reference = testReference,
            honourEarlyReject = testHonourEarlyReject,
            metadata = Metadata.createUsing(testPlatformMetadata, BasicLocaleCollector())
        )

        Assert.assertEquals(configConstructedData, constructorInitiationData)
    }

}
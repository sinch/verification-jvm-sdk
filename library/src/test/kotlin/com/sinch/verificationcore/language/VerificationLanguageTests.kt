package com.sinch.verificationcore.language

import com.sinch.verification.model.VerificationLanguage
import org.junit.Assert
import org.junit.Test

class VerificationLanguageTests {

    @Test
    fun testHttpHeaderLanguageOnly() {
        val language = "pl"
        val verificationLanguage = VerificationLanguage(language)
        Assert.assertEquals(language, verificationLanguage.httpHeader)
    }

    @Test
    fun testHttpHeaderLanguageAndRegion() {
        val verificationLanguage = VerificationLanguage(language = "pl", region = "PL")
        Assert.assertEquals("pl-PL", verificationLanguage.httpHeader)
    }

    @Test
    fun testHttpHeaderAllFields() {
        val verificationLanguage =
            VerificationLanguage(language = "pl", region = "PL", weight = 0.5)
        Assert.assertEquals("pl-PL;q=0.5", verificationLanguage.httpHeader)
    }

    @Test
    fun testHttpHeaderLanguageAndWeight() {
        val verificationLanguage =
            VerificationLanguage(language = "pl", region = null, weight = 0.5)
        Assert.assertEquals("pl;q=0.5", verificationLanguage.httpHeader)
    }

    @Test
    fun testWeight3DigitsMax() {
        val verificationLanguage =
            VerificationLanguage(language = "pl", region = null, weight = 0.2335)
        Assert.assertEquals("pl;q=0.234", verificationLanguage.httpHeader)
    }

    @Test
    fun testContentLanguageParsingCorrect() {
        val verificationLanguage = VerificationLanguage.fromContentLanguageTagHeader("de-DE")
        Assert.assertEquals(verificationLanguage?.language, "de")
        Assert.assertEquals(verificationLanguage?.region, "DE")
    }

    @Test
    fun testContentLanguageParsingNull() {
        val verificationLanguage = VerificationLanguage.fromContentLanguageTagHeader(null)
        Assert.assertEquals(verificationLanguage, null)
    }

    @Test
    fun testContentLanguageParsingMalformedReturnsNull() {
        val verificationLanguage = VerificationLanguage.fromContentLanguageTagHeader("invalid")
        Assert.assertEquals(verificationLanguage, null)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testWeightLowerThen0() {
        val verificationLanguage =
            VerificationLanguage(language = "pl", region = null, weight = -0.1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testWeightGreaterThen1() {
        val verificationLanguage =
            VerificationLanguage(language = "pl", region = null, weight = 1.1)
    }

}
package com.sinch.verificationcore

import com.sinch.verification.metadata.collector.BasicLocaleCollector
import com.sinch.verification.metadata.model.Metadata
import com.sinch.verification.model.VerificationMethodType

object TestConstants {
    val TEST_DEFAULT_METHOD = VerificationMethodType.SMS
    const val TEST_PHONE_NUMBER = "+48123456789"
    const val TEST_APP_KEY = "app_key"
    const val TEST_REFERENCE = "testRef"
    const val TEST_CUSTOM = "testCus"
    val TEST_METADATA = Metadata.createUsing("JVM", BasicLocaleCollector())
}
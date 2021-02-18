package com.sinch.verification.metadata.model

import com.sinch.verification.metadata.collector.LocaleCollector
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Container class for all the metadata collected by the SDK.
 * @property platform System the library is installed on.
 * @property sdk Version of the sdk.
 * @property defaultLocale Locale of the device.
 */
@Serializable
data class Metadata private constructor(
    @SerialName("platform") val platform: String,
    @SerialName("sdk") val sdk: String,
    @SerialName("defaultLocale") val defaultLocale: String
) {
    companion object {

        /**
         * Version of JSON that is sent to the API.
         */
        const val METADATA_VERSION_NUMBER = 2

        /**
         * Version of the SDK that is defined in build.gradle file.
         */
        const val SDK_VERSION = "1.0.9"

        /**
         * Creates metadata object using specified collectors and properties.
         * @param platform System the library is installed on.
         * @param localeCollector Collector used to get locale metadata.
         */
        internal fun createUsing(
            platform: String,
            localeCollector: LocaleCollector
        ) =
            Metadata(
                platform = platform,
                sdk = SDK_VERSION,
                defaultLocale = localeCollector.collect()
            )
    }

    @SerialName("version")
    private val version = METADATA_VERSION_NUMBER

}
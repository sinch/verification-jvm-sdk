package com.sinch.verification.metadata.collector

typealias LocaleCollector = MetadataCollector<String>

/**
 * Base interface for collecting phone metadata used by Sinch API mostly for analytics and early
 * rejection rules.
 * @param Metadata Specific Metadata class.
 */
interface MetadataCollector<Metadata> {

    /**
     * Collects the phone metadata of type [Metadata].
     */
    fun collect(): Metadata

}
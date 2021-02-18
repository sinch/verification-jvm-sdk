package com.sinch.verification.metadata.factory

import com.sinch.verification.metadata.collector.BasicLocaleCollector
import com.sinch.verification.metadata.model.Metadata

class DefaultJVMMetadataFactory(val platform: String = "JVM") : MetadataFactory {

    override fun create(): Metadata {
        return Metadata.createUsing(platform, BasicLocaleCollector())
    }

}
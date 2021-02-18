package com.sinch.verification.metadata.collector

import java.util.*

/**
 * Metadata collector responsible for collecting device locale (string value).
 */
class BasicLocaleCollector() : LocaleCollector {

    override fun collect(): String = Locale.getDefault().toString()

}
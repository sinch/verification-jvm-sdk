package com.sinch.verification.model

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

data class VerificationLanguage(
    val language: String,
    val region: String? = null,
    val weight: Double? = null
) {

    companion object {
        const val REGION_PREFIX = "-"
        const val WEIGHT_PREFIX = ";q="
        const val DECIMAL_PATTERN = "#.###"

        fun fromContentLanguageTagHeader(value: String?): VerificationLanguage? {
            return if (value == null) {
                null
            } else {
                val split = value.split(REGION_PREFIX)
                if (split.size == 2) VerificationLanguage(split[0], split[1]) else null
            }
        }

    }

    init {
        if (weight != null && (weight > 1 || weight < 0)) {
            throw IllegalArgumentException("The weight value should be within range 0<=weight<=1")
        }
    }

    val httpHeader: String get() = asHttpHeader()

    private val weightString: String?
        get() = DecimalFormat(DECIMAL_PATTERN, DecimalFormatSymbols(Locale.UK)).run {
            weight?.let { format(it) }
        }

    private fun asHttpHeader(): String {
        val prefixedRegion = region?.prefixed(REGION_PREFIX)
        val prefixedWeight = weightString?.prefixed(WEIGHT_PREFIX)
        return "$language${prefixedRegion.orEmpty()}${prefixedWeight.orEmpty()}"
    }
}

fun List<VerificationLanguage>.asLanguagesString() =
    if (isEmpty())
        null
    else
        fold("") { accumulator, language ->
            "$accumulator,${language.httpHeader}"
        }.removePrefix(",")

fun String.prefixed(prefix: String): String? = "$prefix$this"
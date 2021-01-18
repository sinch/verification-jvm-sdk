package com.sinch.verification.model.verification.methods

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Class containing detailed information for the actual verification API request.
 * @property cli Full phone number of the incoming verification call.
 */
@Serializable
data class FlashCallVerificationDetails(@SerialName("cli") val cli: String)
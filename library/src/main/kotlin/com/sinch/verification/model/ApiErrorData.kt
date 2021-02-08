package com.sinch.verification.model

import com.sinch.verification.model.ApiErrorData.ErrorCodes.NumberMissingLeadingPlus
import com.sinch.verification.model.ApiErrorData.ErrorCodes.ParameterValidation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Class containing detailed information about what went wrong during the API call. (Server did not return 2xx status).
 * @property errorCode Integer defining specific error.
 * @property message Human readable message describing why API call has failed
 * @property reference Optional reference id that was passed with the request.
 */
@Serializable
data class ApiErrorData(
    @SerialName("errorCode") val errorCode: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("reference") val reference: String? = null
){

    object ErrorCodes {
        const val ParameterValidation = 40001
        const val NumberMissingLeadingPlus = 40005
    }

    /**
     * Flag indicating if error was probably caused my malformed phone number passed to request
     * (too short, too long, wrong characters etc.). Note that this flag can return true even when the cause was actually
     * different as the error code can be only be checked against 'ParameterValidation' error constant.
     */
    val mightBePhoneFormattingError: Boolean get() =
        errorCode == ParameterValidation || errorCode == NumberMissingLeadingPlus
}
package com.sinch.verification.network

import com.sinch.verification.model.ApiErrorData
import okhttp3.ResponseBody
import retrofit2.Retrofit

/**
 * Converts Sinch Api error response Body to [ApiErrorData] object representing the error.
 * @param retrofit retrofit instance which converter is used to parse the JSON.
 * @return [ApiErrorData] object representing error or null if the body could not be parsed properly.
 */
internal fun ResponseBody.convertToApiErrorData(retrofit: Retrofit): ApiErrorData? {
    val responseBodyConverter =
        retrofit.responseBodyConverter<ApiErrorData>(ApiErrorData::class.java, emptyArray())
    return responseBodyConverter.convert(this)

}
package com.sinch.verification.process

import com.sinch.verification.model.ApiErrorData

/**
 * Exception representing errors returned by Sinch verification API.
 * @param data Detailed data containing more information about what went wrong.
 */
class ApiCallException(val data: ApiErrorData) : Exception(data.message)
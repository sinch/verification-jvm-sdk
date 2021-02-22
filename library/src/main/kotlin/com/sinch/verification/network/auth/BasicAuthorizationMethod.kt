package com.sinch.verification.network.auth

import okhttp3.Credentials
import okhttp3.Request

/**
 * [AuthorizationMethod] that uses application key and application secret to authorize API requests. To get the key check your application page on
 * [Sinch Dashboard](https://portal.sinch.com/)
 * @param appKey Application key assigned to the app.
 * @param appSecret Application secret assigned to the app.
 */
class BasicAuthorizationMethod(private val appKey: String, private val appSecret: String) :
    AuthorizationMethod {

    override fun onPrepareAuthorization() {}

    override fun onAuthorize(request: Request): Request {
        val credentials = Credentials.basic(appKey, appSecret)
        return request.newBuilder().addHeader("Authorization", credentials).build()
    }

}
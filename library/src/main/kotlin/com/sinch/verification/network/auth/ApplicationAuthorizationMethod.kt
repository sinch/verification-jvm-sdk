package com.sinch.verification.network.auth

import kotlinx.serialization.toUtf8Bytes
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import java.security.MessageDigest
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * [AuthorizationMethod] that uses application key and application secret to authorize API requests. To get the key check your application page on
 * [Sinch Dashboard](https://portal.sinch.com/)
 * @param appKey Application key assigned to the app.
 * @param appSecret Application secret assigned to the app.
 */
class ApplicationAuthorizationMethod(private val appKey: String, private val appSecret: String) : AuthorizationMethod {

    companion object {
        const val TIMESTAMP_HEADER = "x-timestamp"
    }

    override fun onPrepareAuthorization() {}

    override fun onAuthorize(request: Request): Request {
        val timestamp = Instant.now().toString()
        val scheme = "Application"
        val decodedAppSecret = Base64.getDecoder().decode(appSecret)
        val stringToSign = calculateStringToSign(request, timestamp)
        val signature = encode(decodedAppSecret, stringToSign)

        return request.newBuilder()
            .addHeader(TIMESTAMP_HEADER, timestamp)
            .addHeader("Authorization", "$scheme $appKey:$signature")
            .build()
    }

    private fun encode(key: ByteArray, data: String): String {
        return try {
            val sha256Hmac = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(key, "HmacSHA256")
            sha256Hmac.init(secretKey)
            Base64.getEncoder().encodeToString(
                sha256Hmac.doFinal(data.toUtf8Bytes())
            )
        } catch (e: Exception) {
            ""
        }
    }

    private fun calculateStringToSign(request: Request, timestampValue: String): String {
        val contentTypeValue = request.header("Content-Type") ?: request.body?.contentType()
        val canonicalHeaders = "$TIMESTAMP_HEADER:$timestampValue"
        val canonicalPath = request.url.encodedPath
        return request.method + "\n" +
                request.body.md5Hash + "\n" +
                contentTypeValue + "\n" +
                canonicalHeaders + "\n" +
                canonicalPath
    }

    private val RequestBody?.md5Hash: String
        get() {
            val utf8Body = utf8Body
            if (utf8Body.isEmpty()) {
                return ""
            }
            val md = MessageDigest.getInstance("MD5")
            val array = md.digest(utf8Body.toUtf8Bytes())
            return Base64.getEncoder().encodeToString(array)
        }

    private val RequestBody?.utf8Body: String
        get() {
            if (this == null) {
                return ""
            }
            return Buffer().also {
                this.writeTo(it)
            }.readUtf8()
        }

}
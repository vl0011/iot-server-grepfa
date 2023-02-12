package com.grepfa.iot.grepfa

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.io.Serializable


@Component
class OAuthHelper {
    private val restTemplate = RestTemplate()

    @Bean
    fun accessToken(): String {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("client_id", CLIENT_ID)
        params.add("client_secret", CLIENT_SECRET)
        params.add("scope", SCOPE)
        params.add("grant_type", GRANT_TYPE)
        val response = restTemplate.postForEntity(
            TOKEN_URL, params,
            TokenResponse::class.java
        )
        return if (response.statusCode === HttpStatus.OK) {
            response.body!!.access_token
        } else {
            throw RuntimeException("인증 안됌")
        }
    }

    data class TokenResponse(
        val access_token: String,
        val expires_in: Long,
        val refresh_expires_in: Long,
        val token_type: String,
        val scope: String
    ) : Serializable

    companion object {
        private const val TOKEN_URL = "https://auth.grepfa.com/realms/grepfa/protocol/openid-connect/token"
        private const val CLIENT_ID = "iot-server"
        private const val CLIENT_SECRET = "iMtb2fevuZRetMeGolJsIKGtOJWn2byd"
        private const val SCOPE = "iot"
        private const val GRANT_TYPE = "client_credentials"
    }
}
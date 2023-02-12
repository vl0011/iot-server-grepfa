package com.grepfa.iot.grepfa.mqtt.grepfa.up

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Forwarder {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun handle() {
        logger.info("up event arrived: ")
    }

    suspend fun forwardToHttp() {
        withContext(Dispatchers.IO) {

        }
    }
}

data class EventUp(
    val address: String,
    val msgId: String,
    val data: String,
    val time: String,
)
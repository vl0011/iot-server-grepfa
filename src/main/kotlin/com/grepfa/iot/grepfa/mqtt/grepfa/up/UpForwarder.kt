package com.grepfa.iot.grepfa.mqtt.grepfa.up

import com.grepfa.iot.grepfa.data.device.DeviceRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.scheduler.Schedulers

@Component
class UpForwarder(val repo: DeviceRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @OptIn(DelicateCoroutinesApi::class)
    fun handle() {



        logger.info("up event forwarding")

        GlobalScope.launch {
            Dispatchers.IO{
                HttpClient()
            }
        }
    }
}

data class EventUp(
    val address: String,
    val msgId: String,
    val data: String,
    val time: String,
)

//data class ForwardedPayload(
//
//)
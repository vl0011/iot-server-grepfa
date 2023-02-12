package com.grepfa.iot.grepfa.mqtt.grepfa.up

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.grepfa.iot.grepfa.data.device.DeviceDto
import com.grepfa.iot.grepfa.data.device.DeviceRepository
import com.grepfa.iot.grepfa.mqtt.grepfa.TopicInfo
import com.grepfa.iot.grepfa.mqtt.grepfa.up.dto.EventUpDto
import com.grepfa.iot.grepfa.mqtt.grepfa.up.dto.EventUpElementDto
import com.grepfa.iot.grepfa.mqtt.grepfa.up.dto.RawUpDto
import com.grepfa.iot.grepfa.mqtt.grepfa.up.dto.RawUpElement
import com.grepfa.iot.grepfa.part.GetPartDto
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.request.*
//import io.ktor.http.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.core.scheduler.Schedulers
import java.util.*

@Component
class UpForwarder(val repo: DeviceRepository, val om: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun handle(topicString: TopicInfo, payload: String) {
        logger.info("up event forwarding")
        logger.info("topic: $topicString")
        logger.info("payload: $payload")

        val raw = try {
            om.readValue<RawUpDto>(payload)
        } catch (e: Exception) {
            logger.warn("invalid up payload")
            return
        }
        val d = try {
            upPayloadParse(raw)
        } catch (e: Exception) {
         logger.warn("parsing payload message error: ${e.message}")
        }

        val retStr = om.writeValueAsString(d)
        logger.debug("parsed: $retStr")

//        GlobalScope.launch {
//            Dispatchers.IO{
//                HttpClient()
//            }
//        }
    }

    fun upPayloadParse(raw: RawUpDto) : EventUpDto {
        val dbDev = DeviceDto(repo.findById(raw.deviceId).get())


        val pList = raw.values.map {
            val p = GetPartDto(dbDev.profile.contains.find { b-> b.name ==  it.name}!!)
            EventUpElementDto(
                partId = p.id!!,
                unit = p.unit,
                max = p.max,
                min = p.min,
                name = p.name,
                summary = p.summary,
                description = p.description,
                value = it.value,
                varType = p.varType
            )
        }

        val net = dbDev.network.find {
            it.address == raw.address
        }

        return EventUpDto(
            eventId = UUID.randomUUID(),
            deviceId = dbDev.id!!,
            profileId = dbDev.profile.id!!,
            address = net!!.address,
            networkType = net.type,
            data = pList
        )
    }

    suspend fun getUpPayloadExample(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            EventUpDto(
                eventId = UUID.randomUUID(),
                deviceId = UUID.randomUUID(),
                profileId = UUID.randomUUID(),
                address = "network address",
                networkType = "tcp_ip",
                data = listOf(
                    EventUpElementDto(
                        partId = UUID.randomUUID(),
                        min = 0.0,
                        max = 0.0,
                        unit = "mm",
                        summary = "summary of part information",
                        description = "description of part information",
                        name = "part name",
                        varType = "integer",
                        value = "12"
                    ),
                    EventUpElementDto(
                        partId = UUID.randomUUID(),
                        min = 0.0,
                        max = 0.0,
                        unit = "ppm",
                        summary = "co2 sensor",
                        description = "description of part information",
                        name = "co2",
                        varType = "float",
                        value = "153.2"
                    )
                )
            )
        )
    }
//    suspend fun getRawUpExample(req: ServerRequest): ServerResponse {
//        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
//            RawUpDto(
//                deviceId = UUID.randomUUID(),
//                address = "address of device",
//                values = listOf(
//                    RawUpElement(
//
//                    )
//                )
//            )
//        )
//    }
}

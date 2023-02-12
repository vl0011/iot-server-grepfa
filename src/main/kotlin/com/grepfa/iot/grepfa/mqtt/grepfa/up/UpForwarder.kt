package com.grepfa.iot.grepfa.mqtt.grepfa.up

//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.request.*
//import io.ktor.http.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.grepfa.iot.grepfa.data.device.DeviceDto
import com.grepfa.iot.grepfa.data.device.DeviceRepository
import com.grepfa.iot.grepfa.mqtt.grepfa.TopicInfo
import com.grepfa.iot.grepfa.mqtt.grepfa.up.dto.*
import com.grepfa.iot.grepfa.part.GetPartDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.*

@Component
class UpForwarder(val repo: DeviceRepository, val om: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val upForwardURL = "http://115.31.121.170:80"

    @OptIn(DelicateCoroutinesApi::class)
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
        logger.info("parsed: $retStr")

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                logger.info("start request")
                val client = HttpClient(CIO)
                val resp = client.post(upForwardURL) {
                    contentType(ContentType.Application.Json)
                    setBody(retStr)
                }
                logger.info("status: ${resp.status}")
                logger.info("body: ${resp.body<String>()}")
            }
        }
    }

    fun upPayloadParse(raw: RawUpDto): EventUpDto {
        val dbDev = DeviceDto(repo.findById(raw.deviceId).get())


        val pList = raw.values.map {
            val p = GetPartDto(dbDev.profile.contains.find { b -> b.name == it.name }!!)
            EventUpElementDto(
                p.name, p.summary, p.description, p.id!!, p.unit, p.max, p.min, it.value, p.varType, p.type
            )
        }

        val net = dbDev.network.find {
            it.address == raw.address
        }

        return EventUpDto(UUID.randomUUID(), dbDev.id!!, dbDev.profile.id!!, net!!.address, net.type, pList)
    }


    suspend fun getUpRawExampleWithDeviceId(req: ServerRequest): ServerResponse {
        val d = withContext(Dispatchers.IO) {
            DeviceDto(repo.findById(UUID.fromString(req.pathVariable("id"))).get())
        }

        val pList = d.profile.contains.map {
            RawUpElement(
                it.name, Random().nextDouble(45.0).toString()
            )
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            RawUpDto(d.id!!, d.network.first().address, pList)
        )
    }

    suspend fun getUpPayloadExampleWithDeviceId(req: ServerRequest): ServerResponse {
        val d = withContext(Dispatchers.IO) {
            DeviceDto(repo.findById(UUID.fromString(req.pathVariable("id"))).get())
        }

        val pList = d.profile.contains.map {
            EventUpElementDto(it.name, it.summary, it.description, it.id!!, it.unit, it.max, it.min, Random().nextInt(10).toString(), it.varType, it.type)
        }

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            EventUpDto(UUID.randomUUID(), d.id!!, d.profile.id!!, d.network.first().address, d.network.first().type, pList)
        )

    }

    suspend fun getUpPayloadExample(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            EventUpDto(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "network address", "tcp_ip", listOf(
                    EventUpElementDto("part name", "summary of part information", "description of part information", UUID.randomUUID(), "mm", 0.0, 0.0, "12", "integer", "actuator"),
                    EventUpElementDto("co2", "co2 sensor", "description of part information", UUID.randomUUID(), "ppm", 0.0, 0.0, "153.2", "float", "sensor")
                )
            )
        )
    }

    suspend fun getRawUpExample(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            RawUpDto(
                UUID.randomUUID(), "address of device", listOf(
                    RawUpElement(
                        "part name", "part value",
                    )
                )
            )
        )
    }
}

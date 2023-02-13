package com.grepfa.iot.grepfa.mqtt.grepfa.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.grepfa.iot.grepfa.data.part.PartRepository
import com.grepfa.iot.grepfa.mqtt.grepfa.down.DownRequestDto
import com.grepfa.iot.grepfa.part.GetPartDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*

@Component
class Down(
    @Qualifier("outboundGateway") val gw: OutboundGateway,
    val pr: PartRepository,
    val om: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    suspend fun downHandler(req: ServerRequest): ServerResponse {
        logger.info("mqtt down request...")

        val reqPayload = req.awaitBody<DownRequestDto>()

        val p = try {
            // 파츠 조회
            withContext(Dispatchers.IO) {
                GetPartDto(
                    pr.findById(UUID.fromString(reqPayload.partId)).get()
                )
            }
        } catch (e: Exception) {
            return ServerResponse.badRequest().bodyValueAndAwait("part not found")
        }

        if (p.type != "actuator") {
            return ServerResponse.badRequest().bodyValueAndAwait("not actuator")
        }

        // 네트워크 조회

        // lora or wifi 로 전송
        val pStr = om.writeValueAsString(RawDownDto(p.name, reqPayload.value))
        val tStr = "/grepfa/xxx/device/${p.id}/command/down"
        logger.info("topic: $tStr")
        logger.info("payload: $pStr")
        gw.sendToMqtt(pStr, tStr)
        return ServerResponse.ok().buildAndAwait()
    }
}
package com.grepfa.iot.grepfa.data.device

import com.grepfa.iot.grepfa.data.network.NetworkDto
import com.grepfa.iot.grepfa.data.part.CreatePartDto
import com.grepfa.iot.grepfa.data.profile.CreateProfileDto
import com.grepfa.iot.grepfa.data.profile.ProfileRepository
import com.grepfa.iot.type.GResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*

@Component
class DeviceHandler(val repo: DeviceRepository, val pRepo: ProfileRepository) {
    suspend fun newDevice(req: ServerRequest): ServerResponse {
        val cd = req.awaitBody<CreateDeviceDto>()
        val res = withContext(Dispatchers.IO) {
            val t = pRepo.getReferenceById(cd.profile)
            val e = repo.save(DeviceDto(id = null, cd.name, cd.network, t).toEntity())
            GetDeviceDto(e)
        }

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            GResponse(HttpStatus.CREATED.value(), "", res)
        )
    }
    suspend fun getAllDeviceId(req: ServerRequest) : ServerResponse {
        val idList = withContext(Dispatchers.IO) {
            repo.getAllIds()
        }

        return if (idList == null) ServerResponse.notFound().buildAndAwait()
        else
            ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
                GResponse(HttpStatus.OK.value(), "", idList)
            )
    }
    suspend fun findFromUUID(req: ServerRequest): ServerResponse {
        val device = withContext(Dispatchers.IO) {
            repo.findById(UUID.fromString(req.pathVariable("id")))
        }.map { GetDeviceDto(it) }
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(device)
    }

    suspend fun getExampleNewDevice(req: ServerRequest): ServerResponse {
        val ex = CreateDeviceDto("name", listOf(NetworkDto(type = "lorawan", address = "lora address")), profile = UUID.randomUUID())
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            GResponse(HttpStatus.OK.value(), "", ex)
        )
    }

}
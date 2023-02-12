package com.grepfa.iot.grepfa.data.profile

import com.grepfa.iot.grepfa.data.part.CreatePartDto
import com.grepfa.iot.type.GResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.util.*

@Component
class ProfileHandler(private val repository: ProfileRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)
    suspend fun createNewProfile(req: ServerRequest) : ServerResponse {
        logger.info("adding new profile...")
        val data = req.awaitBody<CreateProfileDto>()
        val result = withContext(Dispatchers.IO) {
            repository.save(data.toEntity())
        } //.awaitSingle()

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            GResponse(HttpStatus.CREATED.value(), "", result)
        )
    }
    suspend fun getAllProfileId(req: ServerRequest) : ServerResponse {
        val idList = withContext(Dispatchers.IO) {
            repository.getAllIds()
        }

        if (idList == null) return ServerResponse.notFound().buildAndAwait()
        else
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            GResponse(HttpStatus.OK.value(), "", idList)
        )
    }

    suspend fun findFromId(req: ServerRequest) : ServerResponse {
        val profileList = withContext(Dispatchers.IO) {
            repository.findById(UUID.fromString(req.pathVariable("id")))
        }.map { GetProfileDto(it) }
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(profileList).switchIfEmpty(notFound().build()).awaitSingle()

    }

    suspend fun getExampleNewProfile(req: ServerRequest) : ServerResponse {
        val ex = CreateProfileDto("hello", listOf(CreatePartDto("name", "sensor or actuator", "float or integer or string", "desc", "summary", 0.0, 100.0)))
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(
            GResponse(HttpStatus.OK.value(), "", ex)
        )
    }
}
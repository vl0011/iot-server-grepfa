package com.grepfa.iot.grepfa.profile

import com.grepfa.iot.exception.BadArgumentException
import com.grepfa.iot.grepfa.part.CreatePartDto
import com.grepfa.iot.grepfa.part.GetPartDto
import java.io.Serializable
import java.util.*

/**
 * A DTO for the {@link com.grepfa.iot.grepfa.profile.Profile} entity
 */
data class CreateProfileDto(
    val name: String? = null,
    val parts: List<CreatePartDto>? = null
) : Serializable {
    fun toEntity() : Profile {
        name?: throw BadArgumentException("argument name is null")
        parts?: throw BadArgumentException("argument parts is null")
        return Profile(
            name = this.name,
            contains = parts.map { it.toEntity() }
        )
    }
}

class GetProfileDto(profile: Profile):Serializable {
    val id = profile.id
    val name = profile.name
    val parts = profile.contains.map { GetPartDto(it) }
}
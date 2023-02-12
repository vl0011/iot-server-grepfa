package com.grepfa.iot.grepfa.part

import com.grepfa.iot.grepfa.profile.Profile
import java.io.Serializable
import java.util.*

/**
 * A DTO for the {@link com.grepfa.iot.grepfa.part.Part} entity
 */
data class CreatePartDto(
    val name: String? = null,
    val type: String? = null,
    val varType: String? = null,
    val description: String? = null,
    val summary: String? = null,
    val unit: String? = null,
    val min: Double? = null,
    val max: Double? = null
) : Serializable {
    fun toEntity(): Part {
        return Part(
            name = name!!,
            type = type!!,
            varType = varType!!,
            description = description!!,
            summary = summary!!,
            unit = unit!!,
            max = max!!,
            min = min!!
        )
    }

}


class GetPartDto(part: Part) :Serializable {
    val id = part.id
    val name = part.name
    val type = part.type
    val varType = part.type
    val description = part.description
    val summary = part.summary
    val unit = part.unit
    val min = part.min
    val max = part.max
}
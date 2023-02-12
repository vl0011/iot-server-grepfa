package com.grepfa.iot.grepfa.network

import java.io.Serializable
import java.util.*

/**
 * A DTO for the {@link com.grepfa.iot.grepfa.network.Network} entity
 */
data class NetworkDto(
    val id: UUID? = null,
    val type: String,
    val address: String
) : Serializable {
    fun toEntity(): Network {
        return Network(type = type, address = address)
    }

    constructor(n: Network) : this(n.id, n.type, n.address)
}

